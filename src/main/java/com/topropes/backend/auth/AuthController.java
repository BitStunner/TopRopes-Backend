package com.topropes.backend.auth;

import com.topropes.backend.auth.dto.AuthSessionDto;
import com.topropes.backend.auth.dto.LoginRequest;
import com.topropes.backend.auth.dto.LogoutRequest;
import com.topropes.backend.auth.dto.RefreshRequest;
import com.topropes.backend.auth.dto.RegisterRequest;
import com.topropes.backend.auth.dto.TokenPairDto;
import com.topropes.backend.auth.dto.UserProfileDto;
import com.topropes.backend.auth.security.AppUserPrincipal;
import com.topropes.backend.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthSessionDto register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/username-taken")
    public java.util.Map<String, Boolean> usernameTaken(@RequestParam String username) {
        return java.util.Map.of("taken", authService.isUsernameTaken(username));
    }

    @PostMapping("/login")
    public AuthSessionDto login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public TokenPairDto refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody(required = false) LogoutRequest request) {
        authService.logout(request == null ? null : request.refreshToken());
    }

    @GetMapping("/me")
    public UserProfileDto me(@org.springframework.security.core.annotation.AuthenticationPrincipal AppUserPrincipal principal) {
        return authService.me(principal);
    }
}
