package com.tsystems.banking.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.tsystems.banking.security.filters.JwtAuthFilter;
import com.tsystems.banking.security.filters.JwtVerificationFilter;
import com.tsystems.banking.services.jwt.JwtService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  private final UserDetailsService userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final JwtService jwtService;
  private final AppConfig appConfig;

  /**
   * @param userDetailsService
   * @param bCryptPasswordEncoder
   * @param jwtService
   * @param appConfig
   * @param appConfig
   */
  public SecurityConfiguration(
    UserDetailsService userDetailsService,
    BCryptPasswordEncoder bCryptPasswordEncoder,
    JwtService jwtService,
    AppConfig appConfig
  ) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.jwtService = jwtService;
    this.appConfig = appConfig;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
      .userDetailsService(userDetailsService)
      .passwordEncoder(bCryptPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf()
      .disable()
      .sessionManagement()
      .sessionCreationPolicy(STATELESS)
      .and()
      .authorizeRequests()
      .antMatchers(GET, "/api/health")
      .permitAll()
      .antMatchers(POST, "/api/auth/**")
      .permitAll()
      .antMatchers("/api/**")
      .authenticated()
      .anyRequest()
      .permitAll()
      .and()
      .addFilter(
        new JwtAuthFilter(super.authenticationManager(), jwtService, appConfig)
      )
      .addFilterBefore(
        new JwtVerificationFilter(jwtService),
        UsernamePasswordAuthenticationFilter.class
      );
  }
}
