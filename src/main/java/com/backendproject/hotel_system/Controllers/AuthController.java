package com.backendproject.hotel_system.Controllers;

import com.backendproject.hotel_system.Dtos.Requests.BrowseRoomRequestDto;
import com.backendproject.hotel_system.Dtos.Requests.UserRequestDto;
import com.backendproject.hotel_system.Dtos.Responses.ApiResponse;
import com.backendproject.hotel_system.Dtos.Responses.UserResponse;
import com.backendproject.hotel_system.Exceptions.PaswordMismatchError;
import com.backendproject.hotel_system.Exceptions.UserAlreadyExistsException;
import com.backendproject.hotel_system.Exceptions.UserNotFoundException;
import com.backendproject.hotel_system.Models.Token;
import com.backendproject.hotel_system.Models.User;
import com.backendproject.hotel_system.repositories.UserRepository;
import com.backendproject.hotel_system.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserResponse> home() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signup(@RequestBody UserRequestDto request) {
        try {
            User user = new User(
                    request.getFirstName(),
                    request.getUserRole(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPhone(),
                    request.getPassword(),
                    request.getAddress(),
                    request.getCity(),
                    request.getState(),
                    request.getZip()
            );
            user.setPhone(request.getPhone());
            User newUser = authService.register(user);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Success", "Successfully Registered", UserResponse.from(newUser)));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>("Bad Request", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Internal Server Error", e.getMessage(), null));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<Token>> login(@RequestBody BrowseRoomRequestDto.UserloginRequest loginRequest) {
        try {
            Token userToken = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new ApiResponse<>(HttpStatus.ACCEPTED.getReasonPhrase(), "Logged in", userToken));
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage(), null));
        } catch (PaswordMismatchError e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage(), null));
        }
    }
    @PostMapping("/signout")
      public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String authHeader){
        try{
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.toString(), "Invalid Authorization header", null));
            }

            String token = authHeader.substring(7); // remove "Bearer "

            authService.logout(token);

            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.ACCEPTED.toString(), "Logout successful","Logged out Successfully"));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Logout failed: " + e.getMessage(), null));
        }
    }


}