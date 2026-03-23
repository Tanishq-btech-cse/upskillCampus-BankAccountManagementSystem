package com.bank.management.system.config;

import com.bank.management.system.entity.Account;
import com.bank.management.system.entity.BankData;
import com.bank.management.system.entity.History;
import com.bank.management.system.exceptions.NoTransectionFound;
import com.bank.management.system.service.AccountService;
import com.bank.management.system.service.RazorpayService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;



/**
 * Centralized UI manager for the Swing-based Bank Management System.
 *
 * <p>This class represents the <b>Presentation Layer</b> of the application.
 * It handles all user interface screens and interactions using Java Swing
 * components and communicates with the {@link AccountService} to perform
 * business operations.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>User authentication (Login)</li>
 *     <li>Account registration (Open Account)</li>
 *     <li>Dashboard navigation</li>
 *     <li>Deposit, Withdraw, and Transfer operations</li>
 *     <li>MPIN verification before secure transactions</li>
 *     <li>Transaction history display</li>
 * </ul>
 *
 * <h2>Architecture Role</h2>
 * <p>This class follows a layered architecture pattern:</p>
 * <ul>
 *     <li><b>UI Layer</b> → UiUtility (Swing)</li>
 *     <li><b>Service Layer</b> → {@link AccountService}</li>
 *     <li><b>Persistence Layer</b> → Hibernate ORM</li>
 * </ul>
 *
 * <h2>Design Notes</h2>
 * <ul>
 *     <li>All methods are static for centralized access.</li>
 *     <li>Maintains a single logged-in {@link Account} instance.</li>
 *     <li>Uses custom bank icon for consistent branding.</li>
 * </ul>
 *
 * <p><b>Note:</b> This class is tightly coupled with Swing components
 * and is intended for desktop-based applications.</p>
 *
 * @author Tanishq Mathpal
 * @since 1.0
 */
public class UiUtility {

    private static AccountService service = new AccountService();
    private static Account loggedInAccount;
    static ImageIcon bankIcon = new ImageIcon(
            UiUtility.class.getResource("/Tanishq_Bank.png")
    );
    static Image scaledImage = bankIcon.getImage()
            .getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    static ImageIcon smallIcon = new ImageIcon(scaledImage);


    /**
     * Displays the login screen for existing users.
     * Provides option to open a new account.
     */
    public static void showLoginUI() {
        JFrame frame = new JFrame("My Bank of India");
        frame.setSize(600, 450);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 102, 204),
                        0, getHeight(), new Color(0, 51, 102)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(350, 320));
        card.setBackground(Color.WHITE);
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("BANK LOGIN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(65, 105, 225));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(title, gbc);

        gbc.gridwidth = 1;

        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        card.add(userLabel, gbc);

        JTextField userField = new JTextField(15);
        gbc.gridx = 1;
        card.add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        card.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(15);
        gbc.gridx = 1;
        card.add(passField, gbc);

        JButton loginBtn = new JButton("LOGIN");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(loginBtn, gbc);

        JButton registerBtn = new JButton("OPEN ACCOUNT");
        gbc.gridy = 4;
        card.add(registerBtn, gbc);

        background.add(card);
        frame.add(background);
        frame.setVisible(true);

        loginBtn.addActionListener(e -> {
            Account account = service.login(
                    userField.getText(),
                    new String(passField.getPassword())
            );

            if (account != null) {
                loggedInAccount = account;
                frame.dispose();
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Login");
            }
        });

        registerBtn.addActionListener(e -> {
            frame.dispose();
            showRegisterUI();
        });
    }

    /**
     * Displays registration screen for opening new account.
     */
    public static void showRegisterUI() {

        JFrame frame = new JFrame("Open Account");
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        JTextField nameField = new JTextField();
        JTextField userField = new JTextField();
        JTextField bankField = new JTextField();
        JTextField ifscField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JTextField balanceField = new JTextField();
        JPasswordField mpinField = new JPasswordField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Username:"));
        panel.add(userField);

        panel.add(new JLabel("Bank Name"));
        panel.add(bankField);

        panel.add(new JLabel("IFSC"));
        panel.add(ifscField);

        panel.add(new JLabel("Password:"));
        panel.add(passField);

        panel.add(new JLabel("Initial Balance:"));
        panel.add(balanceField);

        panel.add(new JLabel("MPIN (4-digit):"));
        panel.add(mpinField);

        JButton createBtn = new JButton("CREATE ACCOUNT");
        panel.add(new JLabel());
        panel.add(createBtn);

        frame.add(panel);
        frame.setVisible(true);

        createBtn.addActionListener(e -> {

            try {
                double balance = Double.parseDouble(balanceField.getText());
                int mpin = Integer.parseInt(new String(mpinField.getPassword()));

                service.openAccount(
                        nameField.getText(),
                        balance,
                        bankField.getText(),
                        ifscField.getText(),
                        userField.getText(),
                        new String(passField.getPassword()),
                        mpin
                );

                JOptionPane.showMessageDialog(frame, "Account Created Successfully 👍");
                frame.dispose();
                showLoginUI();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Balance or MPIN must be numeric",
                        "Error", JOptionPane.ERROR_MESSAGE);

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame,
                        ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        });
    }

    /**
     * Displays dashboard after successful login.
     */
    public static void showDashboard() {
        JFrame frame = new JFrame("Bank Dashboard");
        frame.setSize(400, 400);
        frame.setResizable(false);
        frame.setLayout(new GridLayout(7, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel welcome = new JLabel(
                "Welcome " + loggedInAccount.getName(),
                SwingConstants.CENTER
        );
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        frame.add(welcome);

        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton balanceBtn = new JButton("Check Balance");
        JButton transferBtn = new JButton("Transfer");
        JButton historyBtn = new JButton("History");
        JButton logoutBtn = new JButton("Logout");

        frame.add(depositBtn);
        frame.add(withdrawBtn);
        frame.add(balanceBtn);
        frame.add(transferBtn);
        frame.add(historyBtn);
        frame.add(logoutBtn);

        depositBtn.addActionListener(e -> depositUI());
        withdrawBtn.addActionListener(e -> withdrawUI());
        balanceBtn.addActionListener(e -> checkBalanceUI());
        transferBtn.addActionListener(e -> transferUI());
        historyBtn.addActionListener(e-> showTransectionHistoryUI());
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            showLoginUI();
        });

        frame.setVisible(true);
    }

    /**
     * Verifies MPIN before transaction.
     */
    private static boolean verifyMPin() {

        JPasswordField pf = new JPasswordField();

        int option = JOptionPane.showConfirmDialog(
                null, pf, "Enter MPIN",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                smallIcon
        );

        if (option == JOptionPane.OK_OPTION) {

            int entered = Integer.parseInt(new String(pf.getPassword()));

            if (entered == loggedInAccount.getMPin()) {
                return true;
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Wrong MPIN",
                        "Error",
                        JOptionPane.ERROR_MESSAGE,
                        smallIcon
                );
            }
        }
        return false;
    }
    public static void showTransectionHistoryUI() {
        if (!verifyMPin()) return;
        try {
            List<History> historyList =
                    service.getTransactionHistory(loggedInAccount.getAccountNumber());

            JFrame frame = new JFrame("Transaction History");
            frame.setSize(900, 500);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());

            JLabel title = new JLabel(
                    "Transaction History - Account " +
                            loggedInAccount.getAccountNumber(),
                    SwingConstants.CENTER
            );
            title.setFont(new Font("Segoe UI", Font.BOLD, 18));

            String[] columns = {
                    "Transaction ID", "Type", "Amount", "Date & Time"
            };

            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable table = new JTable(model);
            table.setRowHeight(25);

            JScrollPane scrollPane = new JScrollPane(table);

            loadTableData(model, historyList);

            JPanel filterPanel = new JPanel();

            String[] types = {"All", "Deposit", "Withdraw", "Transfer", "Received"};
            JComboBox<String> typeFilter = new JComboBox<>(types);

            JTextField minAmountField = new JTextField(10);

            JButton filterBtn = new JButton("Filter");

            filterPanel.add(new JLabel("Type:"));
            filterPanel.add(typeFilter);

            filterPanel.add(new JLabel("Min Amount:"));
            filterPanel.add(minAmountField);

            filterPanel.add(filterBtn);

            filterBtn.addActionListener(e -> {

                String selectedType = typeFilter.getSelectedItem().toString();
                String minAmtText = minAmountField.getText();

                double minAmount = 0;
                if (!minAmtText.isEmpty()) {
                    minAmount = Double.parseDouble(minAmtText);
                }

                model.setRowCount(0);

                for (History h : historyList) {

                    boolean typeMatch =
                            selectedType.equals("All") ||
                                    h.getType().equalsIgnoreCase(selectedType);

                    boolean amountMatch =
                            h.getAmount() >= minAmount;

                    if (typeMatch && amountMatch) {
                        model.addRow(new Object[]{
                                h.getTransectionId(),
                                h.getType(),
                                h.getAmount(),
                                h.getDateTime()
                        });
                    }
                }
            });

            frame.add(title, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(filterPanel, BorderLayout.SOUTH);

            frame.setVisible(true);

        } catch (NoTransectionFound e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

//    public static void depositUI() {
//        if (!verifyMPin()) return;
//        String input = (String) JOptionPane.showInputDialog(
//                null,
//                "Enter Deposit Amount:",
//                "Deposit Amount",
//                JOptionPane.PLAIN_MESSAGE,
//                smallIcon,
//                null,
//                "0.0"
//        );
//        if (input != null) {
//            String msg = service.deposit(loggedInAccount, Double.parseDouble(input));
//            refreshAccount(); // update from DB
//            if(msg.equals("Deposit successful 👍")) {
//                JOptionPane.showMessageDialog(
//                        null,
//                        "Deposit Successful 👍\n" +
//                                "Account Balance: ₹" + loggedInAccount.getBalance(),
//                        "Transaction Status",
//                        JOptionPane.INFORMATION_MESSAGE,
//                        smallIcon
//                );
//            }
//            else {
//                JOptionPane.showMessageDialog(
//                        null,
//                        "Invalid attempt of deposit ❌\n" +
//                                " ",
//                        "Transaction Status",
//                        JOptionPane.INFORMATION_MESSAGE,
//                        smallIcon
//                );
//            }
//        }
//    }

    public static void depositUI() {

        if (!verifyMPin()) return;

        try {
            String amt = JOptionPane.showInputDialog("Enter amount:");

            if (amt == null || amt.isEmpty()) return;

            int result = JOptionPane.showConfirmDialog(
                    null,
                    "Complete payment in browser.",
                    "Payment",
                    JOptionPane.YES_NO_OPTION
            );


            if (result == JOptionPane.YES_OPTION) {
                double amount = Double.parseDouble(amt);
                String orderId = RazorpayService.createOrder(amount);
                java.awt.Desktop.getDesktop().browse(
                        new java.net.URI("https://razorpay.com/")
                );
                String msg = service.deposit(loggedInAccount, amount);
                refreshAccount();

                JOptionPane.showMessageDialog(null,
                        msg + "\nBalance: " + loggedInAccount.getBalance());

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    public static void withdrawUI() {
        if (!verifyMPin()) return;
        String input = (String) JOptionPane.showInputDialog(
                null,
                "Enter Withdraw Amount:",
                "Withdraw Amount",
                JOptionPane.PLAIN_MESSAGE,
                smallIcon,
                null,
                "0.0"
        );
        if (input != null) {
            String msg = service.withdraw(loggedInAccount, Double.parseDouble(input));
            refreshAccount();
            if(msg.equals("Withdrawal successful 👍")) {
                JOptionPane.showMessageDialog(null,
                        "Withdraw Successful 👍\n" +
                                "Account Balance: " + loggedInAccount.getBalance(), "Transaction Status",
                        JOptionPane.INFORMATION_MESSAGE,
                        smallIcon);
            } else if (msg.equals("Enter valid amount")) {
                JOptionPane.showMessageDialog(null,
                        "Enter valid amount ⚠️\n" +
                                " ", "Transaction Status",
                        JOptionPane.INFORMATION_MESSAGE,
                        smallIcon);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Insufficient balance ❌\n" +
                                " ", "Transaction Status",
                        JOptionPane.INFORMATION_MESSAGE,
                        smallIcon);
            }
        }
    }

    public static void checkBalanceUI() {
        if(!verifyMPin()) return;
        refreshAccount();
        JOptionPane.showMessageDialog(
                null,
                "Balance: " + loggedInAccount.getBalance(),"Balance Status",
                JOptionPane.INFORMATION_MESSAGE,
                smallIcon
        );
    }

    public static void transferUI() {

        if (!verifyMPin()) return;

        try {
            String[] options = {"Same Bank", "Other Bank"};

            String transferType = (String) JOptionPane.showInputDialog(
                    null,
                    "Select Transfer Type:",
                    "Transfer",
                    JOptionPane.PLAIN_MESSAGE,
                    smallIcon,
                    options,
                    options[0]
            );

            if (transferType == null) return;

            String acc = JOptionPane.showInputDialog(null,
                    "Enter Receiver Account Number:");

            if (acc == null || acc.isEmpty()) return;

            String ifsc;

            if (transferType.equals("Same Bank")) {
                ifsc = loggedInAccount.getIfscCode();
            }

            else {
                Object[] banks = BankData.BANK_IFSC_MAP.keySet().toArray();

                String selectedBank = (String) JOptionPane.showInputDialog(
                        null,
                        "Select Bank:",
                        "Bank Selection",
                        JOptionPane.PLAIN_MESSAGE,
                        smallIcon,
                        banks,
                        banks[0]
                );

                if (selectedBank == null) return;

                ifsc = BankData.BANK_IFSC_MAP.get(selectedBank);

                JOptionPane.showMessageDialog(null,
                        "Selected Bank: " + selectedBank +
                                "\nIFSC: " + ifsc);
            }

            String amt = JOptionPane.showInputDialog(null, "Enter Amount:");

            if (amt == null || amt.isEmpty()) return;

            String msg = service.transfer(
                    loggedInAccount,
                    Long.parseLong(acc),
                    ifsc,
                    Double.parseDouble(amt)
            );

            refreshAccount();

            JOptionPane.showMessageDialog(null,
                    msg + "\nBalance: " + loggedInAccount.getBalance(),
                    "Transaction Status",
                    JOptionPane.INFORMATION_MESSAGE,
                    smallIcon);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE,
                    smallIcon);
        }
    }

    private static void refreshAccount() {
        loggedInAccount =
                service.getUpdatedAccount(loggedInAccount.getAccountNumber());
    }
    private static void loadTableData(DefaultTableModel model, List<History> list) {
        for (History h : list) {
            model.addRow(new Object[]{
                    h.getTransectionId(),
                    h.getType(),
                    h.getAmount(),
                    h.getDateTime()
            });
        }
    }

}