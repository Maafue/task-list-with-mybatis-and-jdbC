package com.example.taskList.config;


import com.example.taskList.web.security.JwtTokenFilter;
import com.example.taskList.web.security.JwtTokenProvider;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sound.sampled.Line;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ApplicationConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationContext applicationContext;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info()
                        .title("ЧУУУУУВААААК ЭТО МОЙ СВАГГЕР!!!")
                        .description("Demo Spring Boot pplication")
                        .version("1.0"));
    }
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .cors()
                .and()
                .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Unauthorized");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("Unauthorized");
                })
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .anonymous().disable()
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}

//      КОД метода SecurityFilterChain filterChain ВЗЯТЫЙ ИЗ ГИТХАБА АВТОРА КУРСА
//    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity)
//            throws Exception {
//        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .sessionManagement(sessionManagement ->
//                        sessionManagement
//                                .sessionCreationPolicy(
//                                        SessionCreationPolicy.STATELESS
//                                )
//                )
//                .exceptionHandling(configurer ->
//                        configurer.authenticationEntryPoint(
//                                        (request, response, exception) -> {
//                                            response.setStatus(
//                                                    HttpStatus.UNAUTHORIZED
//                                                            .value()
//                                            );
//                                            response.getWriter()
//                                                    .write("Unauthorized.");
//                                        })
//                                .accessDeniedHandler(
//                                        (request, response, exception) -> {
//                                            response.setStatus(
//                                                    HttpStatus.FORBIDDEN
//                                                            .value()
//                                            );
//                                            response.getWriter()
//                                                    .write("Unauthorized.");
//                                        }))
//                .authorizeHttpRequests(configurer ->
//                        configurer.requestMatchers("/api/v1/auth/**")
//                                .permitAll()
//                                .requestMatchers("/swagger-ui/**")
//                                .permitAll()
//                                .requestMatchers("/v3/api-docs/**")
//                                .permitAll()
//                                .requestMatchers("/graphiql")
//                                .permitAll()
//                                .anyRequest().authenticated())
//                .anonymous(AbstractHttpConfigurer::disable)
//                .addFilterBefore(new JwtTokenFilter(tokenProvider),
//                        UsernamePasswordAuthenticationFilter.class);
//
//        return httpSecurity.build();
//    }
