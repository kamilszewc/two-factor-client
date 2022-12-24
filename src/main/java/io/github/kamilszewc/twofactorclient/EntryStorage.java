package io.github.kamilszewc.twofactorclient;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class EntryStorage {

    static final String fileName = ".two_factor_client";

    static public ObservableList<Entry> entriesList = FXCollections.observableArrayList();

    EntryStorage() throws IOException {
        readEntriesFromDisk();

        entriesList.addListener(new ListChangeListener<Entry>() {
            @Override
            public void onChanged(Change<? extends Entry> c) {
                if (c.next()) {
                    try {
                        writeEntriesToDisk();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public ObservableList<Entry> getEntries() {
        return entriesList;
    }

    private void readEntriesFromDisk() throws IOException {
        Path filePath = Path.of(System.getProperty("user.home"), fileName);
        File file = new File(filePath.toUri());
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        try {
            while (true) {
                Entry entry = (Entry)objectInputStream.readObject();
                entriesList.add(entry);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (EOFException ex) {
        }
    }

    private void writeEntriesToDisk() throws IOException {
        Path filePath = Path.of(System.getProperty("user.home"), fileName);
        Files.delete(filePath);
        File file = new File(filePath.toUri());

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream output = new ObjectOutputStream(fileOutputStream);

        for (var entry : entriesList) {
            output.writeObject(entry);
        }

        output.close();
        fileOutputStream.close();
    }
}
