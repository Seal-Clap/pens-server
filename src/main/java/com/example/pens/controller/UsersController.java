package com.example.pens.controller;

import com.example.pens.domain.request.UserDTO;
import com.example.pens.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody UserDTO userRequest) {
        return userService.login(userRequest);
    }

    @GetMapping("/{userId}/name")
    public String getUserName(@PathVariable("userId") Integer userId) { return userService.getUserName(userId); }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO request) {
        return userService.register(request);
    }

    @GetMapping("/identity")
    public ResponseEntity identify() { return userService.identify(); }

    @GetMapping("/groups")
    public ResponseEntity getGroups(@RequestParam Integer userId) { return userService.getGroups(userId); }
}
