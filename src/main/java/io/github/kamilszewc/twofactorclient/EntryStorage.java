package io.github.kamilszewc.twofactorclient;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class EntryStorage {

    static final String fileName = ".two_factor_client";

    static public ObservableList<Entry> entriesList = FXCollections.observableArrayList();

    EntryStorage() {
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

    private void readEntriesFromDisk() {
        entriesList.addAll(List.of(Entry.builder()
                .serviceName("Service name")
                .userName("User name")
                .secret("Secret")
                .issuer("Issuer")
                .build()));
    }

    private void writeEntriesToDisk() throws IOException {
        File file = new File(Path.of(System.getProperty("user.home"), fileName).toUri());
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        for (var entry : entriesList) {
            String line = entry.toString() + "\n";
            bufferedWriter.write(line);
        }

        bufferedWriter.close();
        fileWriter.close();
    }
}
