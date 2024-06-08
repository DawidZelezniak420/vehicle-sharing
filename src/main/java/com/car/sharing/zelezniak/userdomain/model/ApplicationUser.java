package com.car.sharing.zelezniak.userdomain.model;

import com.car.sharing.zelezniak.userdomain.model.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.value_objects.UserName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class ApplicationUser implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Embedded
    private UserName userName;

    @Embedded
    private UserCredentials credentials;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "users_addresses",joinColumns =
    @JoinColumn(name = "user_id"),inverseJoinColumns =
    @JoinColumn(name = "address_id"))
    private List<Address> addresses;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns =
    @JoinColumn(name = "user_id"), inverseJoinColumns =
    @JoinColumn(name = "role_id"))
    private Set<Role> roles;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return credentials.getPassword();
    }

    public String getUsername() {
        return credentials.getEmail();
    }
}
