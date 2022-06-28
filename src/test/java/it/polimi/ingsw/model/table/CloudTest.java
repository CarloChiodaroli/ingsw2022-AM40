package it.polimi.ingsw.model.table;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.table.Bag;
import it.polimi.ingsw.server.model.table.Cloud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test the {@link Cloud} methods
 */
public class CloudTest {
    /**
     * Test correct generation of clouds id
     */
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

    /**
     * Test correct clouds fill
     */
    @Test
    public void cloudTest(){
        Bag bag = new Bag(130, 26);
        int totStudents = bag.howManyTotStudents();
        Cloud cloud = new Cloud("c_1", 3);
        cloud.buildCloud(bag);
        assertEquals(totStudents - 3, bag.howManyTotStudents());
        totStudents -= 3;

        cloud = new Cloud("c_2", 3);
        cloud.buildCloud(bag);
        assertEquals(totStudents - 3, bag.howManyTotStudents());
        totStudents -= 3;

        assertEquals(totStudents, bag.howManyTotStudents());

        bag = new Bag(130, 26);
        totStudents = bag.howManyTotStudents();

        cloud = new Cloud("c_1", 4);
        cloud.buildCloud(bag);
        assertEquals(totStudents - 4, bag.howManyTotStudents());
        totStudents -= 4;

        cloud = new Cloud("c_2", 4);
        cloud.buildCloud(bag);
        assertEquals(totStudents - 4, bag.howManyTotStudents());
        totStudents -= 4;

        cloud = new Cloud("c_3", 4);
        cloud.buildCloud(bag);
        assertEquals(totStudents - 4, bag.howManyTotStudents());
        totStudents -= 4;

        assertEquals(bag.howManyTotStudents(), totStudents);

    }
}