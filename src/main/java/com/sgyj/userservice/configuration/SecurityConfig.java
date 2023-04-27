package com.sgyj.userservice.configuration;

import com.sgyj.userservice.properties.AppProperties;
import com.sgyj.userservice.security.EntryPointUnauthorizedHandler;
import com.sgyj.userservice.security.Jwt;
import com.sgyj.userservice.security.JwtAccessDeniedHandler;
import com.sgyj.userservice.security.JwtAuthenticationProvider;
import com.sgyj.userservice.security.JwtAuthenticationTokenFilter;
import com.sgyj.userservice.service.AccountService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Jwt jwt;

    private final JwtTokenConfigure jwtTokenConfigure;

    private final JwtAccessDeniedHandler accessDeniedHandler;

    private final EntryPointUnauthorizedHandler unauthorizedHandler;

    private final AppProperties appProperties;

    @Bean
    public PasswordEncoder passwordEncoder () {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter () {
        return new JwtAuthenticationTokenFilter( jwtTokenConfigure.getHeader(), jwt );
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider ( AccountService accountService ) {
        return new JwtAuthenticationProvider( accountService );
    }

    @Bean
    public SecurityFilterChain filterChain ( HttpSecurity http ) throws Exception {
        http
            .httpBasic()
                    .disable()                                              // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .csrf()
                    .disable()                                              // rest api 이므로 csrf 보안이 필요없으므로 disable 처리.
                .exceptionHandling()
                .accessDeniedHandler( accessDeniedHandler )
                .authenticationEntryPoint( unauthorizedHandler )
            .and()
                .headers()
                .frameOptions()
                .sameOrigin()
            .and()
                .sessionManagement()
                .sessionCreationPolicy( SessionCreationPolicy.STATELESS ) // jwt token 으로 인증하므로 세션은 필요없으므로 생성안함.
            .and()
                .cors()
                    .configurationSource( corsConfigurationSource())
            .and()
                .authorizeHttpRequests()
                .requestMatchers( "/*",
                          "/user/oauth2/callback/**",
                          "/h2-console/**",
                          "/account/refresh-token/**",
                          "/account/sign-up"
                )
                    .permitAll()
                .requestMatchers( HttpMethod.GET, "/account/profile/*" )
                    .permitAll()
                .requestMatchers( "/user/**" )
                    .hasRole( "USER" )
                .requestMatchers( "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**" )
                    .permitAll()
            .and()
                .addFilterBefore( jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource () {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins( appProperties.getHosts() );
        config.setAllowedMethods( Arrays.stream(HttpMethod.values()).map( HttpMethod::name ).toList() );
        config.setExposedHeaders( List.of( "Access-Control-Allow-Headers", "Access-Control-Allow-Origin", "strict-origin-when-cross-origin" ) );
        config.setAllowedHeaders( List.of( "*" ) );
        config.setAllowCredentials( true );
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", config );
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager ( AuthenticationConfiguration authenticationConfiguration ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
