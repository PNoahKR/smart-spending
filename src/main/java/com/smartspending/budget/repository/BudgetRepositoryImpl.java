package com.smartspending.budget.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smartspending.budget.entity.Budget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.smartspending.budget.entity.QBudget.budget;

@Repository
@RequiredArgsConstructor
public class BudgetRepositoryImpl implements BudgetRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Budget> findByIsActiveTrueAndIsRecurringTrue() {
        return queryFactory.selectFrom(budget)
                .where(budget.isActive.eq(true),
                        budget.isRecurring.eq(true))
                .fetch();
    }

    @Override
    public Optional<BigDecimal> findMainBudgetByUser(Long userId) {
        BigDecimal bigDecimal = queryFactory.select(budget.amount)
                .from(budget)
                .where(budget.user.id.eq(userId),
                        budget.mainBudget.eq(true))
                .fetchOne();

        return Optional.ofNullable(bigDecimal);
    }
}
