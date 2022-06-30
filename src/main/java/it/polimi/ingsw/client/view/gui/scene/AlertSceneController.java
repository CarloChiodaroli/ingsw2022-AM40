package it.polimi.ingsw.client.view.gui.scene;

import it.polimi.ingsw.client.view.gui.SceneController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class implements the controller of a generic Alert Scene.
 */
public class AlertSceneController implements GenericSceneController {

    private final Stage stage;

    private double xOffset;
    private double yOffset;

    @FXML
    private BorderPane rootPane;
    @FXML
    private Label titleLbl;
    @FXML
    private Label messageLbl;
    @FXML
    private Button okBtn;

    /**
     * Default constructor
     */
    public AlertSceneController() {
        stage = new Stage();
        stage.initOwner(SceneController.getActiveScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
        xOffset = 0;
        yOffset = 0;
    }

    /**
     * FXML's initialize method
     */
    @FXML
    public void initialize() {
        rootPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootPaneMousePressed);
        rootPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootPaneMouseDragged);
        okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onOkBtnClick);
    }

    /**
     * Handles the mouse pressed event preparing the coordinates for dragging the window
     *
     * @param event mouse pressed
     */
    private void onRootPaneMousePressed(MouseEvent event) {
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    /**
     * Handles the mouse dragged event by moving the window around the screen
     *
     * @param event mouse dragged
     */
    private void onRootPaneMouseDragged(MouseEvent event) {
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    /**
     * Handles the click on the Ok button
     * An alert message will be shown
     *
     * @param event mouse click
     */
    private void onOkBtnClick(MouseEvent event) {
        stage.close();
    }

    /**
     * Sets the title of the Alert Scene
     *
     * @param str title
     */
    public void setAlertTitle(String str) {
        titleLbl.setText(str);
    }

    /**
     * Sets the message of the Alert Scene
     *
     * @param str message
     */
    public void setAlertMessage(String str) {
        messageLbl.setText(str);
    }

    /**
     * Displays Alert Message Pop-Up
     */
    public void displayAlert() {
        stage.showAndWait();
    }

    /**
     * Sets the scene of the stage
     *
     * @param scene scene
     */
    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfirm(String what) {

    }
}
