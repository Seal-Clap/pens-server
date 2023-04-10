package com.example.pens.service;

import com.example.pens.domain.CommonEntity;
import com.example.pens.domain.CommonResponse;
import com.example.pens.domain.User;
import com.example.pens.domain.UserRequest;
import com.example.pens.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity register(UserRequest request) {
        String email = request.getUserEmail();
        try {
            if (userRepository.findByUserEmail(email) != null) {

                return new ResponseEntity("duplicated email", HttpStatus.FORBIDDEN);
            }
            userRepository.save(
                    User.builder()
                            .userName(request.getUserName())
                            .userEmail(request.getUserEmail())
                            .userPassword(passwordEncoder.encode(request.getUserPassword()))
                            .build()
            );
            return new ResponseEntity("Success", HttpStatus.OK);
        } catch (Exception e) {
            throw new KeyAlreadyExistsException(); // Exception 변경해야 함
        }
    }

    @Override
    public ResponseEntity validationLogin(UserRequest userRequest) {
        try {
            String email = userRequest.getUserEmail();
            String password = userRequest.getUserPassword();
            User loginUser = userRepository.findByUserEmail(email);
            if (loginUser == null) {
                String msg = "email not found";
                return new ResponseEntity("email not found", HttpStatus.FORBIDDEN);
            }

            if (!passwordEncoder.matches(password, loginUser.getUserPassword())) {
                return new ResponseEntity("wrong password", HttpStatus.FORBIDDEN);
            }

            String msg = "login success";
            CommonResponse.CommonResponseBuilder builder = CommonResponse.builder();
            builder.message(msg);
            builder.success(true);
            CommonResponse response = builder.build();
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Error", HttpStatus.BAD_REQUEST);
        }
    }
}