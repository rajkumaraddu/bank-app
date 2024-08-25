package com.gpact.bank_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountFormMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "Username is mandatory")
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotNull(message = "Mobile is mandatory")
    @NotBlank(message = "Mobile is mandatory")
    private String mobile;
    @NotNull(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email format is not correct")
    private String email;
    @Min(value = 10, message = "Minimum age is 10")
    private Integer age;
    @Size(min = 12, max = 12, message = "Enter valid Aadhar number. Length 12")
    private String aadhar;
    @Size(min = 10, max = 10, message = "Enter valid PAN card number. Length 10")
    private String pan;
    @NotNull(message = "Address is mandatory")
    private String address;
    @NotNull(message = "Acc Type is mandatory (Savings, Current)")
    private String accType;
    private String status;
    private String statusMessage;

}
