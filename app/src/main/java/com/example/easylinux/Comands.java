package com.example.easylinux;

public class Comands {
    private int id;
    private String title;
    private String command;
    private String description;
    private String example;

    public Comands(int id, String title, String command, String description, String example) {
        this.id = id;
        this.title = title;
        this.command = command;
        this.description = description;
        this.example = example;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCommand() { return command; }
    public String getDescription() { return description; }
    public String getExample() { return example; }
}
