package com.example.fitnessrecord.global.auth.sercurity.jwt;

import com.example.fitnessrecord.domain.user.type.UserType;
import com.example.fitnessrecord.global.auth.dto.TokenResponse;
import com.example.fitnessrecord.global.auth.service.AuthService;
import com.example.fitnessrecord.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

  @Value("${spring.jwt.secret}")
  private String secretKey;

  private static final String KEY_ROLES = "roles";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;// 24시간
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 30;// 한 달
  private final AuthService authService;

  /**
   * 토큰 생성 (발급)
   */
  public TokenResponse generateToken(String email, UserType userType) {
    List<String> roles = getRolesByUserType(userType);

    Claims claims = Jwts.claims().setSubject(email);
    claims.put(KEY_ROLES, roles);

    var now = new Date();
    var accessTokenexpiredDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
    var refreshTokenexpiredDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

    String accessToken = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now) // 토큰 생성 시간
        .setExpiration(accessTokenexpiredDate) // 토큰 만료시간
        .signWith(SignatureAlgorithm.HS512, this.secretKey) //사용할 암호화 알고리즘, 시크릿 키
        .compact();

    String refreshToken = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now) // 토큰 생성 시간
        .setExpiration(refreshTokenexpiredDate) // 토큰 만료시간
        .signWith(SignatureAlgorithm.HS512, this.secretKey) //사용할 암호화 알고리즘, 시크릿 키
        .compact();

    TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
    tokenResponse.setEmail(email);


    return tokenResponse;
  }

  private List<String> getRolesByUserType(UserType userType) {
    List<String> roles = new ArrayList<>();
    roles.add("ROLE_USER");
    if (userType.equals(UserType.ADMIN)) {
      roles.add("ROLE_ADMIN");
    }
    return roles;
  }

  /**
   * token으로 username(사용자 email) 찾기
   */
  public String getUsername(String token) {
    return this.parseClaims(token).getSubject();
  }

  /**
   * 토큰 유효성 검사
   */
  public boolean validateToken(String token) {
    try {
      if (!StringUtils.hasText(token)) {
        return false;
      }
      Claims claims = this.parseClaims(token);
      return !claims.getExpiration().before(new Date());
    } catch (JwtException e) {
      throw new JwtException(e.getMessage());
    }
  }

  /**
   * 토큰이 유효한지 확인
   */
  private Claims parseClaims(String token) {
    try {
      return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      throw new JwtException(ErrorCode.TOKEN_TIME_OUT.getDescription());
    } catch (SignatureException e) {
      throw new JwtException(ErrorCode.JWT_TOKEN_WRONG_TYPE.getDescription());
    } catch (MalformedJwtException e) {
      throw new JwtException(ErrorCode.JWT_TOKEN_MALFORMED.getDescription());
    }
  }

  /**
   * token으로 User를 찾아서 SecurityContext에 올릴 Authentication 생성
   */
  public Authentication getAuthentication(String token) {
    String username = this.getUsername(token);
    UserDetails userDetails = authService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

}
