package com.vehicle.sharing.zelezniak.user_domain.controller;

import com.vehicle.sharing.zelezniak.user_domain.model.client.Client;
import com.vehicle.sharing.zelezniak.user_domain.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/")
    public Collection<Client> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public Client findById(
            @PathVariable Long id) {
        return clientService.findById(id);
    }

    @PutMapping("/update/{id}")
    public void update(
            @PathVariable Long id,
            @Validated @RequestBody Client newData) {
        clientService.update(id, newData);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }

    @GetMapping("/email/{email}")
    public Client findByEmail(@PathVariable String email){
        return clientService.findByEmail(email);
    }
}
