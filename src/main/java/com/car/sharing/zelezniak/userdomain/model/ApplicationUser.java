package com.car.sharing.zelezniak.userdomain.model;

import com.car.sharing.zelezniak.userdomain.model.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.value_objects.UserName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Embedded
    private UserName name;

    @Embedded
    private UserCredentials credentials;

    private LocalDateTime createdAt;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns =
    @JoinColumn(name = "user_id"), inverseJoinColumns =
    @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public String getEmail(){
        return credentials.getEmail();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return credentials.getPassword();
    }

    public String getUsername() {
        return credentials.getEmail();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationUser that = (ApplicationUser) o;
        return  Objects.equals(name, that.name)
                && Objects.equals(credentials, that.credentials)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, credentials, createdAt, address);
    }


    @Override
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
