package com.example.demo;

import java.time.LocalDateTime;

public class TodoEntry {
    String name;
    LocalDateTime due;

    TodoEntry() {
    }
    public TodoEntry(String name) {
        this.name = name;
    }
}