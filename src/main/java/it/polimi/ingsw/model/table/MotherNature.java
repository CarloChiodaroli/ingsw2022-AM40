package it.polimi.ingsw.model.table;

public class MotherNature {

    private static MotherNature istance = null;
    private Island island;
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
    public Island getPosition()
    {
        return this.island;
    }
    /**
     *
     * set Mothernature's Island
     */
    public void setPosition(Island island)
    {
        this.island=island;
    }
}
