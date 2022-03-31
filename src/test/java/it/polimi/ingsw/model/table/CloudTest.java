package it.polimi.ingsw.model.table;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CloudTest {
    @Test
    public void cloudTest(){
        Cloud cloud = new Cloud("c_1", 3);
        assertEquals(cloud.getId(), "c_1");
        cloud = new Cloud("c_1", 4);
        assertEquals(cloud.getId(), "c_1");
        cloud = new Cloud("c_2", 3);
        assertEquals(cloud.getId(), "c_2");
        cloud = new Cloud("c_2", 4);
        assertEquals(cloud.getId(), "c_2");
        cloud = new Cloud("c_3", 3);
        assertEquals(cloud.getId(), "c_3");
        cloud = new Cloud("c_3", 4);
        assertEquals(cloud.getId(), "c_3");

    }
}
