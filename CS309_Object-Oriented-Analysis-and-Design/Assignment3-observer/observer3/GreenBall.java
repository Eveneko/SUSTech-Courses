package observer3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GreenBall extends Ball {

    GreenBall(Color color, int xSpeed, int ySpeed, int ballSize, MainPanel mainPanel) {
        super(color, xSpeed, ySpeed, ballSize, mainPanel);
        mainPanel.registerObserver(this);
    }

    @Override
    public void update(char keyChar) {
        switch (keyChar) {
            case 'a':
                setXSpeed(Math.abs(getXSpeed()) * -1);
                break;
            case 'd':
                setXSpeed(Math.abs(getXSpeed()));
                break;
            case 'w':
                setYSpeed(Math.abs(getYSpeed()) * -1);
                break;
            case 's':
                setYSpeed(Math.abs(getYSpeed()));
                break;
        }
    }

    public void updatePosition(int x, int y) {
    }

}
