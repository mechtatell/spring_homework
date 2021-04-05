package ru.mechtatell.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mechtatell.Models.DTO.AuthRequestDTO;
import ru.mechtatell.Services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequestDTO authRequest) throws Exception {
        String token = service.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequestDTO authRequest) {
        service.saveUser(authRequest);
        return ResponseEntity.ok("OK");
    }

}
