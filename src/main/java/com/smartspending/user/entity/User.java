package com.smartspending.user.entity;

import com.smartspending.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Builder
    public User(String email, String password, String name, Boolean emailVerified) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.emailVerified = emailVerified;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
