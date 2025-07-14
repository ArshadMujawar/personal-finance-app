package com.personalfinance.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponse {
    private Integer id;
    private String month;
    private Double budgetAmount;
    private Double spentAmount;
    private Double remainingAmount;
    private Double percentageUsed;
    private String status; // "Under Budget", "Near Limit", "Over Budget"
    
    public BudgetResponse(Integer id, String month, Double budgetAmount, Double spentAmount) {
        this.id = id;
        this.month = month;
        this.budgetAmount = budgetAmount;
        this.spentAmount = spentAmount != null ? spentAmount : 0.0;
        this.remainingAmount = this.budgetAmount - this.spentAmount;
        this.percentageUsed = this.budgetAmount > 0 ? (this.spentAmount / this.budgetAmount) * 100 : 0.0;
        this.status = _calculateStatus(this.percentageUsed);
    }
    
    private String _calculateStatus(Double percentage) {
        if (percentage >= 100) {
            return "Over Budget";
        } else if (percentage >= 80) {
            return "Near Limit";
        } else {
            return "Under Budget";
        }
    }
} 