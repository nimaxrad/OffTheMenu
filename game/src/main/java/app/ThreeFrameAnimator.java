package app;

import java.awt.image.BufferedImage;

/**
 * Handles animation for a 3-frame sprite sheet.
 *
 */
public class ThreeFrameAnimator {

    private final BufferedImage[] frames;

    private int frameIndex = 0;
    private int tickCounter = 0;

    private final int ticksPerFrame;

    /**
     * @param frames must be exactly 3 frames
     * @param ticksPerFrame how long each frame stays (e.g., 50)
     */
    public ThreeFrameAnimator(BufferedImage[] frames, int ticksPerFrame) {
        if (frames == null || frames.length != 3) {
            throw new IllegalArgumentException("Must provide exactly 3 frames.");
        }

        this.frames = frames;
        this.ticksPerFrame = ticksPerFrame;
    }

    /**
     * Call this every game tick
     */
    public void update() {
        tickCounter++;

        if (tickCounter >= ticksPerFrame) {
            tickCounter = 0;

            // cycle: 0 -> 1 -> 2 -> 0
            frameIndex = (frameIndex + 1) % 3;
        }
    }

    /**
     * Get current frame
     */
    public BufferedImage getFrame() {
        return frames[frameIndex];
    }

    /**
     * restart animation
     */
    public void reset() {
        frameIndex = 0;
        tickCounter = 0;
    }
}