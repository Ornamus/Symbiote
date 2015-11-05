package symbiote.resources;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import symbiote.misc.Log;

public class AudioUtil {

    public static void playSound(String url) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(AudioUtil.class.getResource(url));
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            Log.e("AudioUtil.playSound(" + url + ") exception:\n" + e.getMessage());
        }
    }
}
