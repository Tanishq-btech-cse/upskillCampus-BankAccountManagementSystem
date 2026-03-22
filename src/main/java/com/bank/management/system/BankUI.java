package com.bank.management.system;

import static com.bank.management.system.config.UiUtility.showLoginUI;

/**
 * Main entry point for the Bank Account Management System application.
 *
 * <p>This class launches the Login User Interface using Swing.
 * It connects the application startup flow to the UI layer.</p>
 *
 * <p>When the program starts, it calls {@code showLoginUI()}
 * from {@code UiUtility} to display the login screen.</p>
 *
 * @author Tanishq Mathpal
 */

public class BankUI {
    /**
     * Main method that starts the banking application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        showLoginUI();
    }
}