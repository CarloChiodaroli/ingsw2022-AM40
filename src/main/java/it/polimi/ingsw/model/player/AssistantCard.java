package it.polimi.ingsw.model.player;

import java.util.Objects;

/**
 * Represents the so-called "Assistant Cards" in the game
 */

public class AssistantCard implements Comparable<AssistantCard> {

    private final int weight;
    private final int numOfMotherNatureMovements;

    /**
     * Class Constructor
     *
     * @param weight is the weight value written on the card
     */
    public AssistantCard(int weight) {
        this.weight = weight;
        this.numOfMotherNatureMovements = (int) ((weight + 1) / 2F);
    }

    /**
     * Getter of the weight value
     *
     * @return the weight value
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Getter of the hop count
     *
     * @return the hop count
     */
    public int getNumOfMotherNatureMovements() {
        return numOfMotherNatureMovements;
    }

    @Override
    public int compareTo(AssistantCard o) {
        return this.weight - o.weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssistantCard that = (AssistantCard) o;
        return weight == that.weight && numOfMotherNatureMovements == that.numOfMotherNatureMovements;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, numOfMotherNatureMovements);
    }

    @Override
    public String toString() {
        return "AssistantCard{" +
                "weight=" + weight +
                ", numOfMotherNatureMovements=" + numOfMotherNatureMovements +
                '}';
    }
}
