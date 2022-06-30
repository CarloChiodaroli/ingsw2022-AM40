package it.polimi.ingsw.server.model.table;

import java.util.Optional;

/**
 * Models game's mother nature wood peace with a singleton pattern.
 */
public class MotherNature {

    private static MotherNature instance = null;
    private Island island = null;

    /**
     * Class constructor
     */
    private MotherNature() {
    }

    /**
     * If mother nature hadn't been instantiated, istance it
     *
     * @return mother nature
     */
    public static MotherNature getMotherNature() {
        if (instance == null)
            instance = new MotherNature();
        return instance;
    }

    /**
     * Get the position of mother nature
     *
     * @return the island where mother nature is found
     */
    public Optional<Island> getPosition() {
        return Optional.ofNullable(island);
    }

    /**
     * Set the position of mother nature
     */
    public void setPosition(Island island) {
        this.island = island;
    }

    /**
     * Reset the position of mother nature
     */
    public void resetPosition() {
        this.island = null;
    }
}
