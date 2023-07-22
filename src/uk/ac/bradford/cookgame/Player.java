package uk.ac.bradford.cookgame;

/**
 * The Player class is a subclass of Entity and adds specific state and
 * behaviour for the player in the game including stamina, the ability to change
 * this stamina value, and whether or not the player is currently holding food.
 *
 * @author prtrundl
 */
public class Player extends Entity {

    /**
     * maxStamina stores the maximum possible stamina for this player
     */
    private final int maxStamina;

    /**
     * stamina stores the current stamina for this player
     */
    private int stamina;

    /**
     * carryingFood is used to track whether the player is holding a food item.
     * The player can carry one food item at a time (a value of true means the
     * player currently has food in their hands).
     */
    private boolean carryingFood;
    
    
    private boolean isBreaking=false;
    /**
     * The type of food the player is currently carrying, represented as an
     * integer.
     *
     * @return 0 for no food, 1 for red food, 2 for green food, 3 for brown
     * food.
     */
    private int carriedFoodType;

    /**
     * This constructor is used to create a Player object to use in the game
     *
     * @param maxStamina the maximum stamina of this Player, also used to set
     * its starting stamina value
     * @param x the X position of this Player in the game
     * @param y the Y position of this Player in the game
     */
    public Player(int maxStamina, int x, int y) {
        this.maxStamina = maxStamina;
        this.stamina = maxStamina;
        carryingFood = false;
        carriedFoodType = 0;
        setPosition(x, y);
    }

    /**
     * Changes the current stamina value for this Player, setting the stamina to
     * maxStamina if the change would cause the stamina attribute to exceed
     * maxStamina, or setting stamina to 0 if the change would cause the stamina
     * to fall below zero.
     *
     * @param change An integer representing the change in stamina for this
     * Player. Passing a positive value will increase the stamina, passing a
     * negative value will decrease the stamina.
     */
    public void changeStamina(int change) {
        stamina += change;
        if (stamina > maxStamina) {
            stamina = maxStamina;
        }
        if (stamina < 0) {
            stamina = 0;
        }
    }

    /**
     * Returns the current stamina value for the player
     *
     * @return the value of the stamina attribute for the player
     */
    public int getStamina() {
        return stamina;
    }

    /**
     * Returns the maxHealth value for the player
     *
     * @return the value of the maxStamina attribute for this Customer
     */
    public int getMaxStamina() {
        return maxStamina;
    }

    /**
     * Returns a value representing whether this Player is currently carrying a
     * food item.
     *
     * @return true if the player is carrying food, false otherwise.
     */
    public boolean hasFood() {
        return carryingFood;
    }

    /**
     * Returns an integer representing the type of food the player is carrying,
     * if any.
     *
     * @return 0 for no food carried, 1 for red food, 2 for green food, 3 for
     * brown food
     */
    public int getCarriedFoodType() {
        return carriedFoodType;
    }

    /**
     * Sets the carryingFood attribute to true, representing the fact that the
     * player has picked up a food item.
     *
     * @param type the type of food the player is grabbing: 0 is nothing, 1 is
     * red food, 2 is green food, 3 is brown food
     */
    public void grabFood(int type) {
        carryingFood = true;
        carriedFoodType = type;
        
    }

    /**
     * Sets the carryingFood attribute to false, representing the fact that the
     * player has given a food item to a customer, and sets the carried food
     * type to 0 (no food).
     */
    public void giveFood() {
        carryingFood = false;
        carriedFoodType = 0;
    }
    
    void setIsBreaking(boolean current){
        isBreaking=current;
    }
    boolean isbreaking(){
     return isBreaking;
    }
}
