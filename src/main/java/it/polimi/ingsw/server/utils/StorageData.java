package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.network.Server;

import java.io.*;
import java.nio.file.Files;

public class StorageData {

    public void store(GameManager gameManager) {
        Persistence persistence = new Persistence(gameManager);
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(GameManager.SAVED_GAME_FILE))) {

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(persistence);
            Server.LOGGER.info("GameSaved.");
        } catch (IOException e) {
            Server.LOGGER.severe(e.getMessage());
        }
    }

    public GameManager restore() {
        Persistence persistence;
        try (FileInputStream fileInputStream = new FileInputStream(new File(GameManager.SAVED_GAME_FILE))) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            persistence = (Persistence) objectInputStream.readObject();
            return persistence.getGameController();
        } catch (IOException e) {
            Server.LOGGER.severe("No File Found.");
        } catch (ClassNotFoundException e) {
            Server.LOGGER.severe(e.getMessage());
        }
        return null;
    }

    public void delete() {
        File file = new File(GameManager.SAVED_GAME_FILE);
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            Server.LOGGER.severe("Failed to delete " + GameManager.SAVED_GAME_FILE + " file.");
        }
    }
}



