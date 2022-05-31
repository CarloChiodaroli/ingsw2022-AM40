package it.polimi.ingsw.client.view.gui;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.gui.scene.LobbySceneController;
import it.polimi.ingsw.client.view.gui.scene.PlayersNumberSceneController;
import it.polimi.ingsw.commons.observer.ViewObservable;
import javafx.application.Platform;

import java.util.List;

public class Gui extends ViewObservable implements View {
    private static final String STR_ERROR = "ERROR";
    private static final String MENU_SCENE_FXML = "menu_scene.fxml";

    @Override
    public void askNickname() {
        Platform.runLater(() -> SceneController.changeRootPane(observers, "login_scene.fxml"));
    }

    @Override
    public void askPlayersNumber() {
        PlayersNumberSceneController pnsc = new PlayersNumberSceneController();
        pnsc.addAllObservers(observers);
        pnsc.setPlayersRange(2, 3);
        Platform.runLater(() -> SceneController.changeRootPane(pnsc, "players_number_scene.fxml"));
    }

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

    @Override
    public void showGenericMessage(String genericMessage) {
        Platform.runLater(() -> SceneController.showAlert("Info Message", genericMessage));
    }

    @Override
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        Platform.runLater(() -> {
            SceneController.showAlert("GAME OVER", "The player " + nicknameDisconnected + " disconnected.");
            SceneController.changeRootPane(observers, MENU_SCENE_FXML);
        });
    }

    @Override
    public void showErrorAndExit(String errorMsg) {
        Platform.runLater(() -> {
            SceneController.showAlert(STR_ERROR, errorMsg);
            SceneController.changeRootPane(observers, MENU_SCENE_FXML);
        });
    }

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


}
