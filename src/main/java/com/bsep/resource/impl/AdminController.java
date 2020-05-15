package com.bsep.resource.impl;

import com.bsep.domain.Admin;
import com.bsep.domain.AdminTokenState;
import com.bsep.repository.AdminRepository;
import com.bsep.security.TokenUtils;
import com.bsep.security.auth.JwtAuthenticationRequest;
import com.bsep.security.jwt.JwtAuthenticationFilter;
import com.bsep.service.impl.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


@RestController
@CrossOrigin(origins="http://localhost:3000")
public class AdminController {

    @Autowired
    private LoginService loginService;

    private AdminRepository adminRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminTokenState> loginUser(@RequestBody JwtAuthenticationRequest authenticationRequest,
                                                     HttpServletResponse response) throws AuthenticationException, IOException {
        Admin log = loginService.loginUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
      //  System.out.println(passwordEncoder.encode(authenticationRequest.getPassword()));
       //$2a$10$mwePsuQdjt3W2t15GFKIAOYAYzA454sUG6qVaBckFtQmbhBJiOFxS

        if (log != null) {
            final Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));

            // Ubaci username + password u kontext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Kreiraj token
            Admin user = (Admin) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user.getUsername());
            int expiresIn = tokenUtils.getExpiredIn();

            return ResponseEntity.ok(new AdminTokenState(jwt, expiresIn));
        } else {
            return new ResponseEntity<AdminTokenState>(HttpStatus.NOT_FOUND);
        }

    }
}