package com.example.notify;

import com.example.audit.AuditLogger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.jmx.support.ObjectNameManager;

import java.util.List;


public class NotifyManager {

    List<Notifier> notifiers;

    public NotifyManager(List<Notifier> notifiers){
        this.notifiers=notifiers;
    }

    public void sendNotifiers(String msg){
        for (Notifier notifier : notifiers) {
            notifier.send(msg);
        }
    }
}
