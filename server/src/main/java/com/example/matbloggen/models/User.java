package com.example.matbloggen.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class User {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String authority;

    public Collection<GrantedAuthority> getAuthorities(){
        GrantedAuthority grantedAuthority = this::getAuthority;
        return Collections.singleton(grantedAuthority);
    }
}