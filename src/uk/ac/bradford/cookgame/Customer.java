package uk.ac.bradford.cookgame;

import java.awt.Point;

/**
 * The Customer class is a subclass of Entity and adds specific state and
 * behaviour for the customers in the game, including patience and the ability
 * to change patience values, the type of food the customer wants as well as
 * whether the Customer has been fed yet.
 *
 * @author prtrundl
 */
public class Customer extends Entity {

    /**
     * maxPatience stores the maximum possible patience for this Customer
     */
    private final int maxPatience;

    /**
     * patience stores the current patience value for this Customer. Patience
     * will be decreased over time if the customer has not yet been fed the type
     * of food that they want.
     */
    private int patience;

    /**
     * foodWanted represents the type of food (red, green or brown) that the
     * customer wants, as an integer. 1 is red, 2 is green, 3 is brown.
     */
    private int foodWanted;
    private Point prevPosition;
    /**
     * fed stores a boolean value indicating if the Customer has been given food
     * by the player matching the type of food they want.
     */
    private boolean fed;

    /**
     * This constructor is used to create a Customer object to use in the game,
     * and sets the type of food the player wants to type 1 (i.e. red food)
     *
     * @param maxPatience the maximum patience of this Customer, also used to
     * set its starting patience value
     * @param x the X position of this Customer in the level
     * @param y the Y position of this Customer in the level
     */
    public Customer(int maxPatience, int x, int y) {
        this(maxPatience, x, y, 1);         //calls four argument constructor with red food wanted
        fed = false;
    }

    /**
     * This constructor is used to create a Customer object to use in the game
     *
     * @param maxPatience the maximum patience of this Customer, also used to
     * set its starting patience value
     * @param x the X position of this Customer in the level
     * @param y the Y position of this Customer in the level
     * @param foodType the type of food this customer wants (1 is red food, 2 is
     * green food and 3 is brown food). Passing any other value will set this
     * customers wanted food type to 1 (red food).
     */
    public Customer(int maxPatience, int x, int y, int foodType) {
        this.maxPatience = maxPatience;
        this.patience = maxPatience;
        setPosition(x, y);
        if (foodType < 1 || foodType > 3) {
            foodType = 1;
        }
        foodWanted = foodType;
        fed = false;
        this.prevPosition = new Point(0, 0); // Initialize prevPosition object
    }

    /**
     * Changes the current patience value for this Customer, setting the
     * patience to maxPatience if the change would cause the patience value to
     * exceed maxPatience, or setting it to 0 if the change would make patience
     * negative.
     *
     * @param change An integer representing the change in patience for this
     * Customer. Passing a positive value will increase the patience, passing a
     * negative value will decrease the patience.
     */
    public void changePatience(int change) {
        patience += change;
        if (patience > maxPatience) {
            patience = maxPatience;
        }
        if (patience < 0) {
            patience = 0;
        }
    }

    /**
     * Returns the current patience value for this Customer
     *
     * @return the value of the patience attribute for this Customer
     */
    public int getPatience() {
        return patience;
    }

    /**
     * Returns the maxPatience value for this Customer
     *
     * @return the value of the maxPatience attribute for this Customer
     */
    public int getMaxPatience() {
        return maxPatience;
    }

    /**
     * Returns the type of food this customer wants
     *
     * @return 1 for red food, 2 for green food, 3 for brown food
     */
    public int getFoodWanted() {
        return foodWanted;
    }

    /**
     * Sets a customers fed attribute to true, indicating that the Customer has
     * been given the correct type of food by the player
     */
    public void feed() {
        fed = true;
    }

    /**
     * Accessor method to see if the customer has been fed by the player or not.
     *
     * @return true if the player has correctly fed this Customer, false
     * otherwise.
     */
    public boolean beenFed() {
        return fed;
    }
    public void setPrevPosition(Point point){
        prevPosition.x = point.x;
        prevPosition.y = point.y;
    }
    public Point getPrevPosition(){
    return prevPosition;
    }

}
