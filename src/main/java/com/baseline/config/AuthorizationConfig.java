package com.baseline.config;

import com.baseline.common.exception.InternalServerException;
import com.baseline.auth.AuthService;
import com.baseline.auth.AuthUser;
import com.baseline.common.errorcode.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * TODO Authorization Server가 다른 프로젝트로 분리되어 @EnableAuthorizationServer가 분리됨
 * 여기에선 개발 시간 관계상 이전 버전을 일단 사용함. 추후 분리 필요
 */
@SuppressWarnings("deprecation")
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
    @Value("${cms.jwt-secret}")
    private String jwtSecret;
    private final AuthService authService;

    private final AccessTokenConverter customAccessTokenConverter = new CustomAccessTokenConverter();

    private final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

    @PostConstruct
    public void init() {
        try {
            jwtAccessTokenConverter.setSigningKey(jwtSecret);
            jwtAccessTokenConverter.setAccessTokenConverter(customAccessTokenConverter);
            jwtAccessTokenConverter.afterPropertiesSet();
        } catch (Exception e) {
            log.error("Could not initiate AuthorizationConfig.", e);
            throw new InternalServerException(CommonErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    private static class CustomAccessTokenConverter extends DefaultAccessTokenConverter {
        @Override
        public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
            AuthUser user = (AuthUser)authentication.getUserAuthentication().getPrincipal();

            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) super.convertAccessToken(token, authentication);

            map.put("id", user.getId());
            return map;
        }
    }

    private final AuthenticationManager authenticationManager = new AuthenticationManager() {
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            UsernamePasswordAuthenticationToken input = (UsernamePasswordAuthenticationToken)authentication;

            try {
                AuthUser user = authService.login(input.getName(), input.getCredentials().toString());
                return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            } catch (Exception e) {
                log.error("Failed to authenticate.", e);
                throw new InternalServerException(CommonErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        }
    };

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .addTokenEndpointAuthenticationFilter(new CorsFilter(corsConfigurationSource()));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("cms")
                .secret("secret")
                .scopes("read", "write")
                .authorizedGrantTypes("client_credentials", "password", "authorization_code", "refresh_token")
                .accessTokenValiditySeconds(18000);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.userDetailsService(authService)
                .accessTokenConverter(customAccessTokenConverter)
                .tokenStore(new JwtTokenStore(jwtAccessTokenConverter))
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS)
                .tokenEnhancer(jwtAccessTokenConverter);
    }
}
