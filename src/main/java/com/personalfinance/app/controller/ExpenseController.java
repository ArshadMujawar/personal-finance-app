package com.personalfinance.app.controller;

import com.personalfinance.app.dto.ExpenseRequest;
import com.personalfinance.app.model.Expense;
import com.personalfinance.app.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {
    
    @Autowired
    private ExpenseService expenseService;
    
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses(@RequestParam Integer userId) {
        List<Expense> expenses = expenseService.getAllExpensesByUserId(userId);
        return ResponseEntity.ok(expenses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Integer id, @RequestParam Integer userId) {
        Optional<Expense> expense = expenseService.getExpenseById(id, userId);
        return expense.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Expense>> getExpensesByCategory(
            @PathVariable String category, 
            @RequestParam Integer userId) {
        List<Expense> expenses = expenseService.getExpensesByCategory(userId, category);
        return ResponseEntity.ok(expenses);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<Expense>> getExpensesByDateRange(
            @RequestParam Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Expense> expenses = expenseService.getExpensesByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(expenses);
    }
    
    @PostMapping
    public ResponseEntity<Expense> createExpense(
            @RequestBody ExpenseRequest request,
            @RequestParam Integer userId) {
        Expense expense = expenseService.createExpense(userId, request);
        return ResponseEntity.status(201).body(expense);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(
            @PathVariable Integer id,
            @RequestBody ExpenseRequest request,
            @RequestParam Integer userId) {
        Optional<Expense> updatedExpense = expenseService.updateExpense(id, userId, request);
        return updatedExpense.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Integer id,
            @RequestParam Integer userId) {
        boolean deleted = expenseService.deleteExpense(id, userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/analytics/by-category")
    public ResponseEntity<Map<String, Double>> getExpensesByCategory(@RequestParam Integer userId) {
        Map<String, Double> analytics = expenseService.getExpensesByCategory(userId);
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/analytics/total")
    public ResponseEntity<Map<String, Double>> getTotalExpenses(@RequestParam Integer userId) {
        Double total = expenseService.getTotalExpenses(userId);
        return ResponseEntity.ok(Map.of("total", total != null ? total : 0.0));
    }
    
    @GetMapping("/analytics/total-by-date-range")
    public ResponseEntity<Map<String, Double>> getTotalExpensesByDateRange(
            @RequestParam Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Double total = expenseService.getTotalExpensesByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(Map.of("total", total != null ? total : 0.0));
    }
} 