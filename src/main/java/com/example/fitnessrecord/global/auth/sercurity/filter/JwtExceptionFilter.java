package com.example.fitnessrecord.global.auth.sercurity.filter;

import com.example.fitnessrecord.global.exception.ErrorCode;
import com.example.fitnessrecord.global.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT Exception을 잡아서 Handling 하는 filter
 * setResponse로 직접 응답을 만든다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }catch (JwtException e) {
            String message = e.getMessage();
            if (message.equals(ErrorCode.JWT_TOKEN_WRONG_TYPE.getDescription())) {
                setResponse(response, ErrorCode.JWT_TOKEN_WRONG_TYPE);
            } else if (message.equals(ErrorCode.TOKEN_TIME_OUT.getDescription())) {
                setResponse(response, ErrorCode.TOKEN_TIME_OUT);
            }
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws RuntimeException, IOException {
        ObjectMapper objectMapper = new JsonMapper();
        String responseJson = objectMapper.writeValueAsString(new ErrorResponse(errorCode));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8");
        response.setStatus(errorCode.getStatusCode());
        response.getWriter().print(responseJson);
    }

}
