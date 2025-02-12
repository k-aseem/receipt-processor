package com.fetchrewards.receipt_processor.controller;

import com.fetchrewards.receipt_processor.model.Receipt;
import com.fetchrewards.receipt_processor.model.ReceiptResponse;
import com.fetchrewards.receipt_processor.exception.ReceiptNotFoundException;
import com.fetchrewards.receipt_processor.model.PointsResponse;
import com.fetchrewards.receipt_processor.util.Strings;
import com.fetchrewards.receipt_processor.service.ReceiptService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    // POST endpoint: Process a receipt and return a generated id.
    @PostMapping("/process")
    public ResponseEntity<ReceiptResponse> processReceipt(@Valid @RequestBody Receipt receipt) {
        String id = receiptService.processReceipt(receipt);
        return ResponseEntity.ok(new ReceiptResponse(id));
    }

    // GET endpoint: Retrieve the points for a receipt based on id.
    @GetMapping("/{id}/points")
    public ResponseEntity<PointsResponse> getPoints(@PathVariable String id) {
        Integer points = receiptService.getPoints(id);
        if (points == null) {
            throw new ReceiptNotFoundException(Strings.RECEIPT_NOT_FOUND);
        }
        return ResponseEntity.ok(new PointsResponse(points));
    }
}
