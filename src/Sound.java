import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound
{
  
  
  private Clip clip;

  public void Sound() {
    
  }
  
  public void playSound() {
    try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:/Users/Tobias/Documents/Eclipse/Projects/Biofeedback/sound/click.wav").getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    } catch(Exception ex) {
        System.out.println("Error with playing sound.");
        ex.printStackTrace();
    }
  }
}
