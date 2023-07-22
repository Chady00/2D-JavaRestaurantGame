package uk.ac.bradford.cookgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class handles keyboard events (key presses) captured by a GameGUI object
 * that are passed to an instance of this class. The class is responsible for
 * calling methods in the GameEngine class that will update tiles, players and
 * customers for the various keystrokes that are handled.
 *
 * @author prtrundl
 */
public class InputHandler implements KeyListener {

    GameEngine engine;      //GameEngine that this class calls methods from

    /**
     * Constructor that forms a connection between a GameInputHandler object and
     * a GameEngine object. The GameEngine object registered here is the one
     * that will have methods called to change player and customer positions
     * etc.
     *
     * @param eng The GameEngine object that this GameInputHandler is linked to
     */
    public InputHandler(GameEngine eng) {
        engine = eng;
    }

    /**
     * Unused method
     *
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Method to handle key presses captured by the GameGUI. The method calls
     * the game engine doTurn method to process a game turn for ANY key press,
     * but if the up, down, left or right arrow keys are pressed it also calls a
     * method in the engine to update the game by moving the player.
     *
     * @param e A KeyEvent object generated when a keyboard key is pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                engine.movePlayer('L');
                break;  //handle left arrow key
            case KeyEvent.VK_RIGHT:
                engine.movePlayer('R');
                break;//handle right arrow
            case KeyEvent.VK_UP:
                engine.movePlayer('U');
                break;      //handle up arrow
            case KeyEvent.VK_DOWN:
                engine.movePlayer('D');
                break;  //handle down arrow
        }
        engine.doTurn();    //any key press will result in this method being called
    }

    /**
     * Unused method
     *
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

}
