pupackage observer2;

import java.awt.*;

public class BlueBall extends Ball {

    BlueBall(Color color, int xSpeed, int ySpeed, int ballSize, MainPanel mainPanel) {
        super(color, xSpeed, ySpeed, ballSize, mainPanel);
        mainPanel.registerObserver(this);
    }

    @Override
    public void update(char keyChar) {
        setXSpeed(-1 * getXSpeed());
        setYSpeed(-1 * getYSpeed());
    }
}
