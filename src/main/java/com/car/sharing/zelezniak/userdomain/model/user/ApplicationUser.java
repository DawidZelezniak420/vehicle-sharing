package com.car.sharing.zelezniak.userdomain.model.user;

import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserCredentials;
import com.car.sharing.zelezniak.userdomain.model.user.value_objects.UserName;
import com.car.sharing.zelezniak.utils.TimeFormatter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns =
    @JoinColumn(name = "user_id"), inverseJoinColumns =
    @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public String getEmail() {
        return credentials.getEmail();
    }

    public String getPassword() {
        return credentials.getPassword();
    }

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

    public void setCreationDate() {
        this.createdAt = TimeFormatter
                .getFormattedActualDateTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationUser that = (ApplicationUser) o;
        return Objects.equals(name, that.name)
                && Objects.equals(credentials, that.credentials)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(address, that.address);
    }

    public int hashCode() {
        return Objects.hash(name, credentials
                , createdAt, address);
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
