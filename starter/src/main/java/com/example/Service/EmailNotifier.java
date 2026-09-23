package com.example.Service;

import com.example.notify.Notifier;

public class EmailNotifier implements Notifier {
    @Override
    public void send(String message) {
        System.out.println("EMAIL NOTIFIY");
    }
}
