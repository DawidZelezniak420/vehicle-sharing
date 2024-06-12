package com.car.sharing.zelezniak.userdomain.controller;

import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<Collection<ApplicationUser>> getAll(){
        Collection<ApplicationUser> users = userService.getAll();
        return ResponseEntity.ok()
                .body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationUser> getById(
                            @PathVariable Long id){
        ApplicationUser user = userService.getById(id);
        return ResponseEntity.ok()
                .body(user);
    }

}
