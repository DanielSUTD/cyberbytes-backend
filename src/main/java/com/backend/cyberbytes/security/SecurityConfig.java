package com.backend.cyberbytes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Dependenciaas
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOriginPatterns(List.of("*"));// Permite requisições de qualquer origem, incluindo null(Obs: Posso mudar para um endereço específico)
                    config.setAllowedHeaders(List.of("*")); // Permitir todos os cabeçalhos
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Métodos permitidos
                    return config;
                }))
                //Configura os endpoints
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize


                        // Configurações do RestController /user
                        .requestMatchers(HttpMethod.GET, "/user/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/user/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/user/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/user/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()

                        // Configurações do RestController /pagina
                        //Observação: O correto é que apenas usuários com cargo específico façam a requisição post
                        .requestMatchers(HttpMethod.POST, "/page").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/page/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/page").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/page").hasRole("ADMIN")


                        //Configurações do RestController /auth
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        //Configurações do RestController /ia
                        .requestMatchers(HttpMethod.POST, "/ia/chat").permitAll()
                        .requestMatchers(HttpMethod.POST, "/ia/page").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /*
     * Gerenciador de autenticação
     * */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    /*
     * Função para retorna um objeto para criptografar senhas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
