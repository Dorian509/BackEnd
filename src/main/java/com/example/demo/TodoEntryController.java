package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TodoEntryController {

    @GetMapping("/todos")
    public String todos() { return "M1, M2, M3";
        //public List<TodoEntry> getTodoEntries() {
        //return List.of(new TodoEntry("M1"), new TodoEntry("M2"), new TodoEntry("M3"));
    }

}