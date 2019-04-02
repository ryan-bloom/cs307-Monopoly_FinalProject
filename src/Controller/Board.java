package Controller;

import Model.Space;

import java.security.InvalidParameterException;

/**
 * This interface holds the game board, which is made up of spaces
 */
public interface Board {
    /**
     * Returns the specific space at the location specified
     * Used when a player rolls the dice and determins his/her new location on the board
     * @param location
     * @return Space
     * @throws IndexOutOfBoundsException
     */
    public Space getSpaceAt(int location) throws IndexOutOfBoundsException;

    /**
     * Given a Space (such as Boarwalk), this returns the location of that space on the board
     * Used when a user draws a card that says "Move to this space"
     * @param space
     * @return integer (or index) of location of the Space parameter
     * @throws InvalidParameterException
     */
    public int getLocationOfSpace(Space space) throws InvalidParameterException;
}