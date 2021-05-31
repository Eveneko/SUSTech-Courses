package observer3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends JPanel implements KeyListener {

    private List<Ball> paintingBallList = new ArrayList<>();
    private List<Ball> observers = new ArrayList<>();
    private boolean start = false;
    private int score = 0;
    private Ball redBall;
    private Ball greenBall;
    private Ball blueBall;


    public MainPanel() {
        redBall = new RedBall(Color.RED, 3, 10, 50, this);
        greenBall = new GreenBall(Color.GREEN, 5, 7, 100, this);
        blueBall = new BlueBall(Color.BLUE, 8, 10, 80, this);
        greenBall.registerObserver(redBall);
        greenBall.registerObserver(blueBall);
        paintingBallList.add(redBall);
        paintingBallList.add(greenBall);
        paintingBallList.add(blueBall);

        // WHAT GOES HERE?
        // You need to make it possible for the app to get the keyboard values.
        addKeyListener(this);
        setFocusable(true);
        setPreferredSize(new Dimension(600, 600));

        Timer t = new Timer(20, e -> moveBalls());
        t.start();
    }

    public void setPaintingBallList(List<Ball> paintingBallList) {
        this.paintingBallList = paintingBallList;
    }

    public void moveBalls() {
        for (Ball b : paintingBallList) {
            b.move();
        }

        // collision detection
        for (int i = 0; start && i < paintingBallList.size(); i++) {
            if (paintingBallList.get(i).getColor()==Color.BLUE) {
                for (int j = 0; j < paintingBallList.size(); j++) {
                    if(paintingBallList.get(j).getColor()==Color.RED){
                        Ball ball1 = paintingBallList.get(i);
                        Ball ball2 = paintingBallList.get(j);
                        if(ball1.isIntersect(ball2)){
                            ball1.setVisible(false);
                            ball2.setVisible(false);
                        }
                    }
                }
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int visibleNum = 0;
        for (Ball b : paintingBallList) {
            if (b.isVisible()) {
                b.draw(g);
                visibleNum++;
            }
        }

        if (visibleNum <= 1) {
            g.setFont(new Font("Arial", Font.PLAIN, 75));
            for (int i = 70; i < 600; i += 100) {
                g.setColor(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
                g.drawString("Game Over!", 100, i);
            }
        } else if (start) {
            score += visibleNum;
        }

        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 20, 40);

        this.setBackground(Color.WHITE);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        char keyChar = keyEvent.getKeyChar();
        notifyObservers(keyChar);
        if (keyChar == ' ') {
            start = !start;
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }

    public void registerObserver(Ball ball) {
        observers.add(ball);
    }

    public void removeObserver(Ball ball) {
        observers.remove(ball);
    }

    public void notifyObservers(char keyChar) {
        for (var o : observers) {
            o.update(keyChar);
        }
    }

}
