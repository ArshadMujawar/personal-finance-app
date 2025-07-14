package com.personalfinance.app.repository;

import com.personalfinance.app.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    
    List<Expense> findByUserIdOrderByDateDesc(Integer userId);
    
    List<Expense> findByUserIdAndCategoryOrderByDateDesc(Integer userId, String category);
    
    List<Expense> findByUserIdAndDateBetweenOrderByDateDesc(Integer userId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT e.category, SUM(e.amount) FROM Expense e WHERE e.userId = :userId GROUP BY e.category")
    List<Object[]> getExpensesByCategory(@Param("userId") Integer userId);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.userId = :userId")
    Double getTotalExpensesByUserId(@Param("userId") Integer userId);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.userId = :userId AND e.date BETWEEN :startDate AND :endDate")
    Double getTotalExpensesByUserIdAndDateRange(@Param("userId") Integer userId, 
                                               @Param("startDate") LocalDate startDate, 
                                               @Param("endDate") LocalDate endDate);
} 