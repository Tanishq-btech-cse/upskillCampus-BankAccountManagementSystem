package com.bank.management.system.entity;

import com.bank.management.system.annotation.AccountNumberId;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity representing a bank account within the
 * Bank Management System.
 *
 * <p>This entity models the core domain object of the application
 * and stores all essential information related to a customer's
 * banking account.</p>
 *
 * <h2>Core Attributes</h2>
 * <ul>
 *     <li><b>Account Number</b> – Unique identifier generated using
 *     {@link AccountNumberId}</li>
 *     <li><b>Holder Name</b> – Name of the account owner</li>
 *     <li><b>Balance</b> – Current monetary balance</li>
 *     <li><b>Username</b> – Unique login credential</li>
 *     <li><b>Password</b> – Authentication credential</li>
 *     <li><b>MPIN</b> – Security PIN for transaction authorization</li>
 * </ul>
 *
 * <h2>Database Mapping</h2>
 * <ul>
 *     <li>Mapped to table: <b>account</b></li>
 *     <li>Primary key generated via custom ID generator</li>
 *     <li>Maintains a one-to-many relationship with {@link History}</li>
 * </ul>
 *
 * <h2>Relationship Details</h2>
 * <p>The {@code historyList} field represents a
 * {@code OneToMany} association with {@link History}.
 * The relationship is:</p>
 * <ul>
 *     <li><b>Mapped By:</b> account (owning side is History)</li>
 *     <li><b>Cascade:</b> ALL (propagates persistence operations)</li>
 *     <li><b>Orphan Removal:</b> Enabled</li>
 *     <li><b>Fetch Type:</b> LAZY (loaded on demand)</li>
 * </ul>
 *
 * <p>This entity serves as the central domain model in the
 * layered architecture of the application.</p>
 *
 * @author Tanishq Mathpal
 * @since 1.0
 */
@Entity
@Table(name = "account")
public class Account {

    /**
     * Unique account number generated automatically.
     */
    @Id
    @GeneratedValue
    @AccountNumberId
    @Column(name = "account_number")
    private long accountNumber;

    /**
     * Name of the account holder.
     */
    @Column(name = "holder_name", nullable = false)
    private String name;

    @Column(name = "bank_name", nullable = false)
    private String bankName;
    @Column(name = "ifsc_code", nullable = false)
    private String ifscCode;

    /**
     * Current balance available in the account.
     */
    @Column(name = "account_balance", nullable = false)
    private double balance;

    /**
     * Username used for login authentication.
     */
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * Password used for login authentication.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * MPIN used for authorizing transactions.
     */
    @Column(name = "mpin", nullable = false)
    private int mPin;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    /**
     * List of all transactions associated with this account.
     * Maintains the complete transaction history.
     */
    @OneToMany(mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<History> historyList = new ArrayList<>();


    public long getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMPin() {
        return mPin;
    }

    public void setMPin(int mPin) {
        this.mPin = mPin;
    }

    public List<History> getHistoryList() {
        return historyList;
    }

    /**
     * Adds a transaction record to this account's history.
     * Also sets the account reference inside the History entity.
     *
     * @param history transaction history object to add
     */
    public void addHistory(History history) {
        historyList.add(history);
        history.setAccount(this);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}