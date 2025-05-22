package edu.felipebuso.util;

import edu.felipebuso.model.Event;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String FILE_NAME = "events.data";

    public static void saveEvents(List<Event> events) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
            FILE_NAME))) {
            oos.writeObject(events);
        } catch (IOException e) {
            System.out.println("Erro ao salvar os eventos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Event> loadEvents() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
            FILE_NAME))) {
            return (List<Event>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar os eventos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}
