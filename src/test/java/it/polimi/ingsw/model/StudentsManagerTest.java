package it.polimi.ingsw.model;

import it.polimi.ingsw.model.school.SchoolDashboard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentsManagerTest {
    @Test
    public void addStudentTest(){
        StudentsManagerIstance manager = new StudentsManagerIstance(130, 26);
    }
}


class StudentsManagerIstance extends StudentsManager{
    StudentsManagerIstance(int num, int numc){
        super(num, numc);
    }
}
