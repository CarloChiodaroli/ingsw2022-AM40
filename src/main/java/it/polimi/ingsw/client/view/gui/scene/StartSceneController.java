package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.observer.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * Implements the start scene
 */
public class StartSceneController extends ViewObservable implements GenericSceneController {
    @FXML
    private Button confirmBtn;
    @FXML
    private Label variantLbl;
    @FXML
    private Label numberLbl;

    private boolean status;
    private int players;
    private boolean main;

    public StartSceneController() {
        status = false;
        players = 0;
    }

    /**
     * FXML's initialize method
     */
    @FXML
    public void initialize() {
        if (status)
            variantLbl.setText("Expert game!");
        else
            variantLbl.setText("Normal game!");
        numberLbl.setText(players + " players mode.");
        confirmBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmBtnClick);
        confirmBtn.setDisable(!main);
    }

    /**
     * Handle click on Confirm button
     *
     * @param event mouse click
     */
    private void onConfirmBtnClick(Event event) {
        new Thread(() -> notifyObserver(obs -> obs.onUpdateStart())).start();
    }

    /**
     * Get game params
     *
     * @param status  game mode
     * @param players number of players
     */
    public void getGameParams(boolean status, int players) {
        this.status = status;
        this.players = players;
    }

    /**
     * Set main player
     *
     * @param main true if is main
     */
    public void setMainPlayer(boolean main) {
        this.main = main;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfirm(String what) {

    }
}
