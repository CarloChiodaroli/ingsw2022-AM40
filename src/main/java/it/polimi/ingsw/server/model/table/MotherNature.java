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

    public static MotherNature getMotherNature() {
        if (instance == null)
            instance = new MotherNature();
        return instance;
    }

    /**
     * @return Island MotherNature's Island
     */
    public Optional<Island> getPosition() {
        return Optional.ofNullable(island);
    }

    /**
     * set MotherNature's Island
     */
    public void setPosition(Island island) {
        this.island = island;
    }

    public void resetPosition() {
        this.island = null;
    }
}
