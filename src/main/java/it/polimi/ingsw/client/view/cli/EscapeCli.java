package it.polimi.ingsw.client.view.cli;

/**
 * This enum contains all Escape characters used in the cli.
 */
public enum EscapeCli {

    /**
     * Escape sequence that clears the view.
     */
    CLEAR("\033[2J"),
    /**
     * Escape sequence that represents the blue color.
     */
    BLUE("\u001B[34m"),
    /**
     * Escape sequence that represents the pink color.
     */
    PINK("\u001B[95m"),
    /**
     * Escape sequence that represents the yellow color.
     */
    YELLOW("\u001B[93m"),
    /**
     * Escape sequence that represents the red color.
     */
    RED("\u001B[91m"),
    /**
     * Escape sequence that represents the green color.
     */
    GREEN("\u001B[92m"),
    /**
     * Escape sequence that represents the default color.
     */
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