package com.example.auth0.configuration;

import com.auth0.AuthenticationController;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Configuration
    @EnableWebSecurity
    public class AuthConfig extends WebSecurityConfigurerAdapter {
        @Value(value = "${com.auth0.domain}")
        private String domain;

        @Value(value = "${com.auth0.clientId}")
        private String clientId;

        @Value(value = "${com.auth0.clientSecret}")
        private String clientSecret;

    @Value(value = "${com.auth0.managementApi.clientId}")
    private String managementApiClientId;

    @Value(value = "${com.auth0.managementApi.clientSecret}")
    private String managementApiClientSecret;

    @Value(value = "${com.auth0.managementApi.grantType}")
    private String grantType;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http
                    .authorizeRequests()
                    .antMatchers("/callback", "/login", "/","/home").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .and()
                    .logout().permitAll();
            //logoutSuccessHandler(logoutSuccessHandler())
        }

    @Bean
    public AuthenticationController authenticationController() throws UnsupportedEncodingException {
        JwkProvider jwkProvider = new JwkProviderBuilder(domain).build();
        return AuthenticationController.newBuilder(domain, clientId, clientSecret)
                .withJwkProvider(jwkProvider)
                .build();
    }

    public String getDomain() {
        return domain;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getManagementApiClientId() {
        return managementApiClientId;
    }

    public String getManagementApiClientSecret() {
        return managementApiClientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getUserInfoUrl() {
        return "https://" + getDomain() + "/userinfo";
    }

    public String getUsersUrl() {
        return "https://" + getDomain() + "/api/v2/users";
    }

    public String getUsersByEmailUrl() {
        return "https://" + getDomain() + "/api/v2/users-by-email?email=";
    }

    public String getLogoutUrl() {
        return "https://" + getDomain() +"/v2/logout";
    }

    public String getContextPath(HttpServletRequest request) {
        String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return path;
    }
    }
