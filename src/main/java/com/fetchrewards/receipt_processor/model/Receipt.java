package com.fetchrewards.receipt_processor.model;

import com.fetchrewards.receipt_processor.util.Strings;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class Receipt {
    @NotNull
    @Pattern(regexp = "^[\\w\\s\\-&]+$", message = Strings.INVALID_RECEIPT)
    private String retailer;

    @NotNull
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = Strings.INVALID_RECEIPT)
    private String purchaseDate;

    @NotNull
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = Strings.INVALID_RECEIPT)
    private String purchaseTime;

    @NotNull
    @Size(min = 1, message = Strings.INVALID_RECEIPT)
    private List<Item> items;

    @NotNull
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = Strings.INVALID_RECEIPT)
    private String total;
}