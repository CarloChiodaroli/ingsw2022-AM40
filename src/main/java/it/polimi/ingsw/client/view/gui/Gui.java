package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.model.PlayMessageController;
import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.gui.scene.LobbySceneController;
import it.polimi.ingsw.client.view.gui.scene.PlayersNumberSceneController;
import it.polimi.ingsw.client.view.gui.scene.StartSceneController;
import it.polimi.ingsw.client.view.gui.scene.WizardSceneController;
import it.polimi.ingsw.commons.enums.Wizard;
import javafx.application.Platform;

import java.util.List;

/**
 * This class offers a Graphical User Interface. It is an implementation of the {@link View} interface.
 */
public class Gui extends ViewObservable implements View {
    private static final String STR_ERROR = "ERROR";
    private static final String MENU_SCENE_FXML = "menu_scene.fxml";
    private boolean okNumber = false;
    private boolean okVariant = false;
    private boolean status = false;
    private int players = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public void askNickname() {
        Platform.runLater(() -> SceneController.changeRootPane(observers, "login_scene.fxml"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askPlaySettings() {
        PlayersNumberSceneController pnsc = new PlayersNumberSceneController();
        pnsc.addAllObservers(observers);
        pnsc.setPlayersRange(2, 3);
        Platform.runLater(() -> SceneController.changeRootPane(pnsc, "players_number_scene.fxml"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        if (!nicknameAccepted || !connectionSuccessful) {
            if (!nicknameAccepted && connectionSuccessful) {
                Platform.runLater(() -> {
                    SceneController.showAlert(STR_ERROR, "Nickname already taken.");
                    SceneController.changeRootPane(observers, "login_scene.fxml");
                });
            } else {
                Platform.runLater(() -> {
                    SceneController.showAlert(STR_ERROR, "Could not contact server.");
                    SceneController.changeRootPane(observers, MENU_SCENE_FXML);
                });
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showGenericMessage(String genericMessage) {
        Platform.runLater(() -> SceneController.showAlert("Info Message", genericMessage));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        Platform.runLater(() -> {
            SceneController.showAlert("GAME OVER", "The player " + nicknameDisconnected + " disconnected.");
            SceneController.changeRootPane(observers, MENU_SCENE_FXML);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showErrorAndExit(String errorMsg) {
        Platform.runLater(() -> {
            SceneController.showAlert(STR_ERROR, errorMsg);
            SceneController.changeRootPane(observers, MENU_SCENE_FXML);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showLobby(List<String> nicknameList, int maxPlayers) {
        LobbySceneController lsc;

        try {
            lsc = (LobbySceneController) SceneController.getActiveController();
            lsc.setNicknames(nicknameList);
            lsc.setMaxPlayers(maxPlayers);
            Platform.runLater(lsc::updateValues);
        } catch (ClassCastException e) {
            lsc = new LobbySceneController();
            lsc.addAllObservers(observers);
            lsc.setNicknames(nicknameList);
            lsc.setMaxPlayers(maxPlayers);
            LobbySceneController finalLsc = lsc;
            Platform.runLater(() -> SceneController.changeRootPane(finalLsc, "lobby_scene.fxml"));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatePrinter(PlayMessageController playMessageController) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMainPlayerName(String mainPlayerName) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showOtherDisconnectionMessage(String nicknameDisconnected, String text) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String errorMessage) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askPlayCustomization(List<Wizard> wizards) {
        WizardSceneController wsc = new WizardSceneController();
        wsc.addAllObservers(observers);
        wsc.setAvailableWizard(wizards);
        Platform.runLater(() -> SceneController.changeRootPane(wsc, "wizard_scene.fxml"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWizard() {
        StartSceneController ssc = new StartSceneController();
        ssc.addAllObservers(observers);
        ssc.getGameParams(status, players);
        Platform.runLater(() -> SceneController.changeRootPane(ssc, "start_scene.fxml"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showExpert(boolean expertStatus) {
        okVariant = true;
        status = expertStatus;
        if(okNumber)
            askPlayCustomization();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showEndGame() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showChosenNumOfPlayers(int maxPlayers) {
        okNumber = true;
        players = maxPlayers;
        if(okVariant)
            askPlayCustomization();
    }
}
