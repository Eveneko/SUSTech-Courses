package observer2;

import java.awt.*;

public class RedBall extends Ball {

    RedBall(Color color, int xSpeed, int ySpeed, int ballSize, MainPanel mainPanel) {
        super(color, xSpeed, ySpeed, ballSize, mainPanel);
        mainPanel.registerObserver(this);
    }

    @Override
    public void update(char keyChar) {
        if (keyChar == 'a' || keyChar == 'd') {
            int temp = getXSpeed();
            setXSpeed(getYSpeed());
            setYSpeed(temp);
        }
    }
}
