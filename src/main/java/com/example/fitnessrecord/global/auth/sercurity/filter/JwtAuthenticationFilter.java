package com.example.fitnessrecord.global.auth.sercurity.filter;

import com.example.fitnessrecord.global.auth.sercurity.jwt.TokenProvider;
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
  public static final String TOKEN_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = resolveTokenFromRequest(request.getHeader(TOKEN_HEADER));

    if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
      //토큰 유효성 검증 성공
      Authentication auth = tokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(auth);
    } else {
      log.info("토큰 유효성 검증 실패!!!");
    }
    filterChain.doFilter(request, response);
  }

  /**
   * 헤더로 받은 값에서 PREFIX("Bearer ") 제거
   */
  private static String resolveTokenFromRequest(String token) {
    if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
      return token.substring(TOKEN_PREFIX.length());
    }
    return null;
  }
}
