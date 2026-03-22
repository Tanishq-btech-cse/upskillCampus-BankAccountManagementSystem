package com.bank.management.system.service;

import com.bank.management.system.entity.Account;
import com.bank.management.system.entity.History;
import com.bank.management.system.exceptions.NoAccountFound;
import com.bank.management.system.exceptions.NoTransectionFound;

import java.util.List;

/**
 * Core service contract defining business operations
 * for account management within the Bank Management System.
 *
 * <p>This interface represents the Service Layer in a layered
 * architecture and acts as an abstraction between the
 * presentation layer (UI/Controller) and the data access layer.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>User authentication</li>
 *     <li>Account creation</li>
 *     <li>Deposit and withdrawal operations</li>
 *     <li>Funds transfer between accounts</li>
 *     <li>Balance inquiry</li>
 *     <li>Transaction history retrieval</li>
 * </ul>
 *
 * <p>All implementations must ensure transactional integrity,
 * data consistency, and proper validation of business rules.</p>
 *
 * <h2>Design Principles</h2>
 * <ul>
 *     <li>Follows Service-Oriented Architecture (SOA)</li>
 *     <li>Encapsulates business logic</li>
 *     <li>Promotes loose coupling and testability</li>
 * </ul>
 *
 * @author Tanishq Mathpal
 * @since 1.0
 */
public interface AccountServiceInterface {

    /**
     * Authenticates a user based on provided credentials.
     *
     * @param username the unique username of the account holder
     * @param password the associated password
     * @return the authenticated {@link Account} if credentials are valid;
     *         otherwise may return null or throw an authentication exception
     */
    Account login(String username, String password);

    /**
     * Deposits a specified amount into the given account.
     *
     * @param account the target account
     * @param amount  the amount to deposit (must be positive)
     * @throws IllegalArgumentException if amount is invalid
     */
    String deposit(Account account, double amount);

    /**
     * Withdraws a specified amount from the given account.
     *
     * @param account the source account
     * @param amount  the amount to withdraw (must be positive)
     * @throws IllegalArgumentException if amount is invalid
     * @throws RuntimeException if insufficient balance
     */
    String withdraw(Account account, double amount);

    /**
     * Opens a new bank account with the provided details.
     *
     * @param name           account holder's full name
     * @param initialBalance initial deposit amount
     * @param username       unique login username
     * @param password       login password
     * @param mPin           transaction security PIN
     *
     * @throws IllegalArgumentException if validation fails
     */

    void openAccount(String name, double initialBalance,
                     String Bank, String ifsc,
                     String username, String password, int mPin);

    /**
     * Displays or retrieves the current balance
     * of the specified account.
     *
     * @param account the account to check
     */
    void checkBalance(Account account);

    /**
     * Transfers funds from one account to another.
     *
     * @param sender        the account initiating the transfer
     * @param receiverAccNo receiver's account number
     * @param amount        transfer amount (must be positive)
     *
     * @throws IllegalArgumentException if validation fails
     * @throws RuntimeException if insufficient balance
     */
    String transfer(Account sender, long receiverAccNo,
                    String receiverIfsc, double amount);

    /**
     * Retrieves the latest persisted state of an account.
     *
     * @param accountNumber the account number
     * @return updated {@link Account} instance
     */
    Account getUpdatedAccount(long accountNumber) throws NoAccountFound;

    /**
     * Retrieves the transaction history for a given account.
     *
     * @param accountNumber the account number
     * @return list of {@link History} entries associated with the account
     */

    List<History> getTransactionHistory(long accountNumber) throws NoTransectionFound;
}