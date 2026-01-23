package com.example.ecommerceproject.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_logs")
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipient;  // user email
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String body;
    private LocalDateTime sentAt;
    private boolean success;
    // ✅ Constructors
    public EmailLog() {}

    public EmailLog(String recipient, String subject, String body, LocalDateTime sentAt, boolean success) {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.sentAt = sentAt;
        this.success = success;
    }

    // ✅ Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
