package com.personalfinance.app.service;

import com.personalfinance.app.dto.BudgetRequest;
import com.personalfinance.app.dto.BudgetResponse;
import com.personalfinance.app.model.Budget;
import com.personalfinance.app.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetService {
    
    @Autowired
    private BudgetRepository budgetRepository;
    
    @Autowired
    private ExpenseService expenseService;
    
    public List<BudgetResponse> getAllBudgetsByUserId(Integer userId) {
        List<Budget> budgets = budgetRepository.findByUserIdOrderByMonthDesc(userId);
        return budgets.stream()
                .map(budget -> {
                    Double spentAmount = expenseService.getTotalExpensesByDateRange(
                        userId, 
                        getStartOfMonth(budget.getMonth()), 
                        getEndOfMonth(budget.getMonth())
                    );
                    return new BudgetResponse(budget.getId(), budget.getMonth(), budget.getAmount(), spentAmount);
                })
                .collect(Collectors.toList());
    }
    
    public Optional<BudgetResponse> getBudgetByMonth(Integer userId, String month) {
        Optional<Budget> budget = budgetRepository.findByUserIdAndMonth(userId, month);
        if (budget.isPresent()) {
            Double spentAmount = expenseService.getTotalExpensesByDateRange(
                userId, 
                getStartOfMonth(month), 
                getEndOfMonth(month)
            );
            return Optional.of(new BudgetResponse(budget.get().getId(), month, budget.get().getAmount(), spentAmount));
        }
        return Optional.empty();
    }
    
    public BudgetResponse createBudget(Integer userId, BudgetRequest request) {
        // Check if budget already exists for this month
        if (budgetRepository.existsByUserIdAndMonth(userId, request.getMonth())) {
            throw new RuntimeException("Budget already exists for " + request.getMonth());
        }
        
        Budget budget = new Budget(userId, request.getMonth(), request.getAmount());
        Budget savedBudget = budgetRepository.save(budget);
        
        Double spentAmount = expenseService.getTotalExpensesByDateRange(
            userId, 
            getStartOfMonth(request.getMonth()), 
            getEndOfMonth(request.getMonth())
        );
        
        return new BudgetResponse(savedBudget.getId(), request.getMonth(), request.getAmount(), spentAmount);
    }
    
    public Optional<BudgetResponse> updateBudget(Integer budgetId, Integer userId, BudgetRequest request) {
        return budgetRepository.findById(budgetId)
                .filter(budget -> budget.getUserId().equals(userId))
                .map(budget -> {
                    budget.setMonth(request.getMonth());
                    budget.setAmount(request.getAmount());
                    Budget updatedBudget = budgetRepository.save(budget);
                    
                    Double spentAmount = expenseService.getTotalExpensesByDateRange(
                        userId, 
                        getStartOfMonth(request.getMonth()), 
                        getEndOfMonth(request.getMonth())
                    );
                    
                    return new BudgetResponse(updatedBudget.getId(), request.getMonth(), request.getAmount(), spentAmount);
                });
    }
    
    public boolean deleteBudget(Integer budgetId, Integer userId) {
        Optional<Budget> budget = budgetRepository.findById(budgetId)
                .filter(b -> b.getUserId().equals(userId));
        
        if (budget.isPresent()) {
            budgetRepository.deleteById(budgetId);
            return true;
        }
        return false;
    }
    
    public Optional<BudgetResponse> getCurrentMonthBudget(Integer userId) {
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return getBudgetByMonth(userId, currentMonth);
    }
    
    public BudgetResponse getBudgetOverview(Integer userId) {
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        Optional<Budget> budget = budgetRepository.findByUserIdAndMonth(userId, currentMonth);
        
        Double budgetAmount = budget.map(Budget::getAmount).orElse(0.0);
        Double spentAmount = expenseService.getTotalExpensesByDateRange(
            userId, 
            getStartOfMonth(currentMonth), 
            getEndOfMonth(currentMonth)
        );
        
        return new BudgetResponse(
            budget.map(Budget::getId).orElse(null),
            currentMonth,
            budgetAmount,
            spentAmount
        );
    }
    
    private LocalDate getStartOfMonth(String month) {
        return LocalDate.parse(month + "-01");
    }
    
    private LocalDate getEndOfMonth(String month) {
        LocalDate startOfMonth = getStartOfMonth(month);
        return startOfMonth.plusMonths(1).minusDays(1);
    }
} 