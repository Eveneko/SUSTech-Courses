package observer3;

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

    @Override
    public void updatePosition(int new_x, int new_y) {
        int x = getX(), y = getY();
        if (Math.hypot(new_x - x, new_y - y) < 100) {
            if (new_x >= x) setX(x - 50);
            else setX(x + 50);
            if (new_y >= y) setY(y - 50);
            else setY(y + 50);
        }
    }
}
