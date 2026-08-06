package core;

/**
 * @author nathanomana
 * @author Ken Tran
 * The game panel handles rendering and runs the game loop in its own thread.
 * Draws the game world to an off-screen buffer at base resolution,
 * then scales it up to the window size for crisp pixel art.
 */

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.io.IOException;
import javax.imageio.ImageIO;

import entity.PlayerPig;
import input.InputManager;
import ui.InterfaceManager;
import input.KeyHandler;
import tile.Tiles;
import input.PlayInput;
import app.Level;
import app.StateManager;
import app.Mode;
import app.Direction;
import app.Position;

import input.InputHandler;
import ui.InterfaceManager;

public class GamePanel extends JPanel implements Runnable {

    // Display Constants

    // Size of each tile in pixels
    public static final int TILE_SIZE = 32;

    // Number of tiles visible on screen
    public static final int SCREEN_COLS = 24;
    public static final int SCREEN_ROWS = 14;

    // Internal render resolution
    public static final int BASE_WIDTH = TILE_SIZE * SCREEN_COLS; // 768
    public static final int BASE_HEIGHT = TILE_SIZE * SCREEN_ROWS; // 448

    // Actual window size on the monitor (scaled up from base)
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    // Instance Fields
    private final GameEngine engine;
    private final KeyHandler keyH; // will be replaced my InputManager
    private final BufferedImage frameBuffer;
    private Image playerImage;
    public InputManager inputM = new InputManager(this);
    public InterfaceManager uiM = new InterfaceManager(this);
    public StateManager stateM = new StateManager(this);
    public PlayerPig player = new PlayerPig(3, 3, TILE_SIZE);
    public Level level;
    public Camera camera = new Camera();

    private final GameRenderer renderer = new GameRenderer(this);

    // Map Tiles
    private Image grassTile;
    private Image wallTile;

    public Image getGrassTile() {
        return grassTile;
    }

    // Aliases for Ken's screen classes
    public int screenWidth = SCREEN_WIDTH;
    public int screenHeight = SCREEN_HEIGHT;
    public int tileSize = TILE_SIZE;
    public float scale = 1.0f;
    public int remainingPig = 0;

    private TexturePaint grassTexturePaint;
    private Thread gameThread;
    private boolean running;

    // Constructor
    /**
     * Initializes the GamePanel container, setting its dimensional sizes,
     * background color,
     * base configurations, and instantiating the core GameEngine. Loads tile
     * textures and sprites.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        this.keyH = new KeyHandler();
        // this.addKeyListener(keyH);

        // Register Ken's initial input listener (TitleInput)
        this.addKeyListener(inputM.getCurrentInput());

        this.frameBuffer = new BufferedImage(BASE_WIDTH, BASE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        // //rendering
        // if (grassTile instanceof BufferedImage) {
        // grassTexturePaint = new TexturePaint((BufferedImage) grassTile, new
        // Rectangle(0, 0, TILE_SIZE, TILE_SIZE));
        // }

        for (Tiles tile : Tiles.values()) {
            try {
                String path = "/tiles/" + tile.getImageName();
                tile.image = ImageIO.read(getClass().getResourceAsStream(path));
            } catch (Exception e) {
            }
        }

        this.grassTile = Tiles.ENVIRONMENT_005.image;
        this.wallTile = Tiles.FENCE_000.image;

        // Load player sprite
        try {
            this.playerImage = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_down_01.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println("Could not load player sprite.");
        }

        this.engine = new GameEngine();
    }

    /**
     * Gets the associated GameEngine instance controlling the game logic.
     *
     * @return the GameEngine instance
     */
    public GameEngine getEngine() {
        return engine;
    }

    // Starts the game loop in a new thread
    /**
     * Initializes the Level state with entities and spawns,
     * then starts the main game loop on a newly spawned thread.
     */
    public void startGame() {
        this.level = new Level(player);
        this.level.spawnEntities(engine.getMap(), this);
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Stores the size of the users computer screen and sets the game to fullscreen
     * mode.
     */
    public void setFullScreen() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        // Get the JFrame that contains this panel
        JFrame window = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (window == null) {
            return;
        }

        gd.setFullScreenWindow(null);
        window.dispose();
        window.setUndecorated(true);
        window.setResizable(false);
        gd.setFullScreenWindow(window);

        DisplayMode displayMode = gd.getDisplayMode();
        screenWidth = displayMode.getWidth();
        screenHeight = displayMode.getHeight();

        scale = 4.0f;
        tileSize = (int) (TILE_SIZE * scale); // multiply from original constant, not current tileSize

        uiM.resetPlayScreen();
        requestFocusInWindow();
        // player.resetLevelState(3);
    }

    /**
     * Reverts the game client back to windowed mode, disabling full screen.
     * Updates display scale and re-initializes UI dimensions.
     */
    public void setWindowScreen() {
        if (!GraphicsEnvironment.isHeadless()) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            JFrame window = (JFrame) SwingUtilities.getWindowAncestor(this);
            gd.setFullScreenWindow(null);

            if (window != null) {
                window.dispose();
                window.setUndecorated(false);
                window.setResizable(false);
                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);
            }
        }

        scale = 1.0f;
        tileSize = TILE_SIZE;
        screenWidth = SCREEN_WIDTH;
        screenHeight = SCREEN_HEIGHT;

        uiM.resetPlayScreen();
        requestFocusInWindow();
        // player.resetLevelState(3);
    }

    // Game Loop
    /**
     * The core timing loop for the game, utilizing a delta-time accumulator.
     * Continuously ticks the GameEngine logic and forces screen repaints
     * to match the defined TPS (Ticks Per Second).
     */
    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / engine.getTicksPerSecond();
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                pollInput();

                if (stateM.getCurrentState() != StateManager.gameState.PAUSE) {
                    engine.tick();

                    if (engine.getState().getMode() == Mode.PLAYING && level != null) {
                        level.update(engine, engine.getMap());
                    }
                }

                repaint();
                delta--;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private int ticksSinceLastMove = 0;

    // Perhaps move into own abstract movement class

    // Reads the current state of the KeyHandler and forwards
    // the active direction to the engine as a queued move
    /**
     * Polls the active keyboard inputs and processes queued movement events
     * by forwarding them to the GameEngine. Acts as the controller in MVC.
     */
    private void pollInput() {
        Mode currentMode = engine.getState().getMode();

        // Ken's InputManager handles the START screen now.
        // We only process movement if we are actually PLAYING.
        if (currentMode != Mode.PLAYING) {
            return;
        }

        ticksSinceLastMove++;
        if (ticksSinceLastMove < 8) {
            return;
        }

        PlayInput playInput = (PlayInput) inputM.getPlayInput();

        if (playInput.up || playInput.down || playInput.left || playInput.right) {
            Direction direction;
            if (playInput.up)
                direction = Direction.UP;
            else if (playInput.down)
                direction = Direction.DOWN;
            else if (playInput.left)
                direction = Direction.LEFT;
            else
                direction = Direction.RIGHT;

            player.queueMove(direction);
            engine.handleInput(direction);
            ticksSinceLastMove = 0;
        } else {
            player.clearQueuedMove();
        }
    }

    /**
     * Overrides JPanel's paintComponent to draw the active game state.
     * Delegates map, entity, and player drawing to the GameRenderer.
     *
     * @param g the Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Mode currentMode = engine.getState().getMode();
        StateManager.gameState currentGameState = stateM.getCurrentState();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if (currentMode == Mode.START) {
            if (currentGameState != StateManager.gameState.TITLE) {
                stateM.setCurrentState(StateManager.gameState.TITLE);
            }
            screenWidth = getWidth() > 0 ? getWidth() : SCREEN_WIDTH;
            screenHeight = getHeight() > 0 ? getHeight() : SCREEN_HEIGHT;
            uiM.draw(g2);
        } else if (currentGameState == StateManager.gameState.WIN
                || currentGameState == StateManager.gameState.LOSE) {
            screenWidth = getWidth() > 0 ? getWidth() : screenWidth;
            screenHeight = getHeight() > 0 ? getHeight() : screenHeight;
            uiM.draw(g2);
        } else {
            int panelWidth = getWidth() > 0 ? getWidth() : screenWidth;
            int panelHeight = getHeight() > 0 ? getHeight() : screenHeight;
            screenWidth = panelWidth;
            screenHeight = panelHeight;

            double scale = Math.min((double) panelWidth / BASE_WIDTH, (double) panelHeight / BASE_HEIGHT);
            int viewportWidth = (int) Math.round(BASE_WIDTH * scale);
            int viewportHeight = (int) Math.round(BASE_HEIGHT * scale);
            int viewportX = (panelWidth - viewportWidth) / 2;
            int viewportY = (panelHeight - viewportHeight) / 2;

            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, panelWidth, panelHeight);
            g2.translate(viewportX, viewportY);
            g2.scale(scale, scale);

            // Calculate camera once, reuse everywhere
            camera.update(player.renderX, player.renderY, engine.getMapWidth(), engine.getMapHeight());
            Position cam = camera.getOffset();
            g2.translate(-cam.getX(), -cam.getY());

            renderer.drawMap(g2, cam);

            if (level != null) {
                renderer.drawEntities(g2);
            }

            renderer.drawPlayer(g2, ticksSinceLastMove);

            // Draw UI in the same base-resolution viewport as the world.
            g2.translate(cam.getX(), cam.getY());

            uiM.draw(g2);
        }
        
        // This synchronizes the graphics state, preventing screen tearing and
        // choppy framerates, especially on macOS and Linux JVMs.
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Renders scaling-independent game visuals onto an off-screen BufferedImage
     * buffer.
     * This intermediate step prepares pixel-perfect crisp rendering before scaling
     * memory.
     */
    private void renderToBuffer() {
        Graphics2D g2 = frameBuffer.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        g2.setColor(new Color(60, 150, 60));
        g2.fillRect(0, 0, BASE_WIDTH, BASE_HEIGHT);

        camera.update(player.renderX, player.renderY, engine.getMapWidth(), engine.getMapHeight());
        Position cameraOffset = camera.getOffset();
        g2.translate(-cameraOffset.getX(), -cameraOffset.getY());

        // renderer.drawMap(g2, cam);

        if (level != null) {
            renderer.drawEntities(g2);
        }

        renderer.drawPlayer(g2, ticksSinceLastMove);

        g2.dispose();
    }

    /**
     * Renders all entities managed by the current level.
     *
     * @param g2 the graphics context
     */

    // Scaling the game to the screen
    /**
     * Draws the fully rendered off-screen image buffer directly to the window
     * frame.
     * Prevents blurring artifacts via Nearest Neighbor interpolation.
     *
     * @param g the base Graphics context bounds from the OS window
     */
    private void renderToScreen(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(frameBuffer, 0, 0, screenWidth, screenHeight, null);
    }

}
