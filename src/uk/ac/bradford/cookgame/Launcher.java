package uk.ac.bradford.cookgame;

import java.awt.EventQueue;

/**
 * This class is the entry point for the project, containing the main method
 * that starts a game. It creates instances of the different classes of this
 * project and connects them appropriately.
 *
 * @author prtrundl
 */
public class Launcher {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            /**
             * The run method starts the game in a separate thread. It creates
             * the GUI, the engine and the input handler classes and connects
             * those that call other objects.
             */
            @Override
            public void run() {
                GameGUI gui = new GameGUI();            //create GUI
                gui.setVisible(true);                   //display GUI
                GameEngine eng = new GameEngine(gui);   //create engine
                InputHandler i = new InputHandler(eng); //create input handler
                gui.registerKeyHandler(i);              //registers handler with GUI
                eng.startGame();                        //starts the game
            }
        });
    }

}
