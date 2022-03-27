package it.polimi.ingsw.model.table;

public class MotherNature {

    private static MotherNature istance = null;
    private Island island;

    private MotherNature()
    {
    }

    public static MotherNature getMotherNature()
    {
        if(istance==null)
            istance=new MotherNature();
        return istance;
    }

    public Island getPosition()
    {
        return this.island;
    }

    public void setPosition(Island island)
    {
        this.island=island;
    }
}
