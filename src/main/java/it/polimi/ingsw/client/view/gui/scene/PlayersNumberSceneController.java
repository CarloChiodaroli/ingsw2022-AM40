package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.observer.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

/**
 * This class implements the Player number and expert variant choose scene
 */
public class PlayersNumberSceneController extends ViewObservable implements GenericSceneController {

    @FXML
    private Button confirmBtn;
    @FXML
    private RadioButton radioBtn1;
    @FXML
    private RadioButton radioBtn2;
    @FXML
    private RadioButton radioBtn3;
    @FXML
    private RadioButton radioBtn4;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private ToggleGroup toggleGroup1;

    private int minPlayers;
    private int maxPlayers;
    private boolean expert;
    private boolean numOfPlayersOk;
    private boolean expertOk;
    private boolean first = true;

    /**
     * Default constructor.
     */
    public PlayersNumberSceneController() {
        minPlayers = 0;
        maxPlayers = 0;
        expert = false;
    }

    /**
     * FXML's initialize method
     */
    @FXML
    public void initialize() {
        radioBtn1.setText(minPlayers + " players");
        radioBtn2.setText(maxPlayers + " players");
        radioBtn3.setText("Expert");
        radioBtn4.setText("Normal");
        confirmBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmBtnClick);
        numOfPlayersOk = false;
        expertOk = false;
    }

    /**
     * Handles click on Confirm button
     *
     * @param event mouse click
     */
    private void onConfirmBtnClick(Event event) {
        confirmBtn.setDisable(true);
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        RadioButton selectedRadioButton1 = (RadioButton) toggleGroup1.getSelectedToggle();
        int playersNumber = Character.getNumericValue(selectedRadioButton.getText().charAt(0));
        char variant = selectedRadioButton1.getText().charAt(0);
        if (variant == 'E')
            expert = true;
        if (!numOfPlayersOk) new Thread(() -> notifyObserver(obs -> obs.onUpdatePlayersNumber(playersNumber))).start();
        if (!expertOk) new Thread(() -> notifyObserver(obs -> obs.onUpdateExpert(expert))).start();
    }

    /**
     * Initialises the values for the radioButtons
     *
     * @param minPlayers minimum number of players
     * @param maxPlayers maximum number of players
     */
    public void setPlayersRange(int minPlayers, int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfirm(String what) {
        if (what.equals("Number")) {
            radioBtn1.setDisable(true);
            radioBtn2.setDisable(true);
            numOfPlayersOk = true;
            if (!expertOk) confirmBtn.setDisable(false);
            return;
        }
        if (what.equals("Expert")) {
            if (first) {
                first = false;
                return;
            }
            radioBtn3.setDisable(true);
            radioBtn4.setDisable(true);
            expertOk = true;
            if (numOfPlayersOk) confirmBtn.setDisable(false);
        }
    }
}
