package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Exceptions.PaswordMismatchError;
import com.backendproject.hotel_system.Exceptions.UnAuthorizesAccess;
import com.backendproject.hotel_system.Exceptions.UserAlreadyExistsException;
import com.backendproject.hotel_system.Exceptions.UserNotFoundException;
import com.backendproject.hotel_system.Models.Token;
import com.backendproject.hotel_system.Models.User;
import com.backendproject.hotel_system.repositories.TokenRepository;
import com.backendproject.hotel_system.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final TokenRepository tokenRepository;
    private final RolewisePermissions rolePermissionService;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    @Autowired
    public AuthServiceImpl(
            RolewisePermissions rolePermissionService,
            UserRepository userRepository,
            TokenRepository tokenRepository,
            PasswordEncoder encoder
    ) {
        this.rolePermissionService = rolePermissionService;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token login(String email, String password) {
        Optional<User> useOp = userRepository.findByEmail(email);
        if (useOp.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        if (!encoder.matches(password, useOp.get().getPassword())) {
            throw new PaswordMismatchError("Wrong password");
        }

        User user = useOp.get();
        Token token = generateToken(user);
        return tokenRepository.save(token);
    }

    @Override
    public void logout(String tokenString) {
        Optional<Token> tokenOp = tokenRepository.findByToken(tokenString);
        if (tokenOp.isPresent()) {
            Token token = tokenOp.get();
            token.setHasExpired(true);
            token.setExpires(new Date());
            tokenRepository.save(token);
        }
    }

    @Override
    public User register(User user) {
        Optional<User> userOp = userRepository.findByEmail(user.getEmail());
        if (userOp.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User newUser = new User();
        String encodedPassword = encoder.encode(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPhone(user.getPhone());
        newUser.setAddress(user.getAddress());
        newUser.setCity(user.getCity());
        newUser.setState(user.getState());
        newUser.setZip(user.getZip());
        newUser.setUserRole(user.getUserRole());
        newUser.setCreatedAt(new Date());
        newUser.setUpdatedAt(new Date());

        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public void forgotPassword(String email) {
        return;
    }

    public User validateToken(Token token) {

        Optional<Token> tokenOp = tokenRepository.findByTokenAndHasExpiredFalse(token.getToken());
        if (tokenOp.isEmpty()) {
            throw new UnAuthorizesAccess("Token is invalid or has expired");
        }
        var claims = Jwts.parser()
                .setSigningKey(JWT_SECRET.getBytes())
                .parseClaimsJws(token.getToken())
                .getBody();
        String userId = claims.getSubject();
        Optional<User> userOp = userRepository.findById(Long.parseLong(userId));
        if (userOp.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return userOp.get();
    }

    public Token generateToken(User user) {
        Date tokenExpiryDate = new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30));
        Set<String> permissions = rolePermissionService.getPermissionsForRole(user.getUserRole());

        String tokenString = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(tokenExpiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET.getBytes())
                .compact();

        Token token = new Token();
        token.setToken(tokenString);
        token.setCreatedBy(user.getFirstName() + " " + user.getLastName());
        token.setUserid(user.getId());
        token.setExpires(tokenExpiryDate);
        token.setHasExpired(false);
        return token;
    }

    public Token retrieveToken(long userid) {
        Optional<Token> tokenop = tokenRepository.findTopByUseridAndHasExpiredFalseOrderByCreatedAtDesc(userid);
        return tokenop.orElse(null);
    }
}
