package com.bizzman.config;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    public static final String ADMIN = "ADMINISTRATOR";
    public static final String USER = "USER";

    private List<UserDetails> details;


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
        String rootPath = System.getProperty("user.dir");
        String path = rootPath + "/src/main/java/com/bizzman/config/resources/users.json";

        details = new ArrayList<>();
        readFromJSON(path);

        return new InMemoryUserDetailsManager(details);
    }

    private void readFromJSON(String path){
        List<UserDetails> details = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        JSONArray users;

        try {

            FileReader reader = new FileReader(path);
            Object obj = jsonParser.parse(reader);

            users = (JSONArray) obj;
            System.out.println(users);
            users.forEach( usr -> parseUser( (JSONObject) usr ) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private void parseUser(JSONObject user){
        // Make sure to have all these fields in the json file
        JSONObject userObject = (JSONObject) user.get("user");

        String username = (String) userObject.get("username");
        String password = (String) userObject.get("password");
        String role = (String) userObject.get("role");

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        details.add(User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles(role)
                .build());
    }


}
