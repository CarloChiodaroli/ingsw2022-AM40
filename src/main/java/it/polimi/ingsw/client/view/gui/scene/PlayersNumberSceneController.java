package it.polimi.ingsw.client.view.gui.scene;
import it.polimi.ingsw.client.view.gui.SceneController;
import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.client.observer.ViewObserver;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

public class PlayersNumberSceneController  extends ViewObservable implements GenericSceneController {

    @FXML
    private Button confirmBtn;
    @FXML
    private Button backToMenuBtn;

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

    /**
     * Default constructor.
     */
    public PlayersNumberSceneController() {
        minPlayers = 0;
        maxPlayers = 0;
        expert = false;
    }

    @FXML
    public void initialize() {
        radioBtn1.setText(minPlayers + " players");
        radioBtn2.setText(maxPlayers + " players");
        radioBtn3.setText("Expert");
        radioBtn4.setText("Normal");
        confirmBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmBtnClick);
        backToMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMenuBtnClick);
        numOfPlayersOk = false;
        expertOk = false;
    }

    /**
     * Handles click on Confirm button.
     *
     * @param event the mouse click event.
     */
    private void onConfirmBtnClick(Event event) {
        confirmBtn.setDisable(true);
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        RadioButton selectedRadioButton1 = (RadioButton) toggleGroup1.getSelectedToggle();
        int playersNumber = Character.getNumericValue(selectedRadioButton.getText().charAt(0));
        char variant = selectedRadioButton1.getText().charAt(0);
        if(variant == 'E')
            expert = true;
        if(!numOfPlayersOk) new Thread(() -> notifyObserver(obs -> obs.onUpdatePlayersNumber(playersNumber))).start();
        if(!expertOk) new Thread(() -> notifyObserver(obs -> obs.onUpdateExpert(expert))).start();
    }

    /**
     * Handles click on Back button
     *
     * @param event the mouse click event.
     */
    private void onBackToMenuBtnClick(Event event) {
        backToMenuBtn.setDisable(true);
        new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }

    /**
     * Initialises the values for the radioButtons.
     *
     * @param minPlayers the minimum number of players.
     * @param maxPlayers the maximum number of players.
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
        if(what.equals("Number")){
            radioBtn1.setDisable(true);
            radioBtn2.setDisable(true);
            numOfPlayersOk = true;
            if(!expertOk) confirmBtn.setDisable(false);
            return;
        }
        if(what.equals("Expert")){
            radioBtn3.setDisable(true);
            radioBtn4.setDisable(true);
            expertOk = true;
            if(numOfPlayersOk) confirmBtn.setDisable(false);
        }
    }
}
