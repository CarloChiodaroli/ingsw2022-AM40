package it.polimi.ingsw.model.table;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CloudTest {
    @Test
    public void cloudTest(){
        Bag bag = new Bag(130, 26);
        int totStudents = bag.howManyTotStudents();
        Cloud cloud = new Cloud("c_1", 3);
        assertEquals(cloud.getId(), "c_1");
        cloud.buildClouds(bag);
        assertEquals(bag.howManyTotStudents(), totStudents - 3);
        totStudents -= 3;

        cloud = new Cloud("c_2", 3);
        assertEquals(cloud.getId(), "c_2");
        cloud.buildClouds(bag);
        assertEquals(bag.howManyTotStudents(), totStudents - 3);
        totStudents -= 3;

        assertEquals(bag.howManyTotStudents(), totStudents);

        bag = new Bag(130, 26);
        totStudents = bag.howManyTotStudents();

        cloud = new Cloud("c_1", 4);
        assertEquals(cloud.getId(), "c_1");
        cloud.buildClouds(bag);
        assertEquals(bag.howManyTotStudents(), totStudents - 4);
        totStudents -= 4;

        cloud = new Cloud("c_2", 4);
        assertEquals(cloud.getId(), "c_2");
        cloud.buildClouds(bag);
        assertEquals(bag.howManyTotStudents(), totStudents - 4);
        totStudents -= 4;

        cloud = new Cloud("c_3", 3);
        assertEquals(cloud.getId(), "c_3");
        cloud.buildClouds(bag);
        assertEquals(bag.howManyTotStudents(), totStudents - 3);
        totStudents -= 3;

        cloud = new Cloud("c_3", 4);
        assertEquals(cloud.getId(), "c_3");
        cloud.buildClouds(bag);
        assertEquals(bag.howManyTotStudents(), totStudents - 4);
        totStudents -= 4;

        assertEquals(bag.howManyTotStudents(), totStudents);

    }
}
