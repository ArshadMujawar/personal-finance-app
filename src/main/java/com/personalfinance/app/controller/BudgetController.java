package com.personalfinance.app.controller;

import com.personalfinance.app.dto.BudgetRequest;
import com.personalfinance.app.dto.BudgetResponse;
import com.personalfinance.app.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/budgets")
@CrossOrigin(origins = "*")
public class BudgetController {
    
    @Autowired
    private BudgetService budgetService;
    
    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(@RequestParam Integer userId) {
        List<BudgetResponse> budgets = budgetService.getAllBudgetsByUserId(userId);
        return ResponseEntity.ok(budgets);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getBudgetById(@PathVariable Integer id, @RequestParam Integer userId) {
        // This would need to be implemented differently since we need the month
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/month/{month}")
    public ResponseEntity<BudgetResponse> getBudgetByMonth(
            @PathVariable String month, 
            @RequestParam Integer userId) {
        Optional<BudgetResponse> budget = budgetService.getBudgetByMonth(userId, month);
        return budget.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/current")
    public ResponseEntity<BudgetResponse> getCurrentMonthBudget(@RequestParam Integer userId) {
        Optional<BudgetResponse> budget = budgetService.getCurrentMonthBudget(userId);
        return budget.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/overview")
    public ResponseEntity<BudgetResponse> getBudgetOverview(@RequestParam Integer userId) {
        BudgetResponse overview = budgetService.getBudgetOverview(userId);
        return ResponseEntity.ok(overview);
    }
    
    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @RequestBody BudgetRequest request,
            @RequestParam Integer userId) {
        try {
            BudgetResponse budget = budgetService.createBudget(userId, request);
            return ResponseEntity.status(201).body(budget);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable Integer id,
            @RequestBody BudgetRequest request,
            @RequestParam Integer userId) {
        Optional<BudgetResponse> updatedBudget = budgetService.updateBudget(id, userId, request);
        return updatedBudget.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(
            @PathVariable Integer id,
            @RequestParam Integer userId) {
        boolean deleted = budgetService.deleteBudget(id, userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 