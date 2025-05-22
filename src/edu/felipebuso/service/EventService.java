package edu.felipebuso.service;


import edu.felipebuso.model.Event;
import edu.felipebuso.model.User;
import edu.felipebuso.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EventService {
    private List<Event> events;

    public EventService() {
        this.events = FileUtil.loadEvents();
    }

    public void createEvent(
        String name,
        String address,
        String category,
        LocalDateTime dateTime,
        String description
    ) {
        Event event = new Event(name, address, category, dateTime, description);
        events.add(event);
        FileUtil.saveEvents(events);
    }

    public List<Event> listAllEventsSorted() {
        return events.stream()
            .sorted(Comparator.comparing(Event::getDateTime))
            .collect(Collectors.toList());
    }

    public List<Event> listPastEvents() {
        LocalDateTime now = LocalDateTime.now();
        return events.stream()
            .filter(e -> e.getDateTime().isBefore(now))
            .sorted(Comparator.comparing(Event::getDateTime))
            .collect(Collectors.toList());
    }

    public List<Event> listOngoingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return events.stream()
            .filter(e -> e.getDateTime()
                .isBefore(now.plusMinutes(1)) && e.getDateTime()
                .isAfter(now.minusHours(3))) // Evento atual
            .sorted(Comparator.comparing(Event::getDateTime))
            .collect(Collectors.toList());
    }

    public List<Event> listUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return events.stream()
            .filter(e -> e.getDateTime().isAfter(now))
            .sorted(Comparator.comparing(Event::getDateTime))
            .collect(Collectors.toList());
    }

    public void confirmParticipation(User user, int index) {
        if (index >= 0 && index < events.size()) {
            Event event = events.get(index);
            event.addParticipant(user.getEmail());
            FileUtil.saveEvents(events);
        }
    }

    public void cancelParticipation(User user, int index) {
        if (index >= 0 && index < events.size()) {
            Event event = events.get(index);
            event.removeParticipant(user.getEmail());
            FileUtil.saveEvents(events);
        }
    }

    public List<Event> listUserEvents(User user) {
        return events.stream()
            .filter(e -> e.isParticipant(user.getEmail()))
            .collect(Collectors.toList());
    }

    public List<Event> getEvents() {
        return events;
    }
}
