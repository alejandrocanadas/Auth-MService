package com.example.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth.DTO.LoginRequest;
import com.example.auth.DTO.LoginResponse;
import com.example.auth.DTO.RegistroRequest;
import com.example.auth.DTO.RegistroResponse;
import com.example.auth.model.User;
import com.example.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        String token = jwtService.generarToken(user);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(token);
        response.setRole(user.getRole().name());

        return response;
    }

    public RegistroResponse register(RegistroRequest request) {

    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        throw new RuntimeException("El usuario ya existe.");
    }

    User nuevo = new User();
    nuevo.setEmail(request.getEmail());
    nuevo.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
    nuevo.setRole(request.getRole());

    userRepository.save(nuevo);

    return new RegistroResponse("Usuario registrado correctamente");
}
}