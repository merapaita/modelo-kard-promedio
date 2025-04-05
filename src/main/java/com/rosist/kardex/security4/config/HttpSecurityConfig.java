package com.rosist.kardex.security4.config;

import com.rosist.kardex.security4.config.filter.JwtAuthenticationFilter;
import com.rosist.kardex.security4.util.RoleEnum;
import com.rosist.kardex.security4.util.RolePermisionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider daoAutProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private AuthorizationManager<RequestAuthorizationContext> authorizationManager;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SecurityFilterChain filterChain = http
                .csrf(csrf -> csrf.disable())
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAutProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authReqConfig -> {
                    authReqConfig.anyRequest().access(authorizationManager);
//                    buildRequestMatchersPermisos(authReqConfig);
// esto lo quitamos ya que_todo lo vamos a controlar desde base de datos
                })
                .exceptionHandling( exceptionConfig -> {
                    exceptionConfig.authenticationEntryPoint(authenticationEntryPoint);
                    exceptionConfig.accessDeniedHandler(accessDeniedHandler);
                })
                .build();
        return filterChain;
    }

    private static void buildRequestMatchersMethods(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();
        authReqConfig.anyRequest().authenticated();
    }

    private static void buildRequestMatchersRoles(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        authReqConfig.requestMatchers(HttpMethod.GET, "/agrupo")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/agrupo/pageable")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/agrupo/{tipo}")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());
		authReqConfig.requestMatchers(RegexRequestMatcher.regexMatcher(HttpMethod.GET, "/products/[0-9]*"))		// esta es otra en base a patrones
//        authReqConfig.requestMatchers(HttpMethod.GET, "/agrupo/{id}")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.POST, "/agrupo")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/agrupo")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.DELETE, "/agrupo/[0-9]*")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/aclase")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/aclase/pageable")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/aclase/[B|S|O]{1}")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());
		authReqConfig.requestMatchers(RegexRequestMatcher.regexMatcher(HttpMethod.GET, "/aclase/[0-9]*"))		// esta es otra en base a patrones
//        authReqConfig.requestMatchers(HttpMethod.GET, "/aclase/{id}")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.POST, "/aclase")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/aclase")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.DELETE, "/aclase/[0+9}*")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/profile")
                .hasAnyRole(RoleEnum.ADMINISTRATOR.name(), RoleEnum.ASSISTANT_ADMINISTRATOR.name(), RoleEnum.CUSTOMER.name());

        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();
        authReqConfig.anyRequest().authenticated();
    }

    private static void buildRequestMatchersPermisos(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        authReqConfig.requestMatchers(HttpMethod.GET, "/agrupo")
                .hasAuthority(RolePermisionEnum.READ_ALL_GROUPS.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/agrupo")
                .hasAuthority(RolePermisionEnum.READ_ALL_GROUPS_PAGE.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/agrupo/{id]")
                .hasAuthority(RolePermisionEnum.READ_ONE_GROUP.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/agrupo/{tipo}")
                .hasAuthority(RolePermisionEnum.READ_ALL_GROUPS_FOR_TYPE.name());
        authReqConfig.requestMatchers(HttpMethod.POST, "/agrupo")
                .hasAuthority(RolePermisionEnum.CREATE_ONE_GROUP.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/agrupo")
                .hasAuthority(RolePermisionEnum.UPDATE_ONE_GROUP.name());
        authReqConfig.requestMatchers(HttpMethod.DELETE, "/agrupo/{id}")
                .hasAuthority(RolePermisionEnum.DELETE_ONE_GROUP.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/aclase")
                .hasAuthority(RolePermisionEnum.READ_ALL_CLASS.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/aclase")
                .hasAuthority(RolePermisionEnum.READ_ALL_CLASS_PAGE.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/aclase/{id]")
                .hasAuthority(RolePermisionEnum.READ_ONE_CLASS.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/aclase/{grupo}")
                .hasAuthority(RolePermisionEnum.READ_ALL_CLASS_FOR_GROUP.name());
        authReqConfig.requestMatchers(HttpMethod.POST, "/aclase")
                .hasAuthority(RolePermisionEnum.CREATE_ONE_CLASS.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/aclase")
                .hasAuthority(RolePermisionEnum.UPDATE_ONE_CLASS.name());
        authReqConfig.requestMatchers(HttpMethod.DELETE, "/aclase/{id}")
                .hasAuthority(RolePermisionEnum.DELETE_ONE_CLASS.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/profile")
                .hasAuthority(RolePermisionEnum.READ_MY_PROFILE.name());

        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();
        authReqConfig.anyRequest().authenticated();
    }
}
