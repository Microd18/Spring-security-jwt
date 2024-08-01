package com.example.springsecurityjwt.security.filter;

import com.example.springsecurityjwt.security.TokenAuthentication;
import com.example.springsecurityjwt.security.TokenProvider;
import com.example.springsecurityjwt.security.TokenUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final TokenProvider tokenProvider;
    private final TokenUtils tokenUtils;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && tokenProvider.validateAccessToken(token)) {
            final Claims claims = tokenProvider.getAccessClaims(token);
            final TokenAuthentication jwtInfoToken = tokenUtils.generate(claims);
            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }
        chain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER)) {
            return bearer.substring(BEARER.length());
        }
        return null;
    }
}
