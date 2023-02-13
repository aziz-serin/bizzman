package com.bizzman.controllers;

import com.bizzman.dao.UserService;
import com.bizzman.entities.user.ERole;
import com.bizzman.entities.user.Role;
import com.bizzman.entities.user.User;
import com.bizzman.security.jwt.JwtGenerator;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping(path = "rest/authentication",  produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private UserService userService;

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

    @PostMapping("/introspect")
    public ResponseEntity<?> introspect(@RequestHeader @NotNull String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            return ResponseEntity.ok().body(jwt.getJWTClaimsSet());

        } catch (ParseException e) {
            logger.error("Could not parse the given jwt token: {}", token);
           return ResponseEntity.badRequest().body("Invalid token format, could not parse!");
        }
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> register(@RequestBody @NotNull Map<?,?> body) {
        String role = (String) body.get("role");
        String username = (String) body.get("username");
        String password = (String) body.get("password");

        if (role == null || username == null || password == null) {
            logger.error("Could not parse the request body!");
            return ResponseEntity.badRequest().body("Could not parse the request body!");
        }

        Role user_role;
        switch (role) {
            case "admin":
                user_role = new Role(ERole.ROLE_ADMIN);
                break;
            case "user":
                user_role = new Role(ERole.ROLE_USER);
                break;
            default:
                return ResponseEntity.badRequest().body("Failed to create the user!");
        }
        try {
            User user = userService.create(username, password, user_role);
            return ResponseEntity.ok("User created with id " + user.getId());
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Failed to create the user!");
        }
    }

}
