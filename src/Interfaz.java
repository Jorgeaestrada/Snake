import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Interfaz extends JPanel implements Runnable{

    private Thread thread;
    private Snake snake;
    private static Food food;

    private int xAxis = 1;
    private int yAxis = 0;

    private static final int X_SIZE = 500;
    private static final int Y_SIZE = 500;
    private final int DELAY = 75;

    public Interfaz() {
        snake = new Snake();
        food = new Food();

        thread = new Thread(this);
        thread.start();

        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();
        setPreferredSize(new Dimension(X_SIZE, Y_SIZE));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.RED);
        //add last && remove first
        for (int i = 0; i < snake.getSize(); i++) {
            Point p = snake.getIndex(i);
            g.fillRect(p.x, p.y, 10, 10);
        }

        g.setColor(Color.WHITE);
        Point foodPoint = food.getPoint();
        g.fillRect(foodPoint.x, foodPoint.y,10,10);

        Point p = snake.getLast();
        snake.newPoint(p.x, p.y,xAxis,yAxis);
        snake.removeFirst();
        Toolkit.getDefaultToolkit().sync();
    }


    @Override
    public void run() {

        long beforeTime, timeDifference, sleep;
        beforeTime = System.currentTimeMillis();

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_UP && yAxis != 1) {
                    xAxis = 0;
                    yAxis = -1;
                } else if (key == KeyEvent.VK_DOWN && yAxis != -1) {
                    xAxis = 0;
                    yAxis = 1;
                } else if (key == KeyEvent.VK_LEFT && xAxis != 1) {
                    xAxis = -1;
                    yAxis = 0;
                } else if (key == KeyEvent.VK_RIGHT && xAxis != -1) {
                    xAxis = 1;
                    yAxis = 0;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        while (true) {
            repaint();
            timeDifference = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDifference;

            if (food.getPoint().equals(snake.getLast())) {
                snake.addLast(food.getPoint());

                food.generateFood();
            }


            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            beforeTime = System.currentTimeMillis();
        }
    }
}