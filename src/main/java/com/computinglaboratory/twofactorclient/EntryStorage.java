package com.computinglaboratory.twofactorclient;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class EntryStorage {

    static final String fileName = ".two_factor_client";

    private String password = "";

    static public ObservableList<Entry> entriesList = FXCollections.observableArrayList();

    public EntryStorage() {
        if (!password.isBlank()) {
            try {
                readEntriesFromDisk();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        entriesList.addListener((ListChangeListener<Entry>) c -> {
            if (c.next()) {
                try {
                    writeEntriesToDisk();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public Boolean isFileCreated() {
        return Files.exists(Path.of(System.getProperty("user.home"), fileName));
    }

    public ObservableList<Entry> getEntries() {
        return entriesList;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void readEntriesFromDisk() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Path filePath = Path.of(System.getProperty("user.home"), fileName);
        File file = new File(filePath.toUri());
        if (file.exists()) {

            byte[] salt = {1,2,3};
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            IvParameterSpec iv = new IvParameterSpec("xxxxxxxxxxxxxxxx".getBytes(StandardCharsets.UTF_8));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, iv);

            FileInputStream fileInputStream = new FileInputStream(file);

            ObjectInputStream objectInputStream;
            if (!password.isBlank()) {
                CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);
                objectInputStream = new ObjectInputStream(cipherInputStream);
            } else {
                objectInputStream = new ObjectInputStream(fileInputStream);
            }

            try {
                while (true) {
                    Entry entry = (Entry) objectInputStream.readObject();
                    entriesList.add(entry);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (Exception ex) {
            }
        }
    }

    public void writeEntriesToDisk() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
        Path filePath = Path.of(System.getProperty("user.home"), fileName);
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
            } catch (FileSystemException ex) {
            }
        }
        File file = new File(filePath.toUri());

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        byte[] salt = {1,2,3};
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        IvParameterSpec iv = new IvParameterSpec("xxxxxxxxxxxxxxxx".getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret, iv);

        ObjectOutputStream output;
        if (!password.isBlank()) {
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);
            output = new ObjectOutputStream(cipherOutputStream);
        } else {
            output = new ObjectOutputStream(fileOutputStream);
        }

        for (var entry : entriesList) {
            output.writeObject(entry);
        }

        output.close();
        fileOutputStream.close();
    }
}
