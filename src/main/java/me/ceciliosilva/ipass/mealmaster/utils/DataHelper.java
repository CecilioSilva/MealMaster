package me.ceciliosilva.ipass.mealmaster.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataHelper {

    // The default save directory path
    private static final String saveDirPath = "/home";

    @SuppressWarnings("unchecked")
    public static <T> T loadObject(String saveFileName, T defaultValue) {
        Logger.info("DataHelper", "Loading object at:", saveFileName);

        try {
            // Gets the path where the object should exist
            Path userStorage = Path.of(saveDirPath, saveFileName);
            InputStream is = Files.newInputStream(userStorage);
            ObjectInputStream ois = new ObjectInputStream(is);

            // Casts the read object as the type of the defaultValue
            return (T) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            Logger.error("DataHelper", "reading object", e.toString());

            // If there is an error return the default value
            return defaultValue;
        }
    }

    public static void saveObject(String saveFileName, Object objectToSave) {

        // Gets the directory where the object should be saved
        Path saveDir = Path.of(saveDirPath);

        try {
            // If the directories do not exist create them recursively
            if (!Files.exists(saveDir)) {
                Files.createDirectories(saveDir);
            }
        } catch (IOException e) {
            Logger.error("DataHelper", "creating folders at:", e.toString());
            return;
        }

        // Creates the path of the object to be saved
        Path objectStorage = Path.of(saveDirPath, saveFileName);
        Logger.info("DataHelper", "Saving object at:", objectStorage.toUri().toString());

        try {
            OutputStream os = Files.newOutputStream(objectStorage);
            ObjectOutputStream oos = new ObjectOutputStream(os);

            // Saves the object at the selected path
            oos.writeObject(objectToSave);
        } catch (IOException e) {
            Logger.error("DataHelper", "saving object:", e.toString());
        }

    }

}
