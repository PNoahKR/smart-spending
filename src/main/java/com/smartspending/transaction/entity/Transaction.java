package com.smartspending.transaction.entity;

import com.smartspending.category.entity.Category;
import com.smartspending.common.entity.BaseEntity;
import com.smartspending.transaction.enums.TransactionType;
import com.smartspending.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private Category category;

    @Column(name = "meno")
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Builder
    public Transaction(User user, BigDecimal amount, LocalDate date, String memo, TransactionType type, Category category) {
        this.user = user;
        this.amount = amount;
        this.date = date;
        this.memo = memo;
        this.type = type;
        this.category = category;
    }
}
