package audio;

import java.net.URL;
import javax.sound.sampled.FloatControl;

/**
 * Controls and manages sound effects for the game.
 * Sound effects are accessed by indexing an array which contains the URLs of the sound files.
 *
 * @author Ken Tran
 * @see Audio
 */
public class SoundEffects extends Audio {
    private static SoundEffects soundInstance = null;

    /**
     * Instantiates a new SoundEffects object and saves sound effect files to a URL array.
     * This audio array can be resized as more sound effects are added.
     * Volume of the sound effects are also loaded from the configuration file.
     */
    private SoundEffects() {
        super();

        //increase list if needed
        audioURL = new URL[7];
        audioURL[0] = getClass().getResource("/sounds/sfx_pickup.wav");
        audioURL[1] = getClass().getResource("/sounds/sfx_pigdeath.wav");
        audioURL[2] = getClass().getResource("/sounds/sfx_zap.wav");
        audioURL[3] = getClass().getResource("/sounds/winmusic.wav");
        audioURL[4] = getClass().getResource("/sounds/losemusic.wav");
        audioURL[5] = getClass().getResource("/sounds/select.wav");
        audioURL[6] = getClass().getResource("/sounds/enter.wav");

        preloadClips();
//        volumeScale = settings.getSoundVolume();
    }

    /**
     * @return singleton instance of SoundEffects
     */
    public static SoundEffects getInstance() {
        if (soundInstance == null) {
            soundInstance = new SoundEffects();
        }

        return soundInstance;
    }

    /**
     * Plays the sound effect at position index in the URL array.
     *
     * @param index index of the file in URL array
     */
    public void play(int index){
        setFile(index);
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(-15f);
        clip.start();
    }
}