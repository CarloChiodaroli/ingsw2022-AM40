package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.gui.SceneController;
import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.client.observer.ViewObserver;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * This class implements the scene where users choose their nicknames.
 */
public class LoginSceneController extends ViewObservable implements GenericSceneController {

    @FXML
    private TextField nicknameField;

    @FXML
    private Button joinBtn;

    @FXML
    public void initialize() {
        joinBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onJoinBtnClick);
    }

    /**
     * Handle click on Join button.
     *
     * @param event the mouse click event.
     */
    private void onJoinBtnClick(Event event) {

        String nickname = nicknameField.getText();
        

        new Thread(() -> notifyObserver(obs -> obs.onUpdateNickname(nickname))).start();
    }

    @Override
    public void onConfirm(String what) {

    }
}
