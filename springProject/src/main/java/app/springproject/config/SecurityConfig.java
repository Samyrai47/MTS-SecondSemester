package app.springproject.config;

import app.springproject.filter.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  protected SecurityFilterChain configure(HttpSecurity http, JwtTokenFilter jwtTokenFilter)
      throws Exception {
    http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(req -> req.anyRequest().permitAll());
    http.csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }
}
