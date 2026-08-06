package audio;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import java.net.URL;


/**
 * An abstract object that manages the playback of audio files.
 * Contains an array of URLs which point to each audio file.
 *
 * @author Ken Tran
 * @see Music
 */
public abstract class Audio {
    //Nima: Weak encapsulation
    //Nima: These fields are package-private instead of private or protected
    //made some private

     Clip clip;
     URL audioURL[];
    private FloatControl fc;
     //for settings (implemented later)
    private int volumeScale = 3;
    private Clip[] clips;

    /**
     * Sets the current file to the i'th audio file in the URl array.
     * Loads the desired audio file and adjusts its volume.
     *
     * @param i the index of the file in the URL array
     */
    protected void setFile(int i){
        clip = clips[i];
        clip.setFramePosition(0); // rewind to start
        fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        checkVolume();
    }

    /**
     * Saves used audios in an array as creating a new Clip every time play()
     * is called is very expensive
     */
    protected void preloadClips() {
        clips = new Clip[audioURL.length];
        for (int i = 0; i < audioURL.length; i++) {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(audioURL[i]);
                clips[i] = AudioSystem.getClip();
                clips[i].open(ais);
                FloatControl fc = (FloatControl) clips[i].getControl(FloatControl.Type.MASTER_GAIN);
                fc.setValue(-15f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Determines the volume of the audio files based on the volumeScale.
     * volumeScale ranges from zero to five.
     * Sets the volume of this object.
     */
    private static final float[] GAIN_LEVELS = {-80f, -20f, -12f, -5f, 1f};

    public void checkVolume() {
        fc.setValue(GAIN_LEVELS[volumeScale]);
    }

    /**
     * Decrements the volumeScale by one.
     * Does not change anything if the volumeScale is less than zero.
     */
    public void lowerVolume() {
        if (volumeScale > 0) {
            volumeScale--;
        }
    }

    /**
     * Increments the volumeScale by one.
     * Does not change if the volumeScale is greater than five.
     */
    public void increaseVolume() {
        if (volumeScale < 4) {
            volumeScale++;
        }
    }

    /**
     * @return the current volumeScale of this object
     */
    public int getVolumeScale() {
        return volumeScale;
    }

}