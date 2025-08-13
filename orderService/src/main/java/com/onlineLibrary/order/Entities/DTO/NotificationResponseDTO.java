package com.onlineLibrary.order.Entities.DTO;

public class NotificationResponseDTO {

    private String to;
    private String subject;
    private String message;


    public NotificationResponseDTO(String email, String subject, String message) {
        this.to = email;
        this.subject = subject;
        this.message = message;
    }

    // Getters et setters

    public String getTo() {
        return to;
    }

    public void setTo(String email) {
        this.to = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
