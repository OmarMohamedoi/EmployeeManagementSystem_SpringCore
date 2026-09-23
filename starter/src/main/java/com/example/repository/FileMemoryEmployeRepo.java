package com.example.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("Prod")
@Repository
public class FileMemoryEmployeRepo {
}
