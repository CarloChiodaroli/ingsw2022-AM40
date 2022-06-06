package it.polimi.ingsw.client.view.cli;

/**
 * This enum contains all Escape characters used in the cli.
 */
public enum EscapeCli {

    CLEAR("\033[2J"),
    BLUE("\u001B[34m"),
    PINK("\u001B[95m"),
    YELLOW("\u001B[93m"),
    RED("\u001B[91m"),
    GREEN("\u001B[92m"),
    DEFAULT("\u001B[0m");

    private final String code;

    /**
     * Constructor
     *
     * @param code
     */
    EscapeCli(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}