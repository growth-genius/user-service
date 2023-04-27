package com.sgyj.userservice.configuration;

import com.sgyj.userservice.security.Jwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfigure {

    @Bean
    public Jwt jwt ( JwtTokenConfigure jwtTokenConfigure ) {
        return new Jwt( jwtTokenConfigure.getIssuer(), jwtTokenConfigure.getClientSecret() );
    }

}