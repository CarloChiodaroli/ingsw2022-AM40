package it.polimi.ingsw.model.table;
import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.TeacherColor;

import java.util.Arrays;

public class Bag extends StudentsManager {

    public Bag()
        {
            super(130,26);
        }


    public TeacherColor getAStudent()
    {
        int casualnumber;
        casualnumber= (int)(Math.random()*5);
        return TeacherColor.values()[casualnumber];
    }

}
