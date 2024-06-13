package com.car.sharing.zelezniak.userdomain.controller;

import com.car.sharing.zelezniak.userdomain.model.user.ApplicationUser;
import com.car.sharing.zelezniak.userdomain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public Collection<ApplicationUser> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ApplicationUser getById(
            @PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping("/update/{id}")
    public ApplicationUser update(
            @PathVariable Long id,
            @RequestBody ApplicationUser newData) {
        return userService.update(id, newData);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }
}
