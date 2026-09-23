package com.example.notify;

public class SmsNotifier implements Notifier{
    @Override
    public void send(String message) {
        System.out.println("SMS NOTIFIY");
    }
}
