package audio;
import java.net.URL;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Clip;

/**
 * Manages and controls music tracks used by this game.
 * Music files can be accessed by indexing an array which contains the URLs of the files.
 *
 * @author Ken Tran
 * @see Audio
 */
public class Music extends Audio {
    private static Music musicInstance = null;
    private int activeIndex = -1;

    /**
     * Constructs a new Music object and saves the music files to a URL array.
     * This audio array can be resized as more music tracks are needed.
     * Volume of the music is also loaded from the configuration file.
     *
     */
    private Music() {
        super();

        audioURL = new URL[3];
        audioURL[0] = getClass().getResource("/sounds/sfx_background_music.wav");
        audioURL[1] = getClass().getResource("/sounds/winmusic.wav");
        audioURL[2] = getClass().getResource("/sounds/losemusic.wav");

        preloadClips();
        // volumeScale = settings.getMusicVolume();
    }

    /**
     * @return singleton instance of Music
     */
    public static Music getInstance() {
        if (musicInstance == null) {
            musicInstance = new Music();
        }

        return musicInstance;
    }

    /**
     * Plays the music file at position index in the URL array.
     * Music track is loops until the stop method is called.
     *
     * @param index index of the file in URL array
     */
    public void play(int index) {
        if (clip != null && clip.isActive() && activeIndex == index) {
            return;
        }
        if (clip != null && clip.isActive()) {
            clip.stop();
        }

        activeIndex = index;
        setFile(index);

        // Set volume lower
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(-20f); // -20 decibels, lower = quieter. Range is usually -80 to +6

        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    /**
     * Stops the music file that is currently being played.
     */
    public void stop() {
        if (clip != null) {
            clip.stop();
            activeIndex = -1;
        }
    }
}