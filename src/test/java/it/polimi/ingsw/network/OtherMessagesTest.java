package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.commons.message.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OtherMessagesTest {

    private GsonBuilder builder;
    private Gson gson;

    @BeforeEach
    public void initTest(){
        builder = new GsonBuilder().setPrettyPrinting();
        gson = builder.create();
    }

    @Test
    public void pingTest(){
        PingMessage pingMessage = new PingMessage("Server");
        String gsonSerialization = gson.toJson(pingMessage);

        System.out.println(gsonSerialization);

        Message message = gson.fromJson(gsonSerialization, Message.class);
        assertEquals(MessageType.PING, message.getMessageType());

        PingMessage arrived = gson.fromJson(gsonSerialization, PingMessage.class);
    }

    public void networkSimulationTest(){
        PingMessage pingMessage = new PingMessage("Server");
        String gsonSerialization = gson.toJson(pingMessage);
        System.out.println(gsonSerialization);

        Message message = gson.fromJson(gsonSerialization, Message.class);
        assertEquals(MessageType.PING, message.getMessageType());

        PingMessage arrived = gson.fromJson(gsonSerialization, PingMessage.class);
    }


}
