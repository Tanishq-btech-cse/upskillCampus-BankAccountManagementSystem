package com.bank.management.system.service;

import com.bank.management.system.config.HibernateUtility;
import com.bank.management.system.entity.Account;
import com.bank.management.system.entity.History;
import com.bank.management.system.exceptions.NoTransectionFound;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class AccountService implements AccountServiceInterface {

    @Override
    public void openAccount(String name, double initialBalance,
                            String bank, String ifsc,
                            String username, String password, int mPin) {

        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        try (Session session = HibernateUtility.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            Account account = new Account();
            History history = new History();

            account.setName(name);
            account.setBalance(initialBalance);
            account.setUsername(username);
            account.setPassword(password);
            account.setMPin(mPin);
            account.setBankName(bank);
            account.setIfscCode(ifsc);

            history.setType("New Account");
            history.setAmount(initialBalance);
            history.setDateTime(LocalDateTime.now());

            account.addHistory(history);

            session.persist(account);
            transaction.commit();

            System.out.println("Account created successfully ❤️");
            System.out.println("Account Number: " + account.getAccountNumber());
        }
    }

    @Override
    public Account login(String username, String password) {

        try (Session session = HibernateUtility.getSessionFactory().openSession()) {

            String hql = "FROM Account WHERE username = :u AND password = :p";

            Query<Account> query = session.createQuery(hql, Account.class);
            query.setParameter("u", username);
            query.setParameter("p", password);

            return query.uniqueResult();
        }
    }

    @Override
    public String deposit(Account account, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        try (Session session = HibernateUtility.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            Account managed =
                    session.find(Account.class, account.getAccountNumber());

            managed.setBalance(managed.getBalance() + amount);

            History history = new History();
            history.setType("Deposit");
            history.setAmount(amount);
            history.setDateTime(LocalDateTime.now());

            managed.addHistory(history);

            transaction.commit();

            return "Deposit successful 👍";
        }
    }

    @Override
    public String withdraw(Account account, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        try (Session session = HibernateUtility.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            Account managed =
                    session.find(Account.class, account.getAccountNumber());

            if (managed.getBalance() < amount) {
                throw new RuntimeException("Insufficient balance ❌");
            }

            managed.setBalance(managed.getBalance() - amount);

            History history = new History();
            history.setType("Withdraw");
            history.setAmount(amount);
            history.setDateTime(LocalDateTime.now());

            managed.addHistory(history);

            transaction.commit();

            return "Withdrawal successful 👍";
        }
    }

    @Override
    public void checkBalance(Account account) {

        try (Session session = HibernateUtility.getSessionFactory().openSession()) {

            Account managed =
                    session.find(Account.class, account.getAccountNumber());

            System.out.println("Balance: " + managed.getBalance());
        }
    }

    @Override
    public String transfer(Account sender, long receiverAccNo,
                           String receiverIfsc, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        try (Session session = HibernateUtility.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            Account managedSender =
                    session.find(Account.class, sender.getAccountNumber());

            if (managedSender.getBalance() < amount) {
                throw new RuntimeException("Insufficient balance ❌");
            }

            if (receiverIfsc.equalsIgnoreCase(managedSender.getIfscCode())) {

                Account receiver = session.find(Account.class, receiverAccNo);

                if (receiver == null) {
                    throw new RuntimeException("Receiver not found ❌");
                }

                managedSender.setBalance(managedSender.getBalance() - amount);
                receiver.setBalance(receiver.getBalance() + amount);

                History sendHistory = new History();
                sendHistory.setType("Transfer (Same Bank)");
                sendHistory.setAmount(amount);
                sendHistory.setDateTime(LocalDateTime.now());

                History receiveHistory = new History();
                receiveHistory.setType("Received");
                receiveHistory.setAmount(amount);
                receiveHistory.setDateTime(LocalDateTime.now());

                managedSender.addHistory(sendHistory);
                receiver.addHistory(receiveHistory);

                transaction.commit();

                return "Same Bank Transfer Successful 👍";
            }

            else {

                managedSender.setBalance(managedSender.getBalance() - amount);

                History history = new History();
                history.setType("External Transfer (" + receiverIfsc + ")");
                history.setAmount(amount);
                history.setDateTime(LocalDateTime.now());

                managedSender.addHistory(history);

                transaction.commit();

                return "External Transfer Initiated 👍";
            }
        }
    }

    @Override
    public Account getUpdatedAccount(long accountNumber) {

        try (Session session = HibernateUtility
                .getSessionFactory()
                .openSession()) {

            return session.find(Account.class, accountNumber);
        }
    }

    @Override
    public List<History> getTransactionHistory(long accountNumber) {

        try (Session session = HibernateUtility
                .getSessionFactory()
                .openSession()) {

            Query<History> query = session.createQuery(
                    "SELECT h FROM History h " +
                            "WHERE h.account.accountNumber = :acc " +
                            "ORDER BY h.dateTime DESC",
                    History.class
            );

            query.setParameter("acc", accountNumber);

            List<History> list = query.getResultList();

            if (list.isEmpty()) {
                throw new NoTransectionFound("No transaction found");
            }

            return list;
        }
    }
}