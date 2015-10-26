package symbiote.entity.client;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface Interactable {
    public void mouseEnter();
    public void mouseLeave();

    public void mouseClicked(int x, int y, MouseEvent m);
    public void mouseHeld(int x, int y, MouseEvent m);
    public void mouseReleased(int x, int y, MouseEvent m);
    public void keyPressed(KeyEvent k);
    public void keyReleased(KeyEvent k);
}
