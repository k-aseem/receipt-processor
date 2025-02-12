package com.fetchrewards.receipt_processor.model;

import com.fetchrewards.receipt_processor.util.Strings;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Item {
    @NotNull
    @Pattern(regexp = "^[\\w\\s\\-]+$", message = Strings.INVALID_RECEIPT)
    private String shortDescription;

    @NotNull
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = Strings.INVALID_RECEIPT)
    private String price;
}