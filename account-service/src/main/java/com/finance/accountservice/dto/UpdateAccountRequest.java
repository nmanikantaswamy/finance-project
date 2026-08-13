package com.finance.accountservice.dto;

import com.finance.accountservice.entity.AccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountRequest {

    @NotNull(message = "Status is required")
    private AccountStatus status;
}