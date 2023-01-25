package com.bizzman.controllers;

import com.bizzman.security.jwt.JwtGenerator;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@RequestMapping(path = "rest/authentication",  produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtGenerator jwtGenerator;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/validate")
    public ResponseEntity<?> validate() {
        return ResponseEntity.ok().body("Fuuuuck");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestHeader @NotNull String username, @RequestHeader @NotNull String password)
            throws JOSEException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtGenerator.generateToken(authentication);
        return ResponseEntity.ok(Map.of("token", jwt));
    }
}
