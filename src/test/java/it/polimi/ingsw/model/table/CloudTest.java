package it.polimi.ingsw.model.table;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CloudTest {
    @Test
    public void idCloudTest(){
        Cloud cloud;
        for(int i = 3; i <= 4; i++) {
            cloud = new Cloud("c_1", i);
            assertEquals("c_1", cloud.getId());
        }

        for(int i = 3; i <= 4; i++) {
            cloud = new Cloud("c_2", i);
            assertEquals("c_2", cloud.getId());
        }

        cloud = new Cloud("c_3", 4);
        assertEquals("c_3", cloud.getId());

    }

    @Test
    public void cloudTest(){
        Bag bag = new Bag(130, 26);
        int totStudents = bag.howManyTotStudents();
        Cloud cloud = new Cloud("c_1", 3);
        cloud.buildClouds(bag);
        assertEquals(totStudents - 3, bag.howManyTotStudents());
        totStudents -= 3;

        cloud = new Cloud("c_2", 3);
        cloud.buildClouds(bag);
        assertEquals(totStudents - 3, bag.howManyTotStudents());
        totStudents -= 3;

        assertEquals(totStudents, bag.howManyTotStudents());

        bag = new Bag(130, 26);
        totStudents = bag.howManyTotStudents();

        cloud = new Cloud("c_1", 4);
        cloud.buildClouds(bag);
        assertEquals(totStudents - 4, bag.howManyTotStudents());
        totStudents -= 4;

        cloud = new Cloud("c_2", 4);
        cloud.buildClouds(bag);
        assertEquals(totStudents - 4, bag.howManyTotStudents());
        totStudents -= 4;

        cloud = new Cloud("c_3", 4);
        cloud.buildClouds(bag);
        assertEquals(totStudents - 4, bag.howManyTotStudents());
        totStudents -= 4;

        assertEquals(bag.howManyTotStudents(), totStudents);

    }
}
