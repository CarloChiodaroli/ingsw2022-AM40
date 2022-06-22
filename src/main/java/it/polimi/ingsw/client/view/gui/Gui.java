package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.model.Commands;
import it.polimi.ingsw.client.model.PlayMessageController;
import it.polimi.ingsw.client.model.PlayState;
import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.gui.scene.*;
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
    private boolean status;
    private int players = 0;
    private boolean inPlay = false;
    private PlayState state;
    private Commands commands;


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
    public void showLoginResult(boolean connectionCompleted, boolean connectionSuccessful, String nickname) {
        if (!connectionCompleted || !connectionSuccessful) {
            if (!connectionCompleted && connectionSuccessful) {
                Platform.runLater(() -> {
                    SceneController.changeRootPane(observers, "login_scene.fxml");
                });
            } else {
                Platform.runLater(() -> {
                    SceneController.showAlert(STR_ERROR, "Client will be closed soon");
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
        commands = new Commands(playMessageController);
        state = playMessageController.getState();
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
        Platform.runLater(() -> {
            SceneController.showAlert(STR_ERROR, errorMessage);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        if(!inPlay){
            getPlaySceneController();
            inPlay = true;
        }
        else sendConfirmation("update");
    }

    private void getPlaySceneController() {
        PlaySceneController psc;
        try {
            psc = (PlaySceneController) SceneController.getActiveController();
        } catch (ClassCastException e) {
            psc = new PlaySceneController();
            psc.addAllObservers(observers);
            psc.addGameState(state);
            psc.addCommandSender(commands);
            PlaySceneController finalPsc = psc;
            Platform.runLater(() -> SceneController.changeRootPane(finalPsc, "play_scene.fxml"));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askPlayCustomization() {
        WizardSceneController wsc = new WizardSceneController();
        wsc.addAllObservers(observers);
        wsc.setAvailableWizard(state.getAvailableWizards());
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
        ssc.setMainPlayer(state.isMainPlayer());
        Platform.runLater(() -> SceneController.changeRootPane(ssc, "start_scene.fxml"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showAvailableWizards() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showExpert(boolean expertStatus) {
        okVariant = true;
        status = expertStatus;
        sendConfirmation("Expert");
        if(okNumber)
            askPlayCustomization();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showEndGame(String winner) {
            Platform.runLater(() -> {
                SceneController.showWin(winner);
                SceneController.changeRootPane(observers, MENU_SCENE_FXML);
            });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showChosenNumOfPlayers(int maxPlayers) {
        okNumber = true;
        players = maxPlayers;
        sendConfirmation("Number");
        if(okVariant)
            askPlayCustomization();
    }

    private static void sendConfirmation(String what){
        Platform.runLater(() -> SceneController.sendConfirm(what));
    }
}
