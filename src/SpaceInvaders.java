import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Graphics.*;
import Graphics.events.Key;
import Graphics.ui.Button;

/**
 * Main class to run the game Space Invaders
 * 
 * @authors Cat Martins, Jake Murakami
 */
public class SpaceInvaders {

    public static final int CANVAS_WIDTH = 700;
    public static final int CANVAS_HEIGHT = 700;

    public static int score;
    public static SpaceShipPlayer shipPlayer;
    public static Queue<Image> livesQueue;
    public static List<Bullet> bulletList;
    public static Iterator<Bullet> bulletItr;

    public AlienSpaceShip aliens;
    public boolean animation = false;
    private boolean titleOnCanvas = true;
    private boolean endendGame = false;

    private static CanvasWindow canvas;
    private static Image background;
    private static GraphicsText scoreText;
    private static GraphicsText livesText;

    private Bullet bullet;
    private GraphicsText titleText;
    private GraphicsText startText;
    private GraphicsText gameOverText;
    private GraphicsText gameoverScore;
    private Rectangle gameOverBackground;
    private Button playAgain;
    private GraphicsGroup gameOver;
    private GraphicsGroup title;

    public static void main(String[] args) {
        new SpaceInvaders();

    }

    /**
     * Creates the score and lives tracker, title screen, player spaceship, and list
     * of bullets then runs the game.
     */
    public SpaceInvaders() {
        bulletList = new ArrayList<Bullet>();
        score = 0;
        livesQueue = new LinkedList<>();

        canvas = new CanvasWindow("Space Invaders", CANVAS_WIDTH, CANVAS_HEIGHT);

        background = new Image(0, 0, "starryBackground.jpg");
        background.setMaxHeight(CANVAS_HEIGHT);
        canvas.add(background);

        shipPlayer = new SpaceShipPlayer();
        shipPlayer.addToCanvas(canvas);

        scoreText = new GraphicsText();
        livesText = new GraphicsText();
        title = new GraphicsGroup();
        gameOver = new GraphicsGroup();

        titleScreen();

        run();
    }

    /**
     * Controls all the animation for the game which includes moving on the canvas,
     * shooting bullets from the space ship player and checking for endgame.
     */
    private void run() {

        canvas.animate(() -> {
            if (!endendGame) {
                if (score == 40) {
                    endGame();
                } else {

                    shipPlayer.move(canvas, canvas.getKeysPressed(), animation);

                    if (AlienSpaceShip.move() && !decreaseLife() && !titleOnCanvas) {
                        endGame();
                    }

                    bulletItr = bulletList.iterator();
                    while (bulletItr.hasNext()) {
                        bullet = bulletItr.next();
                        if (bullet.updatePosition(0.5)) {

                        } else {
                            bulletItr.remove();
                        }
                    }
                }
            }
        });

        canvas.onKeyDown(keyEvent -> {

            for (Key key : canvas.getKeysPressed()) {
                if (key == Key.SPACE && titleOnCanvas) {
                    aliens = new AlienSpaceShip(canvas);
                    scoreTracker();
                    livesTracker();
                    canvas.remove(title);
                    titleOnCanvas = false;
                    animation = true;
                    canvas.pause(1000);
                } else if (key == Key.SPACE && animation && !endendGame) {
                    bullet = new Bullet(
                            SpaceInvaders.getShipPlayer().getXPosition() + (SpaceShipPlayer.SHIP_WIDTH / 2));
                    bulletList.add(bullet);
                    bullet.addToCanvas(canvas);
                }
            }
        });
    }

    /**
     * Makes the title screen for the start of the game in the center of the canvas.
     */
    public void titleScreen() {
        titleText = new GraphicsText();
        titleText.setFont("rockwell", FontStyle.BOLD, 80);
        titleText.setFillColor(Color.WHITE);
        titleText.setText("Space Invaders");
        titleText.setCenter(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2);

        startText = new GraphicsText();
        startText.setFont("rockwell", FontStyle.PLAIN, 20);
        startText.setFillColor(Color.WHITE);
        startText.setText("| press space to start |");
        startText.setCenter(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 + 60);

        title.add(titleText);
        title.add(startText);
        canvas.add(title);
    }

    /**
     * Creates the score tracker text in the upper left corner.
     */
    public void scoreTracker() {
        scoreText = new GraphicsText("SCORE " + score, CANVAS_WIDTH / 15, CANVAS_HEIGHT / 20);
        scoreText.setFont("rockwell", FontStyle.BOLD, 20);
        scoreText.setFillColor(Color.WHITE);
        canvas.add(scoreText);
    }

    /**
     * Creates the lives tracker text in the upper right corner.
     */
    public void livesTracker() {

        livesText = new GraphicsText("LIVES", CANVAS_WIDTH * 7 / 10, CANVAS_HEIGHT / 20);
        livesText.setFont("rockwell", FontStyle.BOLD, 20);
        livesText.setFillColor(Color.WHITE);

        for (int i = 3; i > 0; i--) {
            Image lifeImage = new Image("spaceship.png");
            lifeImage.setMaxWidth(30);

            livesQueue.add(lifeImage);
            canvas.add(lifeImage, 530 + i * 40, 15);
        }

        canvas.add(livesText);
    }

    /**
     * Increases the score when an alien is hit and changes the text on the canvas.
     */
    public static void raiseScore() {
        score++;
        scoreText.setText("Score: " + score);
    }

    /**
     * Decreases the lives when the player space ship is hit and s an image from the
     * canvas.
     */
    public static boolean decreaseLife() {
        if (livesQueue.size() != 0) {
            canvas.remove(livesQueue.remove());
            AlienSpaceShip.removeAlienRow();
            if (livesQueue.isEmpty())
                return false;
            return true;
        }
        return false;

    }

    /**
     * Creates and adds the play again button to the canvas which resets the canvas,
     * places the spaceship back to center, removes all bullets, and resets score.
     * Adds the game over screen.
     */
    public void endGame() {
        gameOverText();
        playAgain = new Button("Play Again");
        playAgain.setCenter(350, 370);
        playAgain.onClick(() -> {
            canvas.removeAll();
            canvas.add(background);

            score = 0;

            animation = true;
            titleOnCanvas = true;

            titleScreen();
            bulletList.clear();
            livesQueue.clear();
            shipPlayer.updatePosition(CANVAS_WIDTH / 2 - SpaceShipPlayer.SHIP_WIDTH / 2);
            shipPlayer.addToCanvas(canvas);
            endendGame = false;
        });
        gameOver.add(playAgain);
        canvas.add(gameOver);
        endendGame = true;

    }

    /**
     * Creates the game over screen.
     */
    public void gameOverText() {
        gameOverBackground = new Rectangle(0, 0, 650, 120);
        gameOverBackground.setFilled(true);
        gameOverBackground.setFillColor(Color.WHITE);
        gameOverBackground.setCenter(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - 60);

        if (score >= 40) {
            gameOverText = new GraphicsText("YOU WIN!");
        } else {
            gameOverText = new GraphicsText("GAME OVER!");
        }
        gameOverText.setFont("rockwell", FontStyle.BOLD, 80);
        gameOverText.setFillColor(Color.RED.darker());
        gameOverText.setCenter(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - 75);

        gameoverScore = new GraphicsText();
        gameoverScore.setText("Score: " + score);
        gameoverScore.setFont("rockwell", FontStyle.BOLD, 30);
        gameoverScore.setCenter(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - 20);

        gameOver.add(gameOverBackground);
        gameOver.add(gameoverScore);
        gameOver.add(gameOverText);
    }

    /**
     * Gets the canvas for the game.
     * 
     * @return this canvas
     */
    public static CanvasWindow getThisCanvas() {
        return canvas;
    }

    /**
     * Gets the ship player.
     * 
     * @return the ship player on the canvas
     */
    public static SpaceShipPlayer getShipPlayer() {
        return shipPlayer;
    }

}
