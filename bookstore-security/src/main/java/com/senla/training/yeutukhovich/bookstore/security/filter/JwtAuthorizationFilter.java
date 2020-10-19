package com.senla.training.yeutukhovich.bookstore.security.filter;

import com.senla.training.yeutukhovich.bookstore.security.provider.JwtTokenProvider;
import com.senla.training.yeutukhovich.bookstore.security.util.SecurityConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.LoggerConstant;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Optional<String> jwt = getJwtFromRequest(servletRequest);
        jwt.ifPresent(token -> {
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    setSecurityContext(token);
                }
            } catch (JwtException e) {
                LOGGER.warn(LoggerConstant.INVALID_TOKEN.getMessage(), e.getMessage());
            }
        });
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void setSecurityContext(String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token);
        List<String> authorities = jwtTokenProvider.getAuthorities(token);
        UserDetails userDetails = new User(username, "",
                authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static Optional<String> getJwtFromRequest(ServletRequest request) {
        String bearerToken = ((HttpServletRequest) request).getHeader(SecurityConstant.HEADER_STRING.getConstant());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityConstant.TOKEN_PREFIX.getConstant())) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }
}
