package ru.mechtatell.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mechtatell.Repos.UserRepository;
import ru.mechtatell.Models.User;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = repository.findByLogin(s);
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), new ArrayList<>());
    }
}
