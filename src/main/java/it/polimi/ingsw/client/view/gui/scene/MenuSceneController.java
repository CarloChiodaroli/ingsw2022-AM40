package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.client.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * This class implements the main menu scene.
 */
public class MenuSceneController extends ViewObservable implements GenericSceneController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button playBtn;

    /**
     * FXML's initialize method
     */
    @FXML
    public void initialize() {
        playBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPlayBtnClick);
    }

    /**
     * Handles click on Play button.
     *
     * @param event mouse click
     */
    private void onPlayBtnClick(Event event) {
        SceneController.changeRootPane(observers, event, "connect_scene.fxml");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfirm(String what) {

    }
}