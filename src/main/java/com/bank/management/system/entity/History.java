package com.bank.management.system.entity;

import com.bank.management.system.annotation.TransectionId;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA entity representing a transaction record in the
 * Bank Management System.
 *
         * <p>This entity maintains a complete audit trail of all
 * financial operations performed on an {@link Account}.</p>
        *
        * <h2>Supported Transaction Types</h2>
        * <ul>
 *     <li>Account Creation</li>
        *     <li>Deposit</li>
        *     <li>Withdrawal</li>
        *     <li>Transfer (Sent / Received)</li>
        * </ul>
        *
        * <h2>Core Attributes</h2>
        * <ul>
 *     <li><b>Transaction ID</b> – Unique identifier generated using
 *     {@link TransectionId}</li>
        *     <li><b>Type</b> – Nature of the transaction</li>
        *     <li><b>Amount</b> – Monetary value involved</li>
        *     <li><b>Date & Time</b> – Timestamp of execution</li>
        *     <li><b>Account</b> – Associated {@link Account}</li>
        * </ul>
        *
        * <h2> Database Mapping</h2>
        * <ul>
 *     <li>Mapped to table: <b>history</b></li>
        *     <li>Primary key generated via custom ID generator</li>
        *     <li>Many-to-one relationship with {@link Account}</li>
        * </ul>
        *
        * <h2>Relationship Details</h2>
        * <p>Each History record belongs to exactly one {@link Account},
        * while one account may have multiple transaction records.</p>
        *
        * <p>This entity plays a critical role in ensuring traceability
 * and accountability of banking operations.</p>
        *
        * @author Tanishq Mathpal
 * @since 1.0
 * */
@Entity
@Table(name = "history")
public class History {
    /**
     * Unique transaction ID generated automatically.
     */
    @Id
    @GeneratedValue
    @TransectionId
    private int transectionId;

    /**
     * Type of transaction (Deposit, Withdraw, Transfer, etc.)
     */
    private String type;

    /**
     * Amount involved in the transaction.
     */
    private double amount;

    /**
     * Account associated with this transaction.
     */
    @ManyToOne
    @JoinColumn(name = "account_number")
    private Account account;

    /**
     * Date and time when the transaction occurred.
     */
    private LocalDateTime dateTime;

    public int getTransectionId() {
        return transectionId;
    }

    public void setTransectionId(int transectionId) {
        this.transectionId = transectionId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "History{" +
                "transectionId=" + transectionId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                '}';
    }
}