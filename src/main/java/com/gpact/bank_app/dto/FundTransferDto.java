package com.gpact.bank_app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundTransferDto {

    @Min(value = 1, message = "Amount should be greater than ZERO")
    @NotNull
    private Double amount;
    private Integer fromAccount;
    private Integer toAccount;
    private String status;
}
