package com.bizzman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    public static final String ADMIN = "ADMINISTRATOR";
    public static final String USER = "USER";

    private static final RequestMatcher[] NO_AUTH = {new AntPathRequestMatcher("/webjars/**", "GET"),
            new AntPathRequestMatcher("/**", "GET")};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // By default, all requests are authenticated except our specific list
        http.authorizeRequests().requestMatchers(NO_AUTH).permitAll().anyRequest().hasAnyRole(ADMIN, USER);
        // use form login-logout for the web.
        http.formLogin().loginPage("/sign-in").permitAll();
        http.logout().logoutUrl("/sign-out").logoutSuccessUrl("/").permitAll();
        // Use CSRF for any request
        http.antMatcher("/**").csrf();
        // Disable X-Frame-Options for the H2 console
        http.headers().frameOptions().disable();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("administrator"))
                .roles(ADMIN)
                .build();
        UserDetails employee1 = User.withUsername("employee1")
                .password(passwordEncoder.encode("employee"))
                .roles(USER).build();
        UserDetails employee2 = User.withUsername("employee2")
                .password(passwordEncoder.encode("employee"))
                .roles(USER).build();

        return new InMemoryUserDetailsManager(admin, employee1, employee2);
    }


}
