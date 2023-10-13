import Graphics.CanvasWindow;
import Graphics.Ellipse;
import Graphics.GraphicsGroup;
import Graphics.GraphicsObject;
import Graphics.Image;
import java.awt.Color;
import java.util.List;

/**
 * Represents a Bullet that can collide with AlienSpaceShips.
 */
public class Bullet extends GraphicsGroup {

    public static final double BULLET_RADIUS = 8;
    public static final Color BULLET_COLOR = Color.WHITE;
    public static final int BULLET_SPEED = 20;

    private double centerX;
    private double centerY;
    private double maxBoundX;
    private double maxBoundY;
    private Ellipse bulletShape;

    /**
     * Constructs a bullet ellipse at the given X coordinate.
     * 
     * @param centerX center of bullet
     */
    public Bullet(double centerX) {
        this.centerX = centerX;
        this.centerY = SpaceInvaders.CANVAS_HEIGHT * 3 / 4;
        this.bulletShape = new Ellipse(centerX - BULLET_RADIUS, centerY, BULLET_RADIUS * 2, BULLET_RADIUS * 2);
        bulletShape.setFillColor(BULLET_COLOR);
        this.maxBoundX = SpaceInvaders.CANVAS_WIDTH;
        this.maxBoundY = SpaceInvaders.CANVAS_HEIGHT;

    }

    /**
     * Update the bullet's position if it is in bounds and a collision has not
     * occured.
     * 
     * @return true if the ball is on the canvas
     */
    public boolean updatePosition(double dt) {
        centerY = centerY - (BULLET_SPEED * dt);

        if (centerX > BULLET_RADIUS && centerX < maxBoundX - BULLET_RADIUS && centerY > BULLET_RADIUS
                && centerY < maxBoundY - BULLET_RADIUS && !checkCollision(AlienSpaceShip.alienList)) {

            centerY = centerY - (BULLET_SPEED * dt);
            bulletShape.setPosition(centerX - BULLET_RADIUS, centerY - BULLET_RADIUS);
            return true;

        } else {
            removeFromCanvas(SpaceInvaders.getThisCanvas());
            return false;
        }
    }

    /**
     * Check if this bullet has collided with any aliens in the list according to
     * the top midpoint of the bullet. Chose not to check for side collisions to
     * make the game more difficult.
     * 
     * @param alienList aliens to check collision with
     */
    public boolean checkCollision(List<Image> alienList) {
        GraphicsObject topMidPoint = SpaceInvaders.getThisCanvas().getElementAt(
                (centerX + (centerX + 2 * BULLET_RADIUS)) / 2, centerY - 0.1);

        if (topMidPoint != null && alienList.contains(topMidPoint)) {

            alienList.remove(topMidPoint);
            SpaceInvaders.getThisCanvas().remove(topMidPoint);
            SpaceInvaders.raiseScore();
            return true;

        }
        return false;
    }

    /**
     * Adds the bullet's shape to the given canvas.
     */
    public void addToCanvas(CanvasWindow canvas) {
        canvas.add(bulletShape);
    }

    /**
     * Removes the bullet's shape from the given canvas.
     */
    public void removeFromCanvas(CanvasWindow canvas) {
        canvas.remove(bulletShape);
    }

}
