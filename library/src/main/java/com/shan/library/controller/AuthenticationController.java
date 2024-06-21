package com.shan.library.controller;

import com.shan.library.dto.user.EmailDTO;
import com.shan.library.dto.user.ResetPasswordDTO;
import com.shan.library.dto.user.TokensDTO;
import com.shan.library.dto.user.UserLoginDTO;
import com.shan.library.dto.user.UserRegisterDTO;
import com.shan.library.service.intf.IAuthenticationService;
import com.shan.library.service.intf.IResetPasswordService;
import com.shan.library.service.intf.IUserService;
import com.shan.library.util.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@CrossOrigin(originPatterns = "http://127.0.0.1:5173")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final IUserService userService;
    private final IAuthenticationService authenticationService;
    private final IResetPasswordService resetPasswordService;

    public AuthenticationController(IUserService userService, IAuthenticationService authenticationService,
                                    IResetPasswordService resetPasswordService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.resetPasswordService = resetPasswordService;
    }


    @PostMapping("/register")
    public TokensDTO register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        userService.create(
                userRegisterDTO.getEmail(),
                userRegisterDTO.getUsername(),
                userRegisterDTO.getPassword()
        );
        return authenticationService.login(userRegisterDTO.getEmail(), userRegisterDTO.getPassword());
    }

    @PostMapping("/login")
    public TokensDTO login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        return authenticationService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
    }

    @PostMapping("/refresh")
    public TokensDTO refresh(@RequestBody UUID refreshToken) {
        return authenticationService.refresh(refreshToken);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public void logout() {
        authenticationService.logout(SecurityUtils.getCurrentUserId());
    }

    @PostMapping("/password/reset-link")
    public void sendPasswordResetLink(@RequestBody @Valid EmailDTO emailDTO) {
        resetPasswordService.sendPasswordResetLink(emailDTO.getEmail());
    }

    @PostMapping("password/reset")
    public void resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        resetPasswordService.resetPassword(resetPasswordDTO.getToken(), resetPasswordDTO.getPassword());
    }
}
