package symbiote.resources;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioHandler {

    public static void playSound(String url) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(AudioHandler.class.getResource(url));
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {}
    }
}
