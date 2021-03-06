package com.github.drsgdev.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTProvider provider;
    private final UserDetailsServiceImpl userDetails;
    private final RefreshTokenService refreshService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String jwt = getJWTFromRequest(request);

        boolean jwtIsValid = true;
        try {
            provider.validateJWT(jwt);
        } catch (JwtException | IllegalArgumentException ex) {
            jwtIsValid = false;
        }

        if (StringUtils.hasText(jwt) && jwtIsValid
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = provider.getUsernameFromJWT(jwt);

            UserDetails user = userDetails.loadUserByUsername(username);

            if (user.isEnabled() && user.isAccountNonLocked() && refreshService.validateToken(username)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user.getUsername(), null,
                                user.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        return header;
    }
}
