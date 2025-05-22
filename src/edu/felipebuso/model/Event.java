package edu.felipebuso.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Event implements Serializable {
    private String name;
    private String address;
    private String category;
    private LocalDateTime dateTime;
    private String description;
    private Set<String> participantEmails;

    public Event(
        String name,
        String address,
        String category,
        LocalDateTime dateTime,
        String description
    ) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.dateTime = dateTime;
        this.description = description;
        this.participantEmails = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getParticipantEmails() {
        return participantEmails;
    }

   
    public void addParticipant(String email) {
        participantEmails.add(email);
    }

    public void removeParticipant(String email) {
        participantEmails.remove(email);
    }

    public boolean isParticipant(String email) {
        return participantEmails.contains(email);
    }

    @Override
    public String toString() {
        return "\nEvento: " + name +
            "\nEndereço: " + address +
            "\nCategoria: " + category +
            "\nData e hora: " + dateTime +
            "\nDescrição: " + description +
            "\nParticipantes confirmados: " + participantEmails.size() + "\n";
    }
}
