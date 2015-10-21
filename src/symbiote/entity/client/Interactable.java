package symbiote.entity.client;

import java.awt.event.KeyEvent;

public interface Interactable {
    public void mouseEnter();
    public void mouseLeave();

    public void mouseClicked(int x, int y);
    public void mouseReleased(int x, int y);
    public void keyPressed(KeyEvent k);
    public void keyReleased(KeyEvent k);
}
