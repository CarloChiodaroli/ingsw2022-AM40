package it.polimi.ingsw.model;


public enum TeacherColor {
    YELLOW, PINK, RED, GREEN, BLUE;

private TeacherColor()
{

}

    public <Optional> TeacherColor GetFromInt(int i) {
        switch (i)
        {
            case 0:
                return YELLOW;
            case 1:
                return PINK;
            case 2:
                return RED;
            case 3:
                return GREEN;
            case 4:
                return BLUE;
            default:
                return null;
        }

    }
    }
