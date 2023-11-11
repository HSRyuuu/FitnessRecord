package com.example.fitnessrecord.global.auth.sercurity.jwt;

import com.example.fitnessrecord.domain.user.type.UserType;
import com.example.fitnessrecord.global.auth.dto.TokenResponse;
import com.example.fitnessrecord.global.auth.service.AuthService;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import com.example.fitnessrecord.global.redis.repository.RedisTokenRepository;
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
public class JwtTokenService {

  private final AuthService authService;
  private final RedisTokenRepository redisTokenRepository;

  @Value("${spring.jwt.secret}")
  private String secretKey;

  public static final String TOKEN_PREFIX = "Bearer ";
  private static final String KEY_ROLES = "roles";
  private static final long ACCESS_TOKEN_EXPIRE_TIME =
      (long) 1000 * 60 * 60 * 24;// 24시간 (실제로는 30분정도로 수정해야함)
  private static final long REFRESH_TOKEN_EXPIRE_TIME = (long) 1000 * 60 * 60 * 24 * 30;// 한 달


  /**
   * 토큰 생성 (발급)
   */
  public TokenResponse generateTokenResponse(String email, UserType userType) {
    List<String> roles = getRolesByUserType(userType);

    Claims claims = Jwts.claims().setSubject(email);
    claims.put(KEY_ROLES, roles);

    String accessToken = generateToken(claims, ACCESS_TOKEN_EXPIRE_TIME);

    String refreshToken = generateToken(claims, REFRESH_TOKEN_EXPIRE_TIME);

    //redisTokenRepository(tokenMap)에 (email, refreshToken) k-v쌍을 넣는다.
    redisTokenRepository.addRefreshToken(email, refreshToken);

    TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
    tokenResponse.setEmail(email);

    return tokenResponse;
  }

  /**
   * UserType으로 List roles를 만들어준다.
   */
  private List<String> getRolesByUserType(UserType userType) {
    List<String> roles = new ArrayList<>();
    roles.add("ROLE_USER");
    if (userType.equals(UserType.ADMIN)) {
      roles.add("ROLE_ADMIN");
    }
    return roles;
  }

  /**
   * refreshToken을 확인한 뒤 accessToken을 재발행한다.
   */
  public TokenResponse regenerateAccessToken(String refreshToken) {
    if (!this.validateToken(refreshToken)) {
      throw new MyException(ErrorCode.TOKEN_TIME_OUT);
    }
    Claims claims = parseClaims(refreshToken);

    String email = claims.getSubject();

    String findToken = redisTokenRepository.getRefreshToken(email);
    if (!refreshToken.equals(findToken)) {
      throw new MyException(ErrorCode.JWT_REFRESH_TOKEN_NOT_FOUND);
    }

    String accessToken = generateToken(claims, ACCESS_TOKEN_EXPIRE_TIME);

    //refreshToken은 재발급하지 않는다.
    TokenResponse tokenResponse = TokenResponse.builder()
        .email(email)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

    return tokenResponse;
  }


  /**
   * Claims와 expiredTime으로 JWT Token을 생성한다.
   */
  private String generateToken(Claims claims, Long expiredTime) {
    Date now = new Date();
    Date expired = new Date(now.getTime() + expiredTime);
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now) // 토큰 생성 시간
        .setExpiration(expired) // 토큰 만료시간
        .signWith(SignatureAlgorithm.HS512, this.secretKey) //사용할 암호화 알고리즘, 시크릿 키
        .compact();
  }

  /**
   * 토큰 유효성 검사
   * - text 존재 여부, parseClaims 예외 찾음, expiredDate 조회
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
   * token으로 User를 찾아서 SecurityContext에 올릴 Authentication 생성
   */
  public Authentication getAuthentication(String token) {
    String username = this.getUsername(token);
    UserDetails userDetails = authService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  /**
   * access token이 redis denied map에 포함되었는지 확인
   */
  public boolean isAccessTokenDenied(String accessToken){
    return redisTokenRepository.existsBlackListAccessToken(accessToken);
  }

  /**
   * 헤더로 받은 값에서 PREFIX("Bearer ") 제거
   */
  public String resolveTokenFromRequest(String token) {
    if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
      return token.substring(TOKEN_PREFIX.length());
    }
    return null;
  }

  /**
   * token으로 username(사용자 email) 찾기
   */
  private String getUsername(String token) {
    return this.parseClaims(token).getSubject();
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

}
