package it.polimi.ingsw.manuel.utils;

import it.polimi.ingsw.manuel.controller.GameController;
import it.polimi.ingsw.manuel.network.server.Server;

import java.io.*;
import java.nio.file.Files;

public class StorageData {

    public void store(GameController gameController) {
        Persistence persistence = new Persistence(gameController);
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(GameController.SAVED_GAME_FILE))) {

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(persistence);
            Server.LOGGER.info("GameSaved.");
        } catch (IOException e) {
            Server.LOGGER.severe(e.getMessage());
        }
    }

    public GameController restore() {
        Persistence persistence;
        try (FileInputStream fileInputStream = new FileInputStream(new File(GameController.SAVED_GAME_FILE))) {
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
        File file = new File(GameController.SAVED_GAME_FILE);
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            Server.LOGGER.severe("Failed to delete " + GameController.SAVED_GAME_FILE + " file.");
        }
    }
}



