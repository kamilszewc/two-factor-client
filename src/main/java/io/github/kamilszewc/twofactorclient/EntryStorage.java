package io.github.kamilszewc.twofactorclient;

import java.util.List;

public class EntryStorage {

    EntryStorage() {
    }

    public List<Entry> getAllEntries() {
        return List.of(Entry.builder()
                .serviceName("Service name")
                .userName("User name")
                .secret("Secret")
                .issuer("Issuer")
                .build());
    }
}
