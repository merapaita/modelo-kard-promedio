//package com.rosist.kardex.security1;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.rosist.kardex.security1.jwt.JwtEntryPoint;
//import com.rosist.kardex.security1.jwt.JwtTokenFilter;
//import com.rosist.kardex.security1.service.UsuarioDetailsServiceImpl;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class MainSecurity {
//
//	@Autowired
//	UsuarioDetailsServiceImpl userDetailsServiceImpl;
//
//    @Autowired
//    JwtEntryPoint jwtEntryPoint;
//
//	@Autowired
//	PasswordEncoder passwordEncoder;
//
//    @Autowired
//    JwtTokenFilter jwtTokenFilter;
//
//	AuthenticationManager authenticationManager;
//
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
//		builder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
//		authenticationManager = builder.build();
//		http.authenticationManager(authenticationManager);
//
//		http.csrf(csrf -> csrf.disable());
//		http.cors(cors -> cors.disable());
////        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//		http.authorizeHttpRequests(
//				c -> c.requestMatchers(
//						"/auth/**", "/email-password/**", "/v2/api-docs/**",
//						"/swagger-ui/**", "/swagger-resources/**", "/configuration/**").permitAll()
////				.anyRequest().authenticated()
//
//		);
//        http.exceptionHandling(e -> e.authenticationEntryPoint(jwtEntryPoint));
//        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
//		return http.build();
//	}
//
//}
