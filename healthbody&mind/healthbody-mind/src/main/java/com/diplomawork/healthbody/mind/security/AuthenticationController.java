package com.diplomawork.healthbody.mind.security;

import com.diplomawork.healthbody.mind.security.model.AuthenticationRequest;
import com.diplomawork.healthbody.mind.security.model.AuthenticationResponse;
import com.diplomawork.healthbody.mind.security.model.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/logIn")
    private ResponseEntity<AuthenticationResponse> logInForm(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        String token = authenticationService.userLogIn(authenticationRequest);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
    @PostMapping("/register")
    private ResponseEntity<AuthenticationResponse> registerForm(@Valid @RequestBody RegisterRequest registerRequest) {
        String token = authenticationService.userRegistration(registerRequest);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
