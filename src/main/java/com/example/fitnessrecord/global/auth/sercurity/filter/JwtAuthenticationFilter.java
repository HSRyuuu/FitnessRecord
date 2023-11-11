package com.example.fitnessrecord.global.auth.sercurity.filter;

import com.example.fitnessrecord.global.auth.sercurity.jwt.JwtTokenService;
import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.MyException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Authorization 헤더를 확인해서 Token으로 권한을 확인 후 SecurityContext에 권한을 넣어주는 필터
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String TOKEN_HEADER = "Authorization";

  private final JwtTokenService jwtTokenService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = jwtTokenService.resolveTokenFromRequest(request.getHeader(TOKEN_HEADER));


    if (StringUtils.hasText(token) && jwtTokenService.validateToken(token) && !jwtTokenService.isAccessTokenDenied(token)) {
      //토큰 유효성 검증 성공
      Authentication auth = jwtTokenService.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(auth);
    } else {
      log.info("토큰 유효성 검증 실패!!!");
      throw new MyException(ErrorCode.NO_AUTHORITY_ERROR);
    }
    filterChain.doFilter(request, response);
  }

}
