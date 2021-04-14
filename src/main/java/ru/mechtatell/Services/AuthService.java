package ru.mechtatell.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mechtatell.DAO.Repos.UserRepository;
import ru.mechtatell.Models.DTO.AuthRequestDTO;
import ru.mechtatell.Models.User;
import ru.mechtatell.Util.JWTUtil;

@Service
public class AuthService {
    private final UserRepository repository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthService(UserRepository repository, JWTUtil jwtUtil, PasswordEncoder encoder) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    public void saveUser(AuthRequestDTO authRequest) {
        User user = new User();
        user.setLogin(authRequest.getLogin());
        user.setPassword(encoder.encode(authRequest.getPassword()));
        repository.save(user);
    }

    public String login(AuthRequestDTO authRequest) throws Exception {
        User user = repository.findByLogin(authRequest.getLogin());

        if (user == null) {
            throw new Exception("Check your login");
        }

        if (!encoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new Exception("Check your password");
        }

        return jwtUtil.generateToken(user.getLogin());
    }
}
