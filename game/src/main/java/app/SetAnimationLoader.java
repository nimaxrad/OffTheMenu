package app;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Loads a 3-frame horizontal sprite sheet from resources
 * and animates it by changing frames after a fixed number of ticks.
 * This class uses a set of 3, 32x32 pixel frames.
 */
public class SetAnimationLoader {

    private static final int SPRITE_WIDTH = 32;
    private static final int SPRITE_HEIGHT = 32;
    private static final int FRAME_COUNT = 3;

    private final BufferedImage[] frames;
    private final int ticksPerFrame;

    private int currentFrame;
    private int tickCounter;

    /**
     * Creates a loader/animator for a 3-frame sprite set.
     *
     * @param resourcePath path in resources, for example "/sprites/sleep.png"
     * @param ticksPerFrame number of ticks before changing frame
     * @throws IOException if the image cannot be loaded
     */
    public SetAnimationLoader(String resourcePath, int ticksPerFrame) throws IOException {
        if (ticksPerFrame <= 0) {
            throw new IllegalArgumentException("ticksPerFrame must be greater than 0.");
        }

        this.ticksPerFrame = ticksPerFrame;
        this.frames = new BufferedImage[FRAME_COUNT];
        this.currentFrame = 0;
        this.tickCounter = 0;

        BufferedImage spriteSheet = loadSpriteSheet(resourcePath);
        sliceFrames(spriteSheet);
    }

    /**
     * Loads the sprite sheet from resources.
     */
    private BufferedImage loadSpriteSheet(String resourcePath) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourcePath);

        if (stream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        BufferedImage spriteSheet = ImageIO.read(stream);

        if (spriteSheet == null) {
            throw new IOException("Failed to read image: " + resourcePath);
        }

        return spriteSheet;
    }

    /**
     * Cuts the sprite sheet into 3 frames.
     */
    private void sliceFrames(BufferedImage spriteSheet) {
        for (int i = 0; i < FRAME_COUNT; i++) {
            frames[i] = spriteSheet.getSubimage(
                    i * SPRITE_WIDTH,
                    0,
                    SPRITE_WIDTH,
                    SPRITE_HEIGHT
            );
        }
    }

    /**
     * Call this once every game tick.
     * Changes frame after the specified number of ticks.
     */
    public void update() {
        tickCounter++;

        if (tickCounter >= ticksPerFrame) {
            tickCounter = 0;
            currentFrame = (currentFrame + 1) % FRAME_COUNT;
        }
    }

    /**
     * Returns the current animation frame.
     */
    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }

    /**
     * Returns a specific frame: 0, 1, or 2.
     */
    public BufferedImage getFrame(int index) {
        if (index < 0 || index >= FRAME_COUNT) {
            throw new IllegalArgumentException("Index must be 0, 1, or 2.");
        }

        return frames[index];
    }

    /**
     * Resets animation back to the first frame.
     */
    public void reset() {
        currentFrame = 0;
        tickCounter = 0;
    }

    /**
     * Returns current frame index.
     */
    public int getCurrentFrameIndex() {
        return currentFrame;
    }
}