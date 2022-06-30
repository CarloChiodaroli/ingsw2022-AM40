package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.network.Client;
import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.client.observer.ViewObserver;
import it.polimi.ingsw.client.view.gui.scene.AlertSceneController;
import it.polimi.ingsw.client.view.gui.scene.GenericSceneController;
import it.polimi.ingsw.client.view.gui.scene.WinSceneController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.List;

/**
 * This class implements the controller of a generic scene.
 */
public class SceneController extends ViewObservable {
    private static Scene activeScene;
    private static GenericSceneController activeController;

    /**
     * Returns the active scene
     *
     * @return active scene
     */
    public static Scene getActiveScene() {
        return activeScene;
    }

    /**
     * Returns the active controller
     *
     * @return active controller
     */
    public static GenericSceneController getActiveController() {
        return activeController;
    }


    /**
     * Changes the root panel of the scene argument
     *
     * @param observerList a list of observers to be set into the scene controller
     * @param scene        scene whose change the root panel. This will become the active scene
     * @param fxml         new scene fxml name
     * @param <T>          type parameter
     * @return controller of the new scene loaded by the FXMLLoader
     */
    public static <T> T changeRootPane(List<ViewObserver> observerList, Scene scene, String fxml) {
        T controller = null;

        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();
            controller = loader.getController();
            ((ViewObservable) controller).addAllObservers(observerList);

            activeController = (GenericSceneController) controller;
            activeScene = scene;
            activeScene.setRoot(root);
        } catch (IOException e) {
            Client.LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }
        return controller;
    }

    /**
     * Changes the root panel of the scene argument
     *
     * @param observerList a list of observers to be set into the scene controller
     * @param event        event which is happened into the scene
     * @param fxml         new scene fxml name
     * @param <T>          type parameter
     * @return controller of the new scene loaded by the FXMLLoader
     */
    public static <T> T changeRootPane(List<ViewObserver> observerList, Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        return changeRootPane(observerList, scene, fxml);
    }

    /**
     * Changes the root panel of the active scene
     *
     * @param observerList a list of observers to be set into the scene controller
     * @param fxml         the new scene fxml name
     * @param <T>          this is the type parameter
     * @return the controller of the new scene loaded by the FXMLLoader
     */
    public static <T> T changeRootPane(List<ViewObserver> observerList, String fxml) {
        return changeRootPane(observerList, activeScene, fxml);
    }

    /**
     * Changes the root panel of the scene argument
     *
     * @param controller custom controller that will be set into the FXMLLoader
     * @param scene      scene whose change the root panel. This will become the active scene
     * @param fxml       new scene fxml name
     */
    public static void changeRootPane(GenericSceneController controller, Scene scene, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            loader.setController(controller);
            activeController = controller;
            Parent root = loader.load();
            activeScene = scene;
            activeScene.setRoot(root);
        } catch (IOException e) {
            Client.LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Changes the root panel of the scene argument
     *
     * @param controller custom controller that will be set into the FXMLLoader
     * @param event      event which is happened into the scene
     * @param fxml       new scene fxml name
     */
    public static void changeRootPane(GenericSceneController controller, Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        changeRootPane(controller, scene, fxml);
    }

    /**
     * Changes the root panel of the active scene
     *
     * @param controller custom controller that will be set into the FXMLLoader
     * @param fxml       new scene fxml name
     */
    public static void changeRootPane(GenericSceneController controller, String fxml) {
        changeRootPane(controller, activeScene, fxml);
    }

    /**
     * Shows a custom message in a popup
     *
     * @param title   title of the popup
     * @param message message of the popup
     */
    public static void showAlert(String title, String message) {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/alert_scene.fxml"));

        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            Client.LOGGER.severe(e.getMessage());
            return;
        }
        AlertSceneController alertSceneController = loader.getController();
        Scene alertScene = new Scene(parent);
        alertSceneController.setScene(alertScene);
        alertSceneController.setAlertTitle(title);
        alertSceneController.setAlertMessage(message);
        alertSceneController.displayAlert();
    }

    public static void sendConfirm(String what) {
        activeController.onConfirm(what);
    }

    /**
     * Shows the win message popup
     *
     * @param nickname name of the winner
     */
    public static void showWin(String nickname) {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/win_scene.fxml"));

        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            Client.LOGGER.severe(e.getMessage());
            return;
        }
        WinSceneController winSceneController = loader.getController();
        Scene winScene = new Scene(parent);
        winSceneController.setScene(winScene);
        winSceneController.setWinnerNickname(nickname);
        winSceneController.displayWinScene();
    }
}
