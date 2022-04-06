package it.polimi.ingsw.model.table;

import java.util.Optional;

public class MotherNature {

    private static MotherNature istance = null;
    private Island island=null;
    /**
     * CLASS MOTHERNATURE
     * Design pattern : Singleton
     */
    private MotherNature()
    {
    }

    public static MotherNature getMotherNature()
    {
        if(istance==null)
            istance=new MotherNature();
        return istance;
    }

    /**
     *
     * @return Island Mothernature's Island
     */
    public Optional<Island> getPosition()
    {
        return Optional.ofNullable(island);
    }
    /**
     *
     * set Mothernature's Island
     */
    public void setPosition(Island island)
    {
        this.island=island;
    }

    public void resetPosition(){
        this.island = null;
    }
}
