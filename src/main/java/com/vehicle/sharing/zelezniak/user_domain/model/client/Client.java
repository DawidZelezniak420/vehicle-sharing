package com.vehicle.sharing.zelezniak.user_domain.model.client;

import com.vehicle.sharing.zelezniak.user_domain.model.client.user_value_objects.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
@Builder
public class Client implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Embedded
    private UserName name;

    @Embedded
    private UserCredentials credentials;

    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE})
    @JoinTable(name = "clients_roles", joinColumns =
    @JoinColumn(name = "user_id"), inverseJoinColumns =
    @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public String getEmail() {
        return credentials.getEmail();
    }

    public String getPassword() {
        return credentials.getPassword();
    }

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getUsername() {
        return credentials.getEmail();
    }

    public void addRole(Role roleUser) {
        if (roles == null)
            roles = new HashSet<>();
        roles.add(roleUser);
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Client client = (Client) object;
        return Objects.equals(id, client.id)
                && Objects.equals(name, client.name)
                && Objects.equals(credentials, client.credentials)
                && Objects.equals(createdAt, client.createdAt)
                && Objects.equals(address, client.address);
    }

    public int hashCode() {
        return Objects.hash(id,
                name, credentials,
                createdAt, address);
    }

    public String toString() {
        return "ApplicationUser{" +
                "address=" + address +
                ", createdAt=" + createdAt +
                ", credentials=" + credentials +
                ", name=" + name +
                ", id=" + id +
                '}';
    }
}
