package com.smartspending.transaction.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smartspending.common.response.PageInfoResponseDto;
import com.smartspending.dashboard.dto.response.CategoryStatisticDto;
import com.smartspending.dashboard.dto.response.IncomeExpenseChartDto;
import com.smartspending.dashboard.dto.response.TransactionSummaryDto;
import com.smartspending.transaction.dto.response.TransactionResponseDto;
import com.smartspending.transaction.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.smartspending.category.entity.QCategory.category;
import static com.smartspending.transaction.entity.QTransaction.transaction;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PageInfoResponseDto<TransactionResponseDto> findByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to, Pageable pageable) {
        int offset = (int) pageable.getOffset();
        int size = pageable.getPageSize();

        List<TransactionResponseDto> fetch = queryFactory
                .select(Projections.constructor(
                        TransactionResponseDto.class,
                        transaction.id,
                        transaction.amount,
                        transaction.date,
                        transaction.memo,
                        transaction.type.stringValue(),
                        category.name,
                        category.id
                ))
                .from(transaction)
                .join(transaction.category, category)
                .where(transaction.user.id.eq(userId),
                        transaction.date.between(from, to))
                .orderBy(transaction.date.desc())
                .offset(offset)
                .limit(size)
                .fetch();

        Long fetchOne = queryFactory
                .select(transaction.count())
                .from(transaction)
                .where(transaction.user.id.eq(userId),
                        transaction.date.between(from, to))
                .fetchOne();

        int totalCount = fetchOne.intValue();

        return PageInfoResponseDto.of(pageable, fetch, totalCount);
    }

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

    @Override
    public List<IncomeExpenseChartDto> getIncomeExpenseChart(Long userId, LocalDate start, LocalDate end) {
        List<Tuple> results = queryFactory
                .select(
                        transaction.date,
                        transaction.type,
                        transaction.amount.sum()
                )
                .from(transaction)
                .where(
                        transaction.user.id.eq(userId),
                        transaction.date.between(start, end)
                )
                .groupBy(transaction.date, transaction.type)
                .orderBy(transaction.date.asc())
                .fetch();

        // 날짜별로 수입/지출을 하나의 객체로 매핑
        Map<LocalDate, IncomeExpenseChartDto> chartMap = new TreeMap<>();

        for (Tuple tuple : results) {
            LocalDate date = tuple.get(transaction.date);
            TransactionType type = tuple.get(transaction.type);
            BigDecimal sum = tuple.get(transaction.amount.sum());

            chartMap.putIfAbsent(date, new IncomeExpenseChartDto(date, BigDecimal.ZERO, BigDecimal.ZERO));
            IncomeExpenseChartDto dto = chartMap.get(date);

            if (type == TransactionType.INCOME) {
                dto.setIncome(sum);
            } else {
                dto.setExpense(sum);
            }
        }

        return new ArrayList<>(chartMap.values());
    }

    @Override
    public List<TransactionSummaryDto> findRecentTransactions(Long userId, int limit) {
        return queryFactory
                .select(Projections.constructor(TransactionSummaryDto.class,
                        transaction.date,
                        transaction.type.stringValue(),
                        category.name,  // 주의: category가 null일 수 있음
                        transaction.amount
                ))
                .from(transaction)
                .leftJoin(transaction.category, category)  // ★ leftJoin으로 변경
                .where(transaction.user.id.eq(userId))
                .orderBy(transaction.date.desc())
                .limit(limit)
                .fetch();
    }
}
