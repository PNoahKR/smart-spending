package com.smartspending.transaction.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smartspending.dashboard.dto.response.CategoryStatisticDto;
import com.smartspending.transaction.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.smartspending.category.entity.QCategory.category;
import static com.smartspending.transaction.entity.QTransaction.transaction;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<BigDecimal> sumIncomeByUserAndDate(Long userId, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalIncome = queryFactory.select(transaction.amount.sum())
                .from(transaction)
                .where(transaction.user.id.eq(userId),
                        transaction.type.eq(TransactionType.INCOME),
                        transaction.date.between(startDate, endDate)
                )
                .fetchOne();

        return Optional.ofNullable(totalIncome);
    }

    @Override
    public Optional<BigDecimal> sumExpenseByUserAndDate(Long userId, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalExpense = queryFactory.select(transaction.amount.sum())
                .from(transaction)
                .where(transaction.user.id.eq(userId),
                        transaction.type.eq(TransactionType.EXPENSE),
                        transaction.date.between(startDate, endDate)
                )
                .fetchOne();

        return Optional.ofNullable(totalExpense);
    }

    @Override
    public Optional<List<CategoryStatisticDto>> findTopSpendingCategories(Long userId, LocalDate startDate, LocalDate endDate) {
        List<CategoryStatisticDto> topCategories = queryFactory.select(Projections.constructor(CategoryStatisticDto.class,
                        category.name,
                        transaction.amount.sum()))
                .from(transaction)
                .join(transaction.category, category)
                .where(transaction.user.id.eq(userId),
                        transaction.type.eq(TransactionType.EXPENSE),
                        transaction.date.between(startDate, endDate)
                )
                .groupBy(category.name)
                .orderBy(transaction.amount.sum().desc())
                .limit(5)
                .fetch();

        return Optional.ofNullable(topCategories);
    }
}
