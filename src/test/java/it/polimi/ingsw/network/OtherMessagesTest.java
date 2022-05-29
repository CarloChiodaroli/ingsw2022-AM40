package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.message.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OtherMessagesTest {

    private GsonBuilder builder;
    private Gson gson;

    @BeforeEach
    public void initTest(){
        builder = new GsonBuilder();
        gson = builder.create();
    }

    @Test
    public void pingTest(){
        PingMessage pingMessage = new PingMessage("Server");
        String gsonSerialization = gson.toJson(pingMessage);

        Message message = gson.fromJson(gsonSerialization, Message.class);
        assertEquals(MessageType.PING, message.getMessageType());

        PingMessage arrived = gson.fromJson(gsonSerialization, PingMessage.class);
    }

    @Test
    public void loginTest(){
        String requester = "Aldo";

        LoginMessage loginMessage = new LoginMessage(requester);
        String gsonSerialization = gson.toJson(loginMessage);

        Message message = gson.fromJson(gsonSerialization, Message.class);
        assertEquals(MessageType.LOGIN, message.getMessageType());

        LoginMessage arrived = gson.fromJson(gsonSerialization, LoginMessage.class);

        assertFalse(arrived.isConnectionSuccessful());
        assertFalse(arrived.isNicknameAccepted());
        assertTrue(arrived.isRequest());
        assertEquals(requester, arrived.getSenderName());
        assertDoesNotThrow(arrived::readRequest);
        assertThrows(IllegalStateException.class, arrived::readReply);

        loginMessage = new LoginMessage("Server", true, true);
        gsonSerialization = gson.toJson(loginMessage);

        message = gson.fromJson(gsonSerialization, Message.class);
        assertEquals(MessageType.LOGIN, message.getMessageType());

        arrived = gson.fromJson(gsonSerialization, LoginMessage.class);

        assertTrue(arrived.isConnectionSuccessful());
        assertTrue(arrived.isNicknameAccepted());
        assertFalse(arrived.isRequest());
        assertEquals("Server", arrived.getSenderName());
        assertDoesNotThrow(arrived::readReply);
        assertThrows(IllegalStateException.class, arrived::readRequest);
    }



}
