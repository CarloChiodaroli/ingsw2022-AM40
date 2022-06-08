package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.observer.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class StartSceneController extends ViewObservable implements GenericSceneController{
    @FXML
    private Button confirmBtn;
    @FXML
    private Label variantLbl;
    @FXML
    private Label numberLbl;

    private boolean status;
    private int players;

    public StartSceneController(){
        status = false;
        players = 0;
    }

    @FXML
    public void initialize() {
        if(status)
            variantLbl.setText("Expert game!");
        else
            variantLbl.setText("Normal game!");
        numberLbl.setText(players + " players mode.");
        confirmBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmBtnClick);
    }

    private void onConfirmBtnClick(Event event) {
        new Thread(() -> notifyObserver(obs -> obs.onUpdateStart())).start();
    }

    public void getGameParams(boolean status, int players) {
        this.status = status;
        this.players = players;
    }

    @Override
    public void onConfirm(String what) {

    }
}
