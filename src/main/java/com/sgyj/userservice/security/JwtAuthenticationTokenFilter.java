package com.sgyj.userservice.security;

import static com.sgyj.userservice.utils.CommonUtil.authorities;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
public class JwtAuthenticationTokenFilter extends GenericFilterBean {

    private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

    private final String headerKey;

    private final Jwt jwt;

    public JwtAuthenticationTokenFilter ( String header, Jwt jwt ) {
        this.headerKey = header;
        this.jwt = jwt;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String authorizationToken = obtainAuthorizationToken(request);
        if (authorizationToken != null) {
            try {
                Jwt.Claims claims = verify(authorizationToken);
                Long id = claims.id;
                String accountId = claims.accountId;
                String email = claims.email;

                if(nonNull(id) && isNotEmpty(email) ) {
                    JwtAuthenticationToken authentication =
                            new JwtAuthenticationToken(new JwtAuthentication(id, accountId, email),
                                    null,
                                    authorities( claims )
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception e) {
                log.error("Jwt processing failed: {}", e.getMessage());
            }
        }
        chain.doFilter(request, response);
    }

    private String obtainAuthorizationToken(HttpServletRequest request) {
        String token = request.getHeader(headerKey);
        if (token != null) {
            if (log.isDebugEnabled())
                log.error("Jwt authorization api detected: {}", token);
            token = URLDecoder.decode(token, StandardCharsets.UTF_8 );
            String[] parts = token.split(" ");
            if (parts.length == 2) {
                String scheme = parts[0];
                String credentials = parts[1];
                return BEARER.matcher(scheme).matches() ? credentials : null;
            }
        }

        return null;
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }

}