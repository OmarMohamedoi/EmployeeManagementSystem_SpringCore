package com.example.audit;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Scope("prototype")
public class AuditLogger {

    private final LocalDateTime timestamp = LocalDateTime.now();


    public void log(){
        System.out.println("Successfully created at: "+ timestamp);
    }
}
