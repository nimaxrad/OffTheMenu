package app;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * Loads a horizontal sprite sheet (3 frames) from resources.
 */
public class SetSpriteLoader {

    private BufferedImage spriteSheet;

    private static final int SPRITE_WIDTH = 32;
    private static final int SPRITE_HEIGHT = 32;
    private static final int FRAME_COUNT = 3;

    /**
     * @param resourcePath
     */
    public SetSpriteLoader(String resourcePath) throws IOException {
        spriteSheet = ImageIO.read(
                getClass().getResourceAsStream(resourcePath)
        );

        if (spriteSheet == null) {
            throw new IOException("Failed to load resource: " + resourcePath);
        }
    }

    /**
     * Returns one frame (0, 1, or 2)
     */
    public BufferedImage getSprite(int index) {
        if (index < 0 || index >= FRAME_COUNT) {
            throw new IllegalArgumentException("Index must be 0, 1, or 2");
        }

        return spriteSheet.getSubimage(
                index * SPRITE_WIDTH,
                0,
                SPRITE_WIDTH,
                SPRITE_HEIGHT
        );
    }

    /**
     * Returns all frames
     */
    public BufferedImage[] getAllSprites() {
        BufferedImage[] sprites = new BufferedImage[FRAME_COUNT];

        for (int i = 0; i < FRAME_COUNT; i++) {
            sprites[i] = getSprite(i);
        }

        return sprites;
    }
}