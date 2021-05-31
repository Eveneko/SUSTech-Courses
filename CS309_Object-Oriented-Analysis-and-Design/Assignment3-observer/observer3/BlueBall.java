package observer3;

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

    public void updatePosition(int new_x, int new_y) {
        int x = getX(), y = getY();
        if (Math.hypot(new_x - x, new_y - y) < 120) {
            if (new_x >= x) setX(x - 30);
            else setX(x + 30);
            if (new_y >= y) setY(y - 30);
            else setY(y + 30);
        }
    }
}
