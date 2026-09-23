package com.example.notify;

public class PushNotifier implements Notifier{
    @Override
    public void send(String message) {
        System.out.println("PUSH NOTIFIED");
    }
}
