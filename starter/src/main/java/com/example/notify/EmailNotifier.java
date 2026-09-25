package com.example.notify;

public class EmailNotifier implements Notifier {
    @Override
    public void send(String message) {
        System.out.println("EMAIL NOTIFIY");
    }
}
