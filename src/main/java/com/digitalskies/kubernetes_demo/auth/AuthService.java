package com.digitalskies.kubernetes_demo.auth;


import com.digitalskies.kubernetes_demo.auth.dto.TokenPair;
import com.digitalskies.kubernetes_demo.auth.dto.User;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UserRepository userRepository;

    private HashEncoder hashEncoder;

    private JWTService jwtService;


    public AuthService(UserRepository userRepository, HashEncoder hashEncoder, JWTService jwtService) {
        this.userRepository = userRepository;
        this.hashEncoder = hashEncoder;
        this.jwtService = jwtService;
    }

    TokenPair login(String email, String password) {

        var userOptional=userRepository.findByEmail(email).stream().findFirst();

        if(userOptional.isEmpty()){

            return null;
        }

        var user= userOptional.get();

        if(!hashEncoder.compare(password,user.getHashedPassword())){
            return null;
        }

        String accessToken = jwtService.generateAccessToken(email);

        String refreshToken = jwtService.generateRefreshToken(email);


        return new TokenPair(accessToken,refreshToken);

    }

    @Transactional
    void register(User user){

        var userOptional=userRepository.findByEmail(user.getEmail()).stream().findFirst();

        if(userOptional.isPresent()){

            throw new IllegalArgumentException("Email already registered");
        }

        var hashedPassword = hashEncoder.encode(user.getHashedPassword());
        user.setHashedPassword(hashedPassword);

        userRepository.save(user);
    }

}
