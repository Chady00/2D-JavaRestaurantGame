package uk.ac.bradford.cookgame;

import java.util.ArrayList;
import java.awt.Point;
import static java.lang.Math.random;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The GameEngine class is responsible for managing information about the game,
 * creating levels, the player and customers, as well as updating information
 * when a key is pressed (processed by the InputHandler) while the game is
 * running.
 *
 * @author prtrundl
 */
public class GameEngine {

    /**
     * An enumeration type to represent different types of tiles that make up a
     * level. Each type has a corresponding image file that is used to draw the
     * correct tile to the screen for each tile in a level. FLOOR tiles are open
     * for movement for players and customers, WALL tiles should block movement
     * into that tile, FOOD tiles allow the player to pick up food, TABLE and
     * DOOR tiles can be used for both decoration and for implementing advanced
     * features.
     */
    public enum TileType {
        WALL, FLOOR1, FLOOR2, FOOD1, FOOD2, FOOD3, TABLE, DOOR,HAMMER,brokenWall1,brokenWall2, TRASH
    }

    /**
     * The width of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int LEVEL_WIDTH = 18;

    /**
     * The height of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int LEVEL_HEIGHT = 9;

    /**
     * A random number generator that can be used to include randomised choices
     * in the creation of levels, in choosing places to place the player and
     * customers, and to randomise movement etc. Passing an integer (e.g. 123)
     * to the constructor called here will give fixed results - the same numbers
     * will be generated every time WHICH CAN BE VERY USEFUL FOR TESTING AND
     * BUGFIXING!
     */
    private Random rng = new Random();

    /**
     * The current level number for the game. As the player completes levels the
     * level number should be increased and can be used to increase the
     * difficulty e.g. by creating additional customers and reducing patience
     * etc.
     */
    private int levelNumber = 1;  //current level

    /**
     * The current turn number. Increased by one every turn. Used to control
     * effects that only occur on certain turn numbers.
     */
    private int turnNumber = 0;
    private int accelerate = 0;
    /**
     * The current score in this game.
     */
    public static int score;

    /**
     * The GUI associated with this GameEngine object. This link allows the
     * engine to pass level and entity information to the GUI to be drawn.
     */
    private GameGUI gui;

    /**
     * The 2 dimensional array of tiles that represent the current level. The
     * size of this array should use the LEVEL_HEIGHT and LEVEL_WIDTH attributes
     * when it is created. This is the array that is used to draw images to the
     * screen by the GUI class.
     */
    private TileType[][] level;

    /**
     * An ArrayList of Point objects used to create and track possible locations
     * to place the player and customers when a new level is created.
     */
    private ArrayList<Point> spawnLocations;

    /**
     * A Player object that is the current player. This object stores the state
     * information for the player, including stamina and the current position
     * (which is a pair of co-ordinates that corresponds to a tile in the
     * current level - see the Entity class for more information on the
     * co-ordinate system used as well as the coursework specification
     * document).
     */
    private Player player;

    /**
     * An array of Customer objects that represents the customers in the current
     * level of the game. Elements in this array should be either an object of
     * the type Customer (meaning that a customer is exists (not fed/cleared
     * yet) and therefore needs to be drawn or moved) or should be null (which
     * means nothing is drawn or processed for movement). null values in this
     * array are skipped during drawing and movement processing. Customers that
     * the player correctly feeds can be replaced with (i.e. assigned) the value
     * null in this array which removes them from the game, using syntax such as
     * customers[i] = null.
     */
    private Customer[] customers;

    /**
     * Constructor that creates a GameEngine object and connects it with a
     * GameGUI object.
     *
     * @param gui The GameGUI object that this engine will pass information to
     * in order to draw levels and entities to the screen.
     */
    public GameEngine(GameGUI gui) {
        this.gui = gui;
    }

    /**
     * Generates a new level. The method builds a 2D array of TileType values
     * that will be used to draw level to the screen and to add a variety of
     * tiles into each level. Tiles can be floors, walls, tables, doors or food
     * sources.
     *
     * @return A 2D array filled with TileType values representing the level in
     * the current game. The size of this array should use the width and height
     * of the game level using the LEVEL_WIDTH and LEVEL_HEIGHT attributes.
     */
    public boolean isContradict(int x, int y) {
        // Check if the player's position contradicts the given coordinates
        if (player != null && player.getX() == x && player.getY() == y) {
            return true; // Player's position contradicts
        }

        // Check if any customer's position contradicts the given coordinates
        if (customers != null) {
            for (Customer customer : customers) {
                if (customer != null && customer.getX() == x && customer.getY() == y) {
                    return true; // Customer's position contradicts
                }
            }
        }
        return false; // No contradiction found
    }

    public TileType[][] generateLevel() {
        System.out.println("Level " + levelNumber);
        System.out.println("Total score: " + score);
        TileType[][] level = new TileType[GameEngine.LEVEL_WIDTH][GameEngine.LEVEL_HEIGHT];
        TileType[] tileTypes = {TileType.WALL, TileType.FLOOR1, TileType.FLOOR2, TileType.FOOD1, TileType.FOOD2, TileType.FOOD3, TileType.TABLE, TileType.DOOR};
        int redAppleCount = 0;
        int greenAppleCount = 0;
        int breadCount = 0;
        int maxRedApples = 20;
        int maxGreenApples = 10;
        int maxBread = 10;
        int foodGenerated = 0;
        int customerCount = (int) Math.floor(levelNumber * 1.5); // Number of customers based on levelNumber
        int nbTables = 0;
        for (int row = 0; row < GameEngine.LEVEL_WIDTH; row++) {
            for (int column = 0; column < GameEngine.LEVEL_HEIGHT; column++) {
                double randomIndex = rng.nextDouble();

                if (randomIndex < 1 - (levelNumber * 0.15)) {
                    randomIndex = 1; // Select FLOOR1 with 95%, 90%, 80% probability based on levelNumber
                } else {
                    randomIndex = rng.nextInt(tileTypes.length); // Randomly select other tile types
                }
                if (randomIndex == 3 || randomIndex == 4 || randomIndex == 5) {
                    foodGenerated++;
                } // check if table 
                else if (randomIndex == 6) {
                    nbTables++;
                }
                // no more food 
                while ((customerCount + 2 <= foodGenerated && (randomIndex == 3 || randomIndex == 4 || randomIndex == 5)) || (randomIndex == 6) || randomIndex==8 ) {
                    randomIndex = rng.nextInt(tileTypes.length);
                }
                level[row][column] = tileTypes[(int) randomIndex];

                if (level[row][column] == TileType.FOOD1 && redAppleCount < maxRedApples && breadCount < customerCount) {
                    redAppleCount++;
                } else if (level[row][column] == TileType.FOOD2 && greenAppleCount < maxGreenApples && breadCount < customerCount) {
                    greenAppleCount++;
                } else if (level[row][column] == TileType.FOOD3 && breadCount < maxBread && breadCount < customerCount) {
                    breadCount++;
                }
            }
        }
        int tablesNeeded = customerCount;
        while (tablesNeeded > 0) {
        int randomRow = rng.nextInt(GameEngine.LEVEL_WIDTH);
        int randomColumn = rng.nextInt(GameEngine.LEVEL_HEIGHT);

        // Check if the randomly selected position is available for placing a table
        if (level[randomRow][randomColumn] == TileType.FLOOR1) {
            level[randomRow][randomColumn] = TileType.TABLE;
            tablesNeeded--;
        }
    }
        

        // assign a trash bin
        level[GameEngine.LEVEL_WIDTH - 1][0] = TileType.TRASH;
        return level;
    }

    /**
     * Generates spawn points for the player and customers. The method processes
     * the level array and finds positions that are suitable for spawning, i.e.
     * empty tiles such as floors. Suitable positions should then be added to
     * the ArrayList that can be retrieved as Point objects - Points are a
     * simple kind of object that contain an X and a Y co-ordinate stored using
     * the int primitive type.
     *
     * @return An ArrayList containing Point objects representing suitable X and
     * Y co-ordinates in the current level where the player or customers can be
     * added into the game.
     */
    private ArrayList<Point> getSpawns() {
        ArrayList<Point> spawnLocations = new ArrayList<>();

        for (int row = 0; row < GameEngine.LEVEL_WIDTH; row++) {
            for (int column = 0; column < GameEngine.LEVEL_HEIGHT; column++) {
                TileType tile = level[row][column];
                if (tile == TileType.FLOOR1 || tile == TileType.FLOOR2) {
                    spawnLocations.add(new Point(row, column));
                }
            }
        }

        return spawnLocations;
    }

    /**
     * Adds customers in suitable locations in the current level. The first
     * version of this method should picked fixed positions for customers by
     * calling the three argument version of the constructor for the Customer
     * class and using fixed values for the patience, X and Y positions of the
     * Customer to be added. Customer objects created this way should be added
     * into an array of Customer objects that is declared, instantiated and
     * filled inside this method. This array should the be returned by this
     * method. Customer objects in the array returned by this method will then
     * be drawn to the screen using the existing code in the GameGUI class.
     *
     * The second version of this method (described in a later task) should call
     * the four argument constructor for Customer (instead of the three argument
     * constructor) and pass an integer with a value of either 1, 2 or 3 for the
     * last argument. This will generate new types of customer that want
     * different types of food.
     *
     * The third version of this method should use the spawnLocations ArrayList
     * to pick suitable positions to add customers, removing these positions
     * from the spawns ArrayList as they are used (using the remove() method) to
     * avoid multiple customers spawning in the same location and to prevent
     * them from being added on top of walls, tables etc. The method then
     * creates customers by instantiating the Customer class, setting patience,
     * and then setting the X and Y position for the customer using the X and Y
     * values from the Point object that was removed from the spawns ArrayList.
     *
     * @return An array of Customer objects representing the customers for the
     * current level of the game
     */
    //// this mod
    private Customer[] addCustomers() {
        Customer[] customers = new Customer[(int) (levelNumber * 1.5)]; // Create an array with a fixed length
        Random random = new Random();

        ArrayList<Point> spawnLocations = getSpawns(); // Retrieve spawn locations

        for (int i = 0; i < customers.length; i++) {
            int maxPatience = 150 - (levelNumber * 2); // Set the desired max patience value for each customer

            Point position = spawnLocations.remove(random.nextInt(spawnLocations.size())); // Remove and get a random spawn location
            int randFoodType = getRandomFoodType(random);
            // if vip customer reduce the maxPatience
            if (randFoodType == 4) {
                maxPatience -= 50;
            }
            customers[i] = new Customer(maxPatience, position.x, position.y, randFoodType); // Create a new Customer object with the desired position and food type and add it to the array
        }

        return customers;
    }

    private ArrayList<Point> getAvailablePositions() {
        ArrayList<Point> availablePositions = new ArrayList<>();

        for (int row = 0; row < GameEngine.LEVEL_WIDTH; row++) {
            for (int column = 0; column < GameEngine.LEVEL_HEIGHT; column++) {
                if (level[row][column] == TileType.FLOOR1) {
                    Point position = new Point(row, column);
                    if (!isContradict(position.x, position.y)) {
                        availablePositions.add(position);
                    }
                }
            }
        }

        return availablePositions;
    }

    private Point getRandomPosition(ArrayList<Point> positions, Random random) {
        return spawnLocations.remove(random.nextInt(spawnLocations.size()));

    }

    private int getRandomFoodType(Random random) {
        return random.nextInt(4) + 1; // Generate a random food type (1, 2,  3, or 4)
    }

    /**
     * Creates a Player object in the game. The method instantiates the Player
     * class and assigns values for the energy and position.
     *
     * The first version of this method should use fixed a fixed position for
     * the player to start, by setting fixed X and Y values when calling the
     * constructor in the Player class.
     *
     * The second version of this method should use the spawns ArrayList to
     * select a suitable location to spawn the player and removes the Point from
     * the spawns ArrayList. This will prevent the Player from being added to
     * the game inside a wall, bank or breach for example.
     *
     * @return A Player object representing the player in the game
     */
    private Player createPlayer() {
        int maxStamina = 600; // Set the desired max stamina value

        Point position = spawnLocations.remove(rng.nextInt(spawnLocations.size())); // Remove and get a random spawn location

        return new Player(maxStamina, position.x, position.y);
    }

    /**
     * Handles the movement of the player when attempting to move in the game.
     * This method is automatically called by the InputHandler class when the
     * user has presses one of the arrow keys on the keyboard. The method should
     * check which direction for movement is required, by checking which
     * character was passed to this method (see parameter description below). If
     * the tile above, below, to the left or to the right is clear then the
     * player object should have its position changed to update its position in
     * the game window. If the target tile is not empty then the player should
     * not be moved, but other effects may happen such as giving a customer
     * food, or picking up food etc. To achieve this, the target tile should be
     * checked to determine the type of tile (food, table, wall etc.) and
     * appropriate methods called or attribute values changed.
     *
     * A second version of this method in a later task will also check if the
     * player's stamina is at zero, and if it is then the player should not be
     * moved.
     *
     * @param direction A char representing the direction that the player should
     * move. U is up, D is down, L is left and R is right.
     */
    public void movePlayer(char direction) {
        int x = player.getX();
        int y = player.getY();
        boolean ismove = true;
        int fixed_amount = 15;

        if (player.getStamina() >= fixed_amount) {
            if (direction == 'U' && isValidMove(x, y - 1, false)) {
                checkAndMove(x, y - 1);
            } else if (direction == 'D' && isValidMove(x, y + 1, false)) {
                checkAndMove(x, y + 1);
            } else if (direction == 'L' && isValidMove(x - 1, y, false)) {
                checkAndMove(x - 1, y);
            } else if (direction == 'R' && isValidMove(x + 1, y, false)) {
                checkAndMove(x + 1, y);
            } else {
                ismove = false;
            }
        }
//        System.out.println(player.getStamina());
        if (ismove && player.getStamina() >= 15) {
            player.changeStamina(-5);
        }
        if (isAtTrash(player.getX(), player.getY()) && player.hasFood()) {
            //get rid of the food with player 
            int prevfoodType = player.getCarriedFoodType();
            player.giveFood();

            //generate another food type at rand position
            Point randPos = getRandomPosition(spawnLocations, new Random());
            int randX = randPos.x;
            int randY = randPos.y;
            int foodTarget = prevfoodType;
            
            while (foodTarget == prevfoodType) {
                foodTarget = getRandomFoodType(new Random()); //1 2 3 4 
            }
            Random random = new Random();
            int randomNum = random.nextInt(3)+3;
            while (randomNum-2 == foodTarget) {
                randomNum = random.nextInt(3)+3; //1,2,3 --> 3,4,5;
            }
            level[randX][randY] = TileType.values()[randomNum];
        }

    }

    public boolean isAtTrash(int x, int y) {
        return x == LEVEL_WIDTH - 1 && y == 0;
    }

    /**
     * Attempts to give a customer the food that the player is carrying. This
     * method should only be called (from the movePlayer method) when the player
     * attempts to move into the same tile as a customer and the player is
     * already carrying the right type of food - i.e. the type of food that the
     * customer wants.
     *
     * This method should call a method on the player object to give the food
     * (thus setting the player's attributes properly to reflect that they
     * delivered food and now can pick up more), and increase the score as well
     * as printing the score to the standard output. Finally it should "feed"
     * the customer by calling the correct method on the Customer object
     * indicating that the player has "fed".
     *
     * @param g The Customer object corresponding to the customer in the game
     * that the player just attempted to move into the same tile as.
     */
    private void deliverFood(Customer customer) {
        //YOUR CODE HERE

        if (player.getCarriedFoodType() == customer.getFoodWanted()) {
            player.giveFood(); // removes the carried food
            customer.feed();
            score += customer.getPatience();
            System.out.println("score:" + score);
        }

    }
    
    boolean breakwall(int x, int y){
        System.out.println("turn Number :" + turnNumber);
        if( turnNumber%15 == 0 && level[x][y]==TileType.brokenWall2){
         level[x][y]=TileType.FLOOR1;
         return true;
     }
      else if(turnNumber %15 ==0 && level[x][y] != TileType.brokenWall1){
      // replace old image at x , y with new one
      level[x][y]=TileType.brokenWall1;
      return false;
     }
        else if(turnNumber %15 ==0 && level[x][y]!=TileType.brokenWall2){
         level[x][y]=TileType.brokenWall2;
         return false;
     }
     
      
     return false;
    }
    
    private boolean isValidMove(int x, int y, boolean isCustomer) {
        if (x < 0 || x >= LEVEL_WIDTH || y < 0 || y >= LEVEL_HEIGHT) {
            return false; // Out of bounds
        }

        TileType tileType = level[x][y];
        
        if(!isCustomer && (tileType==TileType.WALL || tileType==TileType.brokenWall1 || tileType==TileType.brokenWall2) ){
            player.setIsBreaking(true);
            boolean action = breakwall(x,y);
            System.out.println("isBreaking = " + player.isbreaking());
            if (!action)
                return false;
            
            return true;
            }
        
        player.setIsBreaking(false);
        // Check if player carries food, prevent from taking other food
        if (!isCustomer && player.getCarriedFoodType() != 0 && (tileType == TileType.FOOD1 || tileType == TileType.FOOD2 || tileType == TileType.FOOD3)) {
            return false;
        }

        // Wall, table, or food source blocking the move for customer
        if (tileType == TileType.WALL || tileType == TileType.TABLE || (isCustomer && (tileType == TileType.FOOD1 || tileType == TileType.FOOD2 || tileType == TileType.FOOD3))) {
            return false;
        }

        // Check if any other customer is already at the given coordinates
        if (isCustomer) {
            for (Customer customer : customers) {
                if (customer != null && customer.getX() == x && customer.getY() == y) {
                    return false;
                }
            }
        }

        // Check if the player's position contradicts the given coordinates
        return !(player != null && player.getX() == x && player.getY() == y);
    }

    private void checkAndMove(int x, int y) {
        TileType tileType = level[x][y];
        Point p = new Point();

        if (tileType == TileType.FOOD1) {
            player.grabFood(TileType.FOOD1.ordinal() - 2);
            level[x][y] = TileType.FLOOR1;
        } else if (tileType == TileType.FOOD2) {
            player.grabFood(TileType.FOOD2.ordinal() - 2);
            level[x][y] = TileType.FLOOR1;
        } else if (tileType == TileType.FOOD3) {
            player.grabFood(TileType.FOOD3.ordinal() - 2);
            level[x][y] = TileType.FLOOR1;
        } else if (player.hasFood()) {
            for (Customer customer : customers) {
                if (customer != null && customer.getX() == x && customer.getY() == y) {
                    //check if food type matches the customer:
                    deliverFood(customer);
                    break;
                }
            }
        }
        p.x = x;
        p.y = y;
        spawnLocations.add(p);
        player.setPosition(x, y);

    }

    /**
     * Moves a specific customer in the game. The method updates the X and Y
     * attributes of the Customer object passed to the method, to set its new
     * position.
     *
     * @param c The Customer that needs to be moved
     */
    private void moveCustomerTowardsTable(Customer customer) {
        int x = customer.getX();
        int y = customer.getY();

        // Find the nearest empty TABLE tile
        Point targetTable = findNearestEmptyTable(x, y);

        // Calculate the distance to the target table in the X and Y directions
        int xDistance = targetTable.x - x;
        int yDistance = targetTable.y - y;

        // Move the customer towards the target table
        if (Math.abs(xDistance) > Math.abs(yDistance)) {
            // Move in the X direction
            if (xDistance < 0) {
                // Move left
                if (isValidMove(x - 1, y, true) && !isStuckInLoop(customer, x - 1, y)) {
                    customer.setPosition(x - 1, y);
                } else {
                    // Change direction
                    if (isValidMove(x, y - 1, true) && !isStuckInLoop(customer, x, y - 1)) {
                        customer.setPosition(x, y - 1);
                    } else if (isValidMove(x, y + 1, true) && !isStuckInLoop(customer, x, y + 1)) {
                        customer.setPosition(x, y + 1);
                    }
                }
            } else {
                // Move right
                if (isValidMove(x + 1, y, true) && !isStuckInLoop(customer, x + 1, y)) {
                    customer.setPosition(x + 1, y);
                } else {
                    // Change direction
                    if (isValidMove(x, y - 1, true) && !isStuckInLoop(customer, x, y - 1)) {
                        customer.setPosition(x, y - 1);
                    } else if (isValidMove(x, y + 1, true) && !isStuckInLoop(customer, x, y + 1)) {
                        customer.setPosition(x, y + 1);
                    }
                }
            }
        } else {
            // Move in the Y direction
            if (yDistance < 0) {
                // Move up
                if (isValidMove(x, y - 1, true) && !isStuckInLoop(customer, x, y - 1)) {
                    customer.setPosition(x, y - 1);
                } else {
                    // Change direction
                    if (isValidMove(x - 1, y, true) && !isStuckInLoop(customer, x - 1, y)) {
                        customer.setPosition(x - 1, y);
                    } else if (isValidMove(x + 1, y, true) && !isStuckInLoop(customer, x + 1, y)) {
                        customer.setPosition(x + 1, y);
                    }
                }
            } else {
                // Move down
                if (isValidMove(x, y + 1, true) && !isStuckInLoop(customer, x, y + 1)) {
                    customer.setPosition(x, y + 1);
                } else {
                    // Change direction
                    if (isValidMove(x - 1, y, true) && !isStuckInLoop(customer, x - 1, y)) {
                        customer.setPosition(x - 1, y);
                    } else if (isValidMove(x + 1, y, true) && !isStuckInLoop(customer, x + 1, y)) {
                        customer.setPosition(x + 1, y);
                    }
                }
            }
        }

        // Update the previous position of the customer
        customer.setPrevPosition(new Point(x, y));
    }

    private boolean isStuckInLoop(Customer customer, int newX, int newY) {
        Point previousPosition = customer.getPrevPosition();
        return previousPosition != null && previousPosition.x == newX && previousPosition.y == newY;
    }

    private Point findNearestEmptyTable(int startX, int startY) {
        int closestDistance = Integer.MAX_VALUE;
        Point closestTable = null;

        for (int x = 0; x < GameEngine.LEVEL_WIDTH; x++) {
            for (int y = 0; y < GameEngine.LEVEL_HEIGHT; y++) {
                if (level[x][y] == TileType.TABLE && !isCustomerAt(x, y)) {
                    int distance = Math.abs(startX - x) + Math.abs(startY - y);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestTable = new Point(x, y);
                    }
                }
            }
        }

        // If no empty table found return current position as fallback
        if (closestTable == null) {
            closestTable = new Point(startX, startY);
        }

        return closestTable;
    }

    private boolean isCustomerAt(int x, int y) {
        for (Customer customer : customers) {
            if (customer != null && customer.getX() == x && customer.getY() == y) {
                return true;
            }
        }
        return false;
    }

//    private void moveCustomer(Customer customer) {
//        int x = customer.getX();
//        int y = customer.getY();
//
//        // Randomly choose a direction
//        int direction = rng.nextInt(4);
//
//        // Move the customer based on the chosen direction
//        switch (direction) {
//            case 0: // Move up
//                if (isValidMove(x, y - 1, true)) {
//                    customer.setPosition(x, y - 1);
//                }
//                break;
//            case 1: // Move down
//                if (isValidMove(x, y + 1, true)) {
//                    customer.setPosition(x, y + 1);
//                }
//                break;
//            case 2: // Move left
//                if (isValidMove(x - 1, y, true)) {
//                    customer.setPosition(x - 1, y);
//                }
//                break;
//            case 3: // Move right
//                if (isValidMove(x + 1, y, true)) {
//                    customer.setPosition(x + 1, y);
//                }
//                break;
//            default:
//                break;
//        }
//    }
    /**
     * Moves all customers on the current level. This method iterates over all
     * elements of the customers array (e.g. using a for loop) and checks if
     * each one is null (using an if statement inside that for loop). For every
     * element of the array that is NOT null, this method calls the moveCustomer
     * method and passes it the current array element (i.e. the current customer
     * object being used in the loop).
     */
    private void moveAllCustomers() {
        //YOUR CODE HERE
        for (Customer customer : customers) {
            if (customer != null && handle_customers(customer) && !isNextToTable(customer)) {
//                moveCustomer(customer);  task 10 removed it 
                moveCustomerTowardsTable(customer);

            }
        }
    }

    private boolean handle_customers(Customer customer) {
        // check if player is 3 tiles away from player should stop
        // or that the player is already on a table 
        return !((Math.abs(player.getX() - customer.getX()) < 3 && Math.abs(player.getY() - customer.getY()) < 3));
    }

    private boolean isCustomerOnTable(Customer customer) {
        int x = customer.getX();
        int y = customer.getY();
        return level[x][y] == TileType.TABLE;
    }

    /**
     * Processes the customers array to find any Customer in the array that has
     * been fed (i.e. given food by the player of the type they wanted). Any
     * Customer in the array with a "fed" attribute value of true should be set
     * to null; when drawing or moving customers the null elements in the
     * customers array are skipped, essentially removing them from the game.
     */
    private void cleanFedCustomers() {
        for (int i = 0; i < customers.length; i++) {
            if (customers[i] != null && customers[i].beenFed()) {
                customers[i] = null;
            }
        }
    }

    /**
     * This method is called when the number of "unfed" customers in the level
     * is zero, meaning that the player has fed all customers, "completing" the
     * level. This method is similar to the startGame method and will use SOME
     * identical code.
     *
     * This method should increase the current level number, create a new level
     * by calling the generateLevel method and setting the level attribute using
     * the returned 2D array, add new Customers, and finally place the player in
     * the new level.
     *
     * A second version of this method in a later task should also find suitable
     * positions to add customers and the player using the spawnLocations
     * ArrayList and code in the getSpawns method.
     */
    private void nextLevel() {
        levelNumber++; // Increment the levelNumber attribute
        player.changeStamina(150 - (levelNumber * 2));
        // Generate a new level and assign it to the level attribute
        level = generateLevel();

        // Add customers to the new level and assign the resulting array to the customers attribute
        customers = addCustomers();

        // Place the player in the new level
        placePlayer();
    }

    /**
     * The first version of this method should place the player in the game
     * level by setting new fixed X and Y values for the player object in this
     * class.
     *
     * The second version of this method in a later task should place the player
     * in a game level by choosing a position from the spawnLocations ArrayList,
     * removing the spawn position as it is used. The method sets the players
     * position in the level by calling its setPosition method with the x and y
     * values of the Point taken from the spawnLocations ArrayList.
     */
//    private void placePlayer() {
//        int playerX;
//        int playerY;
//        do {
//            playerX = rng.nextInt(GameEngine.LEVEL_WIDTH);
//            playerY = rng.nextInt(GameEngine.LEVEL_HEIGHT);
//        } while (level[playerX][playerY] != TileType.FLOOR1 && level[playerX][playerY] != TileType.FLOOR2);
//
//        player.setPosition(playerX, playerY);
//    }
    private void placePlayer() {
        Point position = spawnLocations.remove(rng.nextInt(spawnLocations.size())); // Remove and get a random spawn location

        player.setPosition(position.x, position.y);
    }

    /**
     * This method should be called each game turn and should check if all
     * NON-NULL Customer objects in the customers array have been fed. If all
     * valid Customer objects have been fed or the array contains only null
     * values then this method should return true, but if there are any customer
     * objects in the array with a "fed" attribute value of false then the
     * method should return false.
     *
     * @return true if all Customer objects in the customers array have been
     * fed, false otherwise
     */
    private boolean allCustomersFed() {
        //YOUR CODE HERE
        return false;   //modify to return either true or false
    }

    /**
     * This method is automatically called by doTurn, and it should reduce the
     * patience value for all Customer objects in the customers array by a small
     * fixed amount, by using a for loop that iterates over the customers array,
     * checking for non-null elements and calling an appropriate method on any
     * non-null objects in the array.
     */
    private void reduceCustomerPatience() {
        for (Customer customer : customers) {
            if (customer != null) {
                if (isNextToTable(customer)) {
                    customer.changePatience(-1); // Reduce patience by 1 if next to a table
                } else {
                    customer.changePatience(-3); // Reduce patience by 2 if not next to a table
                }
            }
        }
    }

    private boolean isNextToTable(Customer customer) {
        int x = customer.getX();
        int y = customer.getY();

        // Check if any adjacent tile is a table
        if (isTable(x - 1, y) || isTable(x + 1, y) || isTable(x, y - 1) || isTable(x, y + 1)) {
            return true;
        }

        return false;
    }

    private boolean isTable(int x, int y) {
        if (x >= 0 && x < LEVEL_WIDTH && y >= 0 && y < LEVEL_HEIGHT && level[x][y] == TileType.TABLE) {
            return true;
        }
        return false;
    }

    /**
     * Performs a single turn of the game when the user presses a key on the
     * keyboard. The method clears (removes from the game) "fed" customers every
     * ten turns, moves any customers that have not been fed and cleared every
     * three turns, and increments the turn number. Finally it makes the GUI
     * redraw the game level by passing it the level, player and customers
     * objects for the current level.
     *
     * A second version of this method in a later task will also check if all
     * customers in the current level have been fed, and if they have it will
     * call the nextLevel() method to generate a new, harder level.
     *
     * A third version of this method will also increase the player's stamina
     * slightly to allow them to recover and move again if their stamina runs
     * out.
     */
    public void doTurn() {
        turnNumber++;

        if (turnNumber % 10 == 0) {
            cleanFedCustomers();
        }
        if (turnNumber % 3 == 0) {
            moveAllCustomers();
            reduceCustomerPatience();
        }
        // Update the player's stamina if < fixed amount =-15
        if (player.getStamina() <= 15) {
            player.changeStamina(1);
            accelerate++;
        }
        if (accelerate % 15 == 0 && player.getStamina() < 20) {
            player.changeStamina(600);
            accelerate = 0;
        }
        gui.updateDisplay(level, player, customers);

        // Check if all customers have been fed or no customers remain
        boolean allCustomersFed = true;
        for (Customer customer : customers) {
            if (customer != null && !customer.beenFed()) {
                allCustomersFed = false;
                break;
            }
        }

        // Call nextLevel() if all customers have been fed or no customers remain
        if (allCustomersFed || areAllCustomersNull()) {
            nextLevel();
            doTurn();
        }
    }

    private boolean areAllCustomersNull() {
        for (Customer customer : customers) {
            if (customer != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Starts a game. This method generates a level, finds spawn positions in
     * the level, adds customers and the player and then requests the GUI to
     * update the level on screen using the information on level, player and
     * customers.
     */
    public void startGame() {
        level = generateLevel();
        spawnLocations = getSpawns();
        customers = addCustomers();
        player = createPlayer();
        gui.updateDisplay(level, player, customers);
    }
}
