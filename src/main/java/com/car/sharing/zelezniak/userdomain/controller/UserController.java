package com.car.sharing.zelezniak.userdomain.controller;

import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<Collection<ApplicationUser>> getAll() {
        Collection<ApplicationUser> users = userService.getAll();
        return ResponseEntity.ok()
                .body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationUser> getById(
            @PathVariable Long id) {
        ApplicationUser user = userService.getById(id);
        return ResponseEntity.ok()
                .body(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApplicationUser> update(
            @PathVariable Long id,
            @RequestBody ApplicationUser newData) {
        ApplicationUser user = userService.update(id, newData);
        return ResponseEntity.ok()
                .body(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
