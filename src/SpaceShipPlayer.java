import java.util.Set;
import Graphics.*;
import Graphics.CanvasWindow;
import Graphics.events.Key;

/**
 * A SpaceShipPlayer represented as an image of a ship that can shoot Bullets
 * and move left to right.
 */
public class SpaceShipPlayer {
    public static final double SHIP_WIDTH = 70;
    public static final double SHIP_HEIGHT = 70;

    private double centerX;
    private double centerY;
    private double maxBoundX;
    private Image shipShape;

    /**
     * Constructs a space ship player at the center of the canvas width. Y position 
     * stays constant.
     */
    public SpaceShipPlayer() {
        this.centerX = SpaceInvaders.CANVAS_WIDTH / 2 - SHIP_WIDTH / 2;
        this.centerY = SpaceInvaders.CANVAS_HEIGHT * 3 / 4;

        this.maxBoundX = SpaceInvaders.CANVAS_WIDTH;

        this.shipShape = new Image(centerX, centerY);
        shipShape.setImagePath("spaceship.png");
        shipShape.setMaxHeight(SHIP_HEIGHT);
        shipShape.setMaxWidth(SHIP_WIDTH);

    }

    /**
     * Moves the SpaceShipPlayer shape left or right according to the arrow key
     * pressed.
     * 
     * @param canvas    canvas window spaceship is on
     * @param key       keys pressed
     * @param animation true if game running, false if game ends
     */
    public void move(CanvasWindow canvas, Set<Key> keySet, boolean animation) {
        for (Key key : keySet) {
            if (key == Key.LEFT_ARROW && animation) {
                updatePosition(shipShape.getX() - 10);

            } else if (key == Key.RIGHT_ARROW && animation) {
                updatePosition(shipShape.getX() + 10);
            }
        }
    }

    /**
     * Update the ship's position to the given center x, if it is in bounds.
     */
    public void updatePosition(double newCenterX) {
        if (newCenterX > 0 && newCenterX < maxBoundX - SHIP_WIDTH) {
            centerX = newCenterX;
            shipShape.setPosition(centerX, centerY);
        }
    }

    /**
     * Adds the ship's shape to the given canvas.
     * 
     * @param canvas canvas window
     */
    public void addToCanvas(CanvasWindow canvas) {
        canvas.add(shipShape);
    }

    /**
     * Gets the shape of the space ship player
     * 
     * @return shape
     */
    public Image getShape() {
        return shipShape;
    }

    /**
     * Gets the x position of the space ship player.
     * 
     * @return center x position
     */
    public double getXPosition() {
        return centerX;
    }
}