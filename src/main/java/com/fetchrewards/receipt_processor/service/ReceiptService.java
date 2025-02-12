package com.fetchrewards.receipt_processor.service;

import com.fetchrewards.receipt_processor.model.Receipt;
import com.fetchrewards.receipt_processor.model.Item;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReceiptService {

    // In-memory store for receipt IDs and their points
    private Map<String, Integer> receiptPoints = new ConcurrentHashMap<>();

    // Process receipt: calculate points, store them, and return a unique id
    public String processReceipt(Receipt receipt) {
        int points = calculatePoints(receipt);
        String id = UUID.randomUUID().toString();
        receiptPoints.put(id, points);
        return id;
    }

    // Retrieve points by id
    public Integer getPoints(String id) {
        return receiptPoints.get(id);
    }

    // Calculate points based on the rules
    // Any parsing errors (e.g., for date or time) will throw an exception.
    public int calculatePoints(Receipt receipt) {
        int points = 0;

        // Rule 1: 1 point for every alphanumeric character in the retailer name.
        String retailer = receipt.getRetailer();
        for (char c : retailer.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                points++;
            }
        }

        // Convert total to BigDecimal for numeric operations
        BigDecimal total = new BigDecimal(receipt.getTotal());

        // Rule 2: 50 points if total is a round dollar amount (no cents).
        if (total.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            points += 50;
        }

        // Rule 3: 25 points if the total is a multiple of 0.25.
        if (total.remainder(new BigDecimal("0.25")).compareTo(BigDecimal.ZERO) == 0) {
            points += 25;
        }

        // Rule 4: 5 points for every two items on the receipt.
        int itemPairs = receipt.getItems().size() / 2;
        points += itemPairs * 5;

        // Rule 5: For each item, if the trimmed description's length is a multiple of
        // 3, add points equal to the ceiling of (price * 0.2).
        for (Item item : receipt.getItems()) {
            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                BigDecimal price = new BigDecimal(item.getPrice());
                BigDecimal multiplied = price.multiply(new BigDecimal("0.2"));
                int itemPoints = multiplied.setScale(0, RoundingMode.CEILING).intValue();
                points += itemPoints;
            }
        }

        // Rule 6: 6 points if the day in the purchase date (YYYY-MM-DD) is odd.
        LocalDate date = LocalDate.parse(receipt.getPurchaseDate(), DateTimeFormatter.ISO_DATE);
        if (date.getDayOfMonth() % 2 == 1) {
            points += 6;
        }

        // Rule 7: 10 points if the time of purchase (HH:mm) is between 2:00pm
        // (inclusive) and 4:00pm (exclusive).
        LocalTime time = LocalTime.parse(receipt.getPurchaseTime(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime start = LocalTime.of(14, 0);
        LocalTime end = LocalTime.of(16, 0);
        if (!time.isBefore(start) && time.isBefore(end)) {
            points += 10;
        }

        return points;
    }
}
