package com.example.sitter;

public class Message {

    private long timeStamp = 0;
    private String content;
    private long sender;
    private long receiver;
    private int status = 0;

    public Message() {
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Message setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }

    public long getSender() {
        return sender;
    }

    public Message setSender(long sender) {
        this.sender = sender;
        return this;
    }

    public long getReceiver() {
        return receiver;
    }

    public Message setReceiver(long receiver) {
        this.receiver = receiver;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Message setStatus(int status) {
        this.status = status;
        return this;
    }
}
