package com.personalfinance.app.repository;

import com.personalfinance.app.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    
    List<Budget> findByUserIdOrderByMonthDesc(Integer userId);
    
    Optional<Budget> findByUserIdAndMonth(Integer userId, String month);
    
    boolean existsByUserIdAndMonth(Integer userId, String month);
    
    @Query("SELECT b FROM Budget b WHERE b.userId = :userId AND b.month = :month")
    Optional<Budget> findCurrentMonthBudget(@Param("userId") Integer userId, @Param("month") String month);
} 