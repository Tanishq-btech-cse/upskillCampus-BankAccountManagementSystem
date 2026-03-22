package com.bank.management.system.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;

public class RazorpayService {

    private static final String KEY = "rzp_test_SUODgWUudHNngg";
    private static final String SECRET = "aOedhDyPoSWz1f46I9KkcG9B";

    public static String createOrder(double amount) {

        try {
            RazorpayClient client = new RazorpayClient(KEY, SECRET);

            JSONObject options = new JSONObject();
            options.put("amount", (int)(amount * 100)); // in paise
            options.put("currency", "INR");
            options.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = client.orders.create(options);

            return order.get("id");

        } catch (Exception e) {
            throw new RuntimeException("Payment creation failed ❌");
        }
    }
}