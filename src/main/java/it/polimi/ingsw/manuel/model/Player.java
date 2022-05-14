package it.polimi.ingsw.manuel.model;

import java.util.Objects;
import java.util.Observable;

// useless
/*@Deprecated
public class Player extends Observable{
    private final String nickname;
    private PlayerState state;

    @Deprecated
    public Player(String nickname) {
        this.nickname = nickname;
    }

    @Deprecated
    public String getNickname() {
        return nickname;
    }

    @Deprecated
    public void setState(PlayerState state) {
        this.state = state;
    }

    @Deprecated
    public PlayerState getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return nickname.equals(player.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }

}
*/