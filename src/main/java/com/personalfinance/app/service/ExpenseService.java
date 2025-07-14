package com.personalfinance.app.service;

import com.personalfinance.app.dto.ExpenseRequest;
import com.personalfinance.app.model.Expense;
import com.personalfinance.app.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    public List<Expense> getAllExpensesByUserId(Integer userId) {
        return expenseRepository.findByUserIdOrderByDateDesc(userId);
    }
    
    public List<Expense> getExpensesByCategory(Integer userId, String category) {
        return expenseRepository.findByUserIdAndCategoryOrderByDateDesc(userId, category);
    }
    
    public List<Expense> getExpensesByDateRange(Integer userId, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate);
    }
    
    public Expense createExpense(Integer userId, ExpenseRequest request) {
        Expense expense = new Expense(
            userId,
            request.getAmount(),
            request.getCategory(),
            request.getDate(),
            request.getDescription()
        );
        return expenseRepository.save(expense);
    }
    
    public Optional<Expense> getExpenseById(Integer expenseId, Integer userId) {
        return expenseRepository.findById(expenseId)
                .filter(expense -> expense.getUserId().equals(userId));
    }
    
    public Optional<Expense> updateExpense(Integer expenseId, Integer userId, ExpenseRequest request) {
        return expenseRepository.findById(expenseId)
                .filter(expense -> expense.getUserId().equals(userId))
                .map(expense -> {
                    expense.setAmount(request.getAmount());
                    expense.setCategory(request.getCategory());
                    expense.setDate(request.getDate());
                    expense.setDescription(request.getDescription());
                    return expenseRepository.save(expense);
                });
    }
    
    public boolean deleteExpense(Integer expenseId, Integer userId) {
        Optional<Expense> expense = expenseRepository.findById(expenseId)
                .filter(e -> e.getUserId().equals(userId));
        
        if (expense.isPresent()) {
            expenseRepository.deleteById(expenseId);
            return true;
        }
        return false;
    }
    
    public Map<String, Double> getExpensesByCategory(Integer userId) {
        List<Object[]> results = expenseRepository.getExpensesByCategory(userId);
        return results.stream()
                .collect(Collectors.toMap(
                    row -> (String) row[0],
                    row -> (Double) row[1]
                ));
    }
    
    public Double getTotalExpenses(Integer userId) {
        return expenseRepository.getTotalExpensesByUserId(userId);
    }
    
    public Double getTotalExpensesByDateRange(Integer userId, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.getTotalExpensesByUserIdAndDateRange(userId, startDate, endDate);
    }
} 