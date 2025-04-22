package com.smartspending.user.entity;

import com.smartspending.category.entity.Category;
import com.smartspending.common.entity.BaseEntity;
import com.smartspending.transaction.entity.Transaction;
import com.smartspending.user.enums.Provider;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider;

    @Column(name = "provider_id")
    private String providerId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, Boolean emailVerified, Provider provider, String providerId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.emailVerified = emailVerified;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
