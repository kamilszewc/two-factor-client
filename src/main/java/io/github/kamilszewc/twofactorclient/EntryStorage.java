package io.github.kamilszewc.twofactorclient;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class EntryStorage {

    static final String fileName = ".two_factor_client";

    private String password;

    static public ObservableList<Entry> entriesList = FXCollections.observableArrayList();

    public EntryStorage() {
        if (password != null) {
            try {
                readEntriesFromDisk();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        entriesList.addListener(new ListChangeListener<Entry>() {
            @Override
            public void onChanged(Change<? extends Entry> c) {
                if (c.next()) {
                    try {
                        writeEntriesToDisk();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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

            IvParameterSpec iv = new IvParameterSpec("xxxxxxxxxxxxxxxx".getBytes("UTF-8"));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, iv);

            FileInputStream fileInputStream = new FileInputStream(file);
            CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);
            ObjectInputStream objectInputStream = new ObjectInputStream(cipherInputStream);

            try {
                while (true) {
                    Entry entry = (Entry) objectInputStream.readObject();
                    entriesList.add(entry);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (EOFException ex) {
            }
        }
    }

    public void writeEntriesToDisk() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
        Path filePath = Path.of(System.getProperty("user.home"), fileName);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
        File file = new File(filePath.toUri());

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        byte[] salt = {1,2,3};
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        IvParameterSpec iv = new IvParameterSpec("xxxxxxxxxxxxxxxx".getBytes("UTF-8"));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret, iv);

        CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);
        ObjectOutputStream output = new ObjectOutputStream(cipherOutputStream);

        for (var entry : entriesList) {
            output.writeObject(entry);
        }

        output.close();
        fileOutputStream.close();
    }
}
