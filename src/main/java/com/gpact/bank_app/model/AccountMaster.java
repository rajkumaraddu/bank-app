package com.gpact.bank_app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccountMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accNumber;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserMaster user;
    private String accType;
    private Double balance;
    private String accStatus;
}
