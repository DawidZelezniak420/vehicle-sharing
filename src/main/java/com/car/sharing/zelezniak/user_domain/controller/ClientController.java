package com.car.sharing.zelezniak.user_domain.controller;

import com.car.sharing.zelezniak.user_domain.model.user.Client;
import com.car.sharing.zelezniak.user_domain.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService userService;

    @GetMapping("/")
    public Collection<Client> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Client getById(
            @PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping("/update/{id}")
    public Client update(
            @PathVariable Long id,
            @RequestBody Client newData) {
        return userService.update(id, newData);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }
}
