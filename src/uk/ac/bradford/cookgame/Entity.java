package uk.ac.bradford.cookgame;

/**
 * The Entity class stores basic state information for both the Player and
 * Customer object types in the game.
 *
 * @author prtrundl
 */
public abstract class Entity {

    /**
     * xPos is the current x position in the game for this entity. Together with
     * the yPos attribute this determines the position of this Entity when it is
     * drawn to the screen using the style X,Y. 0,0 is the top left tile in the
     * level. 1,0 is the tile to the right of 0,0. 0,1 is the tile below 0,0
     * etc.
     */
    private int xPos;

    /**
     * yPos is the current y position in the game for this entity. Together with
     * the xPos attribute this sets the position of this Entity when it is drawn
     * to the screen using the style X,Y. 0,0 is the top left tile in the level.
     * 1,0 is the tile to the right of 0,0. 0,1 is the tile below 0,0.
     */
    private int yPos;

    /**
     * This method returns the current X position for this Entity in the game
     *
     * @return The X co-ordinate of this Entity in the game
     */
    public int getX() {
        return xPos;
    }

    /**
     * This method returns the current Y position for this Entity in the game
     *
     * @return The Y co-ordinate of this Entity in the game
     */
    public int getY() {
        return yPos;
    }

    /**
     * Sets the position of the Entity in the game
     *
     * @param x The new X position for this Entity
     * @param y The new Y position for this Entity
     */
    public void setPosition(int x, int y) {
        xPos = x;
        yPos = y;
    }

}
