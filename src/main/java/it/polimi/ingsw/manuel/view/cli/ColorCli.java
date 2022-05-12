package it.polimi.ingsw.manuel.view.cli;

/**
 * This class contains all colors used in Cli.
 */
public enum ColorCli {
    CLEAR("\033[H\033[2J");    // CYAN

    private final String code;

    ColorCli(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}