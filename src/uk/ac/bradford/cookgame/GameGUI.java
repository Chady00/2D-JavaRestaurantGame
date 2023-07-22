package uk.ac.bradford.cookgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import uk.ac.bradford.cookgame.GameEngine.TileType;

/**
 * The GameGUI class is responsible for rendering graphics to the screen to
 * display the game level, player and customers. The GameGUI class passes
 * keyboard events to a registered GameInputHandler to be handled.
 *
 * @author prtrundl
 */
public class GameGUI extends JFrame {

    /**
     * The three final int attributes below set the size of some graphical
     * elements, specifically the display height and width of tiles in the game
     * and the height of patience/stamina bars for Entity objects in the game.
     * Tile sizes must match the size of the image files used in the game.
     */
    public static final int TILE_WIDTH = 64;
    public static final int TILE_HEIGHT = 64;
    public static final int BAR_HEIGHT = 5;

    /**
     * The canvas is the area that graphics are drawn to. It is an internal
     * class of the GameGUI class.
     */
    Canvas canvas;

    /**
     * Constructor for the GameGUI class. It calls the initGUI method to
     * generate the required objects for display.
     */
    public GameGUI() {
        initGUI();
    }

    /**
     * Registers an object to be passed keyboard events captured by the GUI.
     *
     * @param i the GameInputHandler object that will process keyboard events to
     * make the game respond to input
     */
    public void registerKeyHandler(InputHandler i) {
        addKeyListener(i);
    }

    /**
     * Method to create and initialise components for displaying elements of the
     * game on the screen.
     */
    private void initGUI() {
        add(canvas = new Canvas());     //adds canvas to this frame
        setTitle("BowlDown");
        setSize(1166, 614);
        setLocationRelativeTo(null);        //sets position of frame on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Method to update the graphical elements on the screen, usually after
     * player and/or customers have moved when a keyboard event was handled. The
     * method requires three arguments and displays corresponding information on
     * the screen.
     *
     * @param tiles A 2-dimensional array of TileTypes. This is the tiles of the
     * current game level that should be drawn to the screen.
     * @param player An Player object. This object is used to draw the player in
     * the right tile and display its stamina. null can be passed for this
     * argument, in which case no player will be drawn.
     * @param customers An array of Customer objects that is processed to draw
     * customers with a patience bar in tiles. null can be passed for this
     * argument in which case no customers will be drawn. Elements in the
     * customers array can also be null, in which case nothing will be drawn for
     * that array element.
     */
    public void updateDisplay(TileType[][] tiles, Player player, Customer[] customers) {
        canvas.update(tiles, player, customers);
    }
}

/**
 * Internal class used to draw elements within a JPanel. The Canvas class loads
 * images from an asset folder inside the main project folder.
 *
 * @author prtrundl
 */
class Canvas extends JPanel {

    private BufferedImage floor1;
    private BufferedImage floor2;
    private BufferedImage wall;
    private BufferedImage player;
    private BufferedImage playerfood1;
    private BufferedImage playerfood2;
    private BufferedImage playerfood3;
    private BufferedImage customer1;
    private BufferedImage customer2;
    private BufferedImage customer3;
    private BufferedImage vipcustomer;
    private BufferedImage door;
    private BufferedImage food1;
    private BufferedImage food2;
    private BufferedImage food3;
    private BufferedImage table;
    private BufferedImage trash;
    private Image hammer;
    private BufferedImage brokenWall1;
    private BufferedImage brokenWall2;
    private BufferedImage playerHammer;

    TileType[][] currentTiles;  //the current 2D array of tiles to display
    Player currentPlayer;       //the current player object to be drawn
    Customer[] currentCustomers;   //the current array of customers to draw

    /**
     * Constructor that loads tile images for use in this class
     */
    public Canvas() {
        loadTileImages();
    }

    /**
     * Loads tiles images from a fixed folder location within the project
     * directory
     */
    private void loadTileImages() {
        try {
            floor1 = ImageIO.read(new File("assets/tiles.png"));
            assert floor1.getHeight() == GameGUI.TILE_HEIGHT
                    && floor1.getWidth() == GameGUI.TILE_WIDTH;
            floor2 = ImageIO.read(new File("assets/floor2.png"));
            assert floor2.getHeight() == GameGUI.TILE_HEIGHT
                    && floor2.getWidth() == GameGUI.TILE_WIDTH;
            wall = ImageIO.read(new File("assets/wall.png"));
            assert wall.getHeight() == GameGUI.TILE_HEIGHT
                    && wall.getWidth() == GameGUI.TILE_WIDTH;
            player = ImageIO.read(new File("assets/player.png"));
            assert player.getHeight() == GameGUI.TILE_HEIGHT
                    && player.getWidth() == GameGUI.TILE_WIDTH;
            playerfood1 = ImageIO.read(new File("assets/playerfood.png"));
            assert playerfood1.getHeight() == GameGUI.TILE_HEIGHT
                    && playerfood1.getWidth() == GameGUI.TILE_WIDTH;
            playerfood2 = ImageIO.read(new File("assets/playerfood2.png"));
            assert playerfood2.getHeight() == GameGUI.TILE_HEIGHT
                    && playerfood2.getWidth() == GameGUI.TILE_WIDTH;
            playerfood3 = ImageIO.read(new File("assets/playerfood3.png"));
            assert playerfood3.getHeight() == GameGUI.TILE_HEIGHT
                    && playerfood3.getWidth() == GameGUI.TILE_WIDTH;
            customer1 = ImageIO.read(new File("assets/customer.png"));
            assert customer1.getHeight() == GameGUI.TILE_HEIGHT
                    && customer1.getWidth() == GameGUI.TILE_WIDTH;
            customer2 = ImageIO.read(new File("assets/customer2.png"));
            assert customer2.getHeight() == GameGUI.TILE_HEIGHT
                    && customer2.getWidth() == GameGUI.TILE_WIDTH;
            customer3 = ImageIO.read(new File("assets/customer3.png"));
            assert customer3.getHeight() == GameGUI.TILE_HEIGHT
                    && customer3.getWidth() == GameGUI.TILE_WIDTH;
            vipcustomer = ImageIO.read(new File("assets/vipcustomer.png"));
            assert vipcustomer.getHeight() == GameGUI.TILE_HEIGHT
                    && vipcustomer.getWidth() == GameGUI.TILE_WIDTH;
            food1 = ImageIO.read(new File("assets/food.png"));
            assert food1.getHeight() == GameGUI.TILE_HEIGHT
                    && food1.getWidth() == GameGUI.TILE_WIDTH;
            food2 = ImageIO.read(new File("assets/food2.png"));
            assert food2.getHeight() == GameGUI.TILE_HEIGHT
                    && food2.getWidth() == GameGUI.TILE_WIDTH;
            food3 = ImageIO.read(new File("assets/food3.png"));
            assert food3.getHeight() == GameGUI.TILE_HEIGHT
                    && food3.getWidth() == GameGUI.TILE_WIDTH;
            door = ImageIO.read(new File("assets/door.png"));
            assert door.getHeight() == GameGUI.TILE_HEIGHT
                    && door.getWidth() == GameGUI.TILE_WIDTH;
            table = ImageIO.read(new File("assets/table.png"));
            assert table.getHeight() == GameGUI.TILE_HEIGHT
                    && table.getWidth() == GameGUI.TILE_WIDTH;
            trash = ImageIO.read(new File("assets/trash.png"));
            assert trash.getHeight() == GameGUI.TILE_HEIGHT
                    && trash.getWidth() == GameGUI.TILE_WIDTH;
            hammer = new ImageIcon("assets/test.gif").getImage();
            assert hammer.getHeight(null) == GameGUI.TILE_HEIGHT
                    && hammer.getWidth(null) == GameGUI.TILE_WIDTH;
            brokenWall1 = ImageIO.read(new File("assets/brokenWall1.png"));
            assert brokenWall1.getHeight() == GameGUI.TILE_HEIGHT
                    && brokenWall1.getWidth() == GameGUI.TILE_WIDTH;
            brokenWall2 = ImageIO.read(new File("assets/brokenWall2.png"));
            assert brokenWall2.getHeight() == GameGUI.TILE_HEIGHT
                    && brokenWall2.getWidth() == GameGUI.TILE_WIDTH;
            playerHammer=ImageIO.read(new File("assets/playerHammer.png"));
            assert playerHammer.getHeight() == GameGUI.TILE_HEIGHT
                    && playerHammer.getWidth() == GameGUI.TILE_WIDTH;
        } catch (IOException e) {
            System.out.println("Exception loading images: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    /**
     * Updates the current graphics on the screen to display the tiles, player
     * and customers
     *
     * @param t The 2D array of TileTypes representing the current level of the
     * game
     * @param player The current player object, used to draw the player and its
     * stamina
     * @param customers The array of customers to display on the level with
     * their patience bar
     */
    public void update(TileType[][] t, Player player, Customer[] customers) {
        currentTiles = t;
        currentPlayer = player;
        currentCustomers = customers;
        repaint();
    }

    /**
     * Override of method in super class, it draws the custom elements for this
     * game such as the tiles, player and customers.
     *
     * @param g Graphics drawing object
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLevel(g);
    }

    /**
     * Draws graphical elements to the screen to display the current game level
     * tiles, the player and the customers. If the currentTiles, currentPlayer
     * or currentCustomers objects are null they will not be drawn.
     *
     * @param g
     */
    private void drawLevel(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (currentTiles != null) {
            for (int i = 0; i < currentTiles.length; i++) {
                for (int j = 0; j < currentTiles[i].length; j++) {
                    switch (currentTiles[i][j]) {
                        case FLOOR1:
                            g2.drawImage(floor1, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case FLOOR2:
                            g2.drawImage(floor2, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case WALL:
                            g2.drawImage(floor1, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            g2.drawImage(wall, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case FOOD1:
                            g2.drawImage(floor1, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            g2.drawImage(food1, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case FOOD2:
                            g2.drawImage(floor1, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            g2.drawImage(food2, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case FOOD3:
                            g2.drawImage(floor1, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            g2.drawImage(food3, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case DOOR:
                            g2.drawImage(floor1, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            g2.drawImage(door, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case TABLE:
                            g2.drawImage(floor1, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            g2.drawImage(table, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case TRASH:
                            g2.drawImage(floor1, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            g2.drawImage(trash, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case brokenWall1:
                            g2.drawImage(brokenWall1, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            g2.drawImage(hammer, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case brokenWall2:
                            g2.drawImage(brokenWall2, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            g2.drawImage(hammer, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                    }
                }
            }
        }
        if (currentCustomers != null) {
            Image customerImage = customer1;
            for (Customer cust : currentCustomers) {
                if (cust != null) {
                    switch (cust.getFoodWanted()) {
                        case 2:
                            customerImage = customer2;
                            break;
                        case 3:
                            customerImage = customer3;
                            break;
                        case 4:
                            customerImage = vipcustomer;
                            break;
                        default:
                            customerImage = customer1;
                            break;
                    }
                    g2.drawImage(customerImage, cust.getX() * GameGUI.TILE_WIDTH, cust.getY() * GameGUI.TILE_HEIGHT, null);
                    g2.setColor(Color.BLUE);
                    Font font = new Font("Arial", Font.BOLD, 15);
                    g2.setFont(font);
                    g2.drawString("Score: " + GameEngine.score, 30, 30);
                    drawHealthBar(g2, cust);
                }
            }
        }
        if (currentPlayer != null) {
            Image playerImage = null;

            switch (currentPlayer.getCarriedFoodType()) {
                case 0:
                    playerImage = player;
                    break;
                case 1:
                    playerImage = playerfood1;
                    break;
                case 2:
                    playerImage = playerfood2;
                    break;
                case 3:
                    playerImage = playerfood3;
                    break;
            }
            if(currentPlayer.isbreaking()){
                playerImage = playerHammer;
            }
            g2.drawImage(playerImage, currentPlayer.getX() * GameGUI.TILE_WIDTH, currentPlayer.getY() * GameGUI.TILE_HEIGHT, null);
            drawEnergyBar(g2, currentPlayer);
        }
        g2.dispose();
    }

    /**
     * Draws a patience bar for the given Customer at the bottom of the tile
     * that the Customer is located in.
     *
     * @param g2 The graphics object to use for drawing
     * @param g The customer that the patience bar will be drawn for
     */
    private void drawHealthBar(Graphics2D g2, Customer g) {
        double remainingPatience = (double) g.getPatience() / (double) g.getMaxPatience();
        int barY = g.getY() * GameGUI.TILE_HEIGHT + 60; // Adjust the Y position

        g2.setColor(Color.RED);
        g2.fill(new Rectangle2D.Double(g.getX() * GameGUI.TILE_WIDTH, barY, GameGUI.TILE_WIDTH, GameGUI.BAR_HEIGHT));
        g2.setColor(Color.GREEN);
        g2.fill(new Rectangle2D.Double(g.getX() * GameGUI.TILE_WIDTH, barY, GameGUI.TILE_WIDTH * remainingPatience, GameGUI.BAR_HEIGHT));
    }

    /**
     * Draws an energy bar for the given Player at the bottom of the tile that
     * the Player is located in.
     *
     * @param g2 The graphics object to use for drawing
     * @param p The Player that the energy bar will be drawn for
     */
    private void drawEnergyBar(Graphics2D g2, Player p) {
        double remainingStamina = (double) p.getStamina() / (double) p.getMaxStamina();
        int barY = p.getY() * GameGUI.TILE_HEIGHT + 55; // Adjust the Y position

        g2.setColor(Color.BLUE);
        g2.fill(new Rectangle2D.Double(p.getX() * GameGUI.TILE_WIDTH, barY, GameGUI.TILE_WIDTH, GameGUI.BAR_HEIGHT));
        g2.setColor(Color.CYAN);
        g2.fill(new Rectangle2D.Double(p.getX() * GameGUI.TILE_WIDTH, barY, GameGUI.TILE_WIDTH * remainingStamina, GameGUI.BAR_HEIGHT));
    }
}
