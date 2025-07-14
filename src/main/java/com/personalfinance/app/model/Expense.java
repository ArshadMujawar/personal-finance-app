package com.personalfinance.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(nullable = false)
    private Double amount;
    
    @Column(nullable = false, length = 50)
    private String category;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false, length = 100)
    private String description;
    
    // Constructor for creating expense without ID
    public Expense(Integer userId, Double amount, String category, LocalDate date, String description) {
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }
} 