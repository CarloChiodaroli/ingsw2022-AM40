package it.polimi.ingsw.client.view.gui.scene;

/**
 * Represents the generic Scene controller.
 */
public interface GenericSceneController {

    /**
     * Gets the confirmation from the server, and consequently updates the view.
     *
     * @param what represents what has been confirmed.
     */
    void onConfirm(String what);
}
