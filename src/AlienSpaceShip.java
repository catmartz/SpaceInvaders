import Graphics.CanvasWindow;
import Graphics.Image;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An AlienSpaceShip represented as an image of an alien that can be removed by
 * a Bullet.
 */
public class AlienSpaceShip {
    // 350 × 250 pixels
    public static final double ALIEN_PADDING = 10;
    public static final double ALIEN_WIDTH = (SpaceInvaders.CANVAS_WIDTH / 2.8) / 5;
    public static final double ALIEN_HEIGHT = (SpaceInvaders.CANVAS_HEIGHT / 2) / 5;
    public static final double ALIEN_COLLISION = SpaceInvaders.CANVAS_HEIGHT * 3 / 4 - 50;

    public static List<Image> alienList;
    private Image alienShape;

    /**
     * Constructs aliens and adds them to the given canvas from the alien list.
     * 
     * @param canvas canvas window
     */
    public AlienSpaceShip(CanvasWindow canvas) {
        alienList = new ArrayList<>();

        generateAliens();

        for (Image i : alienList) {
            canvas.add(i);
        }
    }

    /**
     * Generates a 4x10 grid of aliens and places them in a list.
     */
    public void generateAliens() {
        String redAlien = "alienSpaceShipRed.png";
        String greenAlien = "alienSpaceShip.png";

        double centerX = ALIEN_PADDING * 5;
        double centerY = SpaceInvaders.CANVAS_HEIGHT / 8;

        for (int i = 0; i < 40; i++) {
            alienShape = new Image(centerX, centerY);
            alienShape.setMaxHeight(ALIEN_HEIGHT);
            alienShape.setMaxWidth(ALIEN_WIDTH);
            alienList.add(alienShape);

            if (i <= 8) {
                centerX += ALIEN_WIDTH + ALIEN_PADDING;
                alienShape.setImagePath(redAlien);
            } else if (i == 9) {
                centerY = SpaceInvaders.CANVAS_HEIGHT / 4;
                centerX = ALIEN_PADDING * 5;
                alienShape.setImagePath(redAlien);
            } else if (i <= 18) {
                centerX += ALIEN_WIDTH + ALIEN_PADDING;
                alienShape.setImagePath(greenAlien);
            } else if (i == 19) {
                centerY = SpaceInvaders.CANVAS_HEIGHT * 3 / 8;
                centerX = ALIEN_PADDING * 5;
                alienShape.setImagePath(greenAlien);
            } else if (i <= 28) {
                centerX += ALIEN_WIDTH + ALIEN_PADDING;
                alienShape.setImagePath(redAlien);
            } else if (i == 29) {
                centerY = SpaceInvaders.CANVAS_HEIGHT / 2;
                centerX = ALIEN_PADDING * 5;
                alienShape.setImagePath(redAlien);
            } else if (i <= 39) {
                centerX += ALIEN_WIDTH + ALIEN_PADDING;
                alienShape.setImagePath(greenAlien);
            }
        }

    }

    /**
     * Moves all the aliens down the canvas and checks if a collision occured
     * 
     * @return true if collision occured
     */
    public static boolean move() {
        boolean out = false;
        if (alienList != null) {
            Iterator<Image> alienItr = alienList.iterator();
            while (alienItr.hasNext()) {
                Image alien = alienItr.next();
                double alienX = alien.getX();
                double alienY = alien.getY() + 0.2;
                alien.setPosition(alienX, alienY);
                if (checkCollision(alienY)) {
                    out = true;
                }
            }
        }
        return out;

    }

    /**
     * Checks if an alien reached the space ship player
     * 
     * @param y the alien location
     * @return true if collision occured
     */
    private static boolean checkCollision(double y) {
        if (y >= ALIEN_COLLISION) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Removes an entire row of aliens if they reached the space ship player.
     */
    public static void removeAlienRow() {
        alienList.removeIf(alien -> {
            if (alien.getY() >= ALIEN_COLLISION) {
                SpaceInvaders.getThisCanvas().remove(alien);
                return true;
            }
            return false;
        });
    }

}
