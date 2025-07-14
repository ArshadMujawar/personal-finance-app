package com.personalfinance.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Budget {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(nullable = false, length = 50)
    private String month;
    
    @Column(nullable = false)
    private Double amount;
    
    // Constructor for creating budget without ID
    public Budget(Integer userId, String month, Double amount) {
        this.userId = userId;
        this.month = month;
        this.amount = amount;
    }
} 