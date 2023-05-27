package pingpong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class GamePong extends JPanel implements KeyListener {
    private static final int WIDTH = 683;
    private static final int HEIGHT = 555;
    private static final int PADDLE_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 100;
    private static final int BALL_SIZE = 20;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_BASE_SPEED = 3;
    private static final double BALL_SPEED_INCREMENT = 0.3;
    private static final int BALL_TRAIL_MAX_LENGTH = 10;

    private int paddle1Y;
    private int paddle2Y;
    private int ballX;
    private int ballY;
    private double ballXSpeed;
    private double ballYSpeed;
    private Map<Integer, Boolean> keys;
    private Timer timer;

    private int player1Score;
    private int player2Score;

    private Point[] ballTrail;
    private Color BALL_TRAIL_COLOR_START;
    private Color BALL_TRAIL_COLOR_END;

    public GamePong() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        keys = new HashMap<>();
        keys.put(KeyEvent.VK_W, false);
        keys.put(KeyEvent.VK_S, false);
        keys.put(KeyEvent.VK_UP, false);
        keys.put(KeyEvent.VK_DOWN, false);

        initGame();
    }

    private void initGame() {
        paddle1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        paddle2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;

        ballXSpeed = BALL_BASE_SPEED;
        ballYSpeed = BALL_BASE_SPEED;


        ballTrail = new Point[BALL_TRAIL_MAX_LENGTH];
        BALL_TRAIL_COLOR_START = new Color(255, 255, 255, 100);
        BALL_TRAIL_COLOR_END = new Color(255, 255, 255, 0);

        timer = new Timer(10, e -> {
            update();
            repaint();
        });
        timer.start();
    }

    public void startGame() {
    }

    private void update() {
        updatePaddles();
        updateBall();
        checkCollision();
        updateBallTrail();
    }

    private void updatePaddles() {
        if (isKeyDown(KeyEvent.VK_W) && paddle1Y > 0) {
            paddle1Y -= PADDLE_SPEED;
        } else if (isKeyDown(KeyEvent.VK_S) && paddle1Y < HEIGHT - PADDLE_HEIGHT) {
            paddle1Y += PADDLE_SPEED;
        }

        if (isKeyDown(KeyEvent.VK_UP) && paddle2Y > 0) {
            paddle2Y -= PADDLE_SPEED;
        } else if (isKeyDown(KeyEvent.VK_DOWN) && paddle2Y < HEIGHT - PADDLE_HEIGHT) {
            paddle2Y += PADDLE_SPEED;
        }
    }

    private void updateBall() {
        ballX += ballXSpeed;
        ballY += ballYSpeed;

        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) {
            ballYSpeed = -ballYSpeed;
        }
    }

    private void checkCollision() {
        if (ballX <= PADDLE_WIDTH && ballY + BALL_SIZE >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) {
            double relativeIntersectY = (paddle1Y + PADDLE_HEIGHT / 2) - (ballY + BALL_SIZE / 2);
            double normalizedRelativeIntersectionY = relativeIntersectY / (PADDLE_HEIGHT / 2);
            double bounceAngle = normalizedRelativeIntersectionY * Math.PI / 4;
            ballXSpeed = Math.abs(ballXSpeed) + BALL_SPEED_INCREMENT;
            ballYSpeed = -BALL_BASE_SPEED * Math.sin(bounceAngle);
        } else if (ballX >= WIDTH - PADDLE_WIDTH - BALL_SIZE && ballY + BALL_SIZE >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT) {
            double relativeIntersectY = (paddle2Y + PADDLE_HEIGHT / 2) - (ballY + BALL_SIZE / 2);
            double normalizedRelativeIntersectionY = relativeIntersectY / (PADDLE_HEIGHT / 2);
            double bounceAngle = normalizedRelativeIntersectionY * Math.PI / 4;
            ballXSpeed = -Math.abs(ballXSpeed) - BALL_SPEED_INCREMENT;
            ballYSpeed = -BALL_BASE_SPEED * Math.sin(bounceAngle);
        } else if (ballX < 0 || ballX > WIDTH - BALL_SIZE) {
            if (ballX < 0) {
                player2Score++;
                ballXSpeed = -BALL_BASE_SPEED;
            } else {
                player1Score++;
                ballXSpeed = BALL_BASE_SPEED;
            }
            resetBall();
        }
    }

    private void resetBall() {
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;

        ballYSpeed = BALL_BASE_SPEED;
    }

    private void updateBallTrail() {
        for (int i = BALL_TRAIL_MAX_LENGTH - 1; i > 0; i--) {
            ballTrail[i] = ballTrail[i - 1];
        }
        ballTrail[0] = new Point(ballX + BALL_SIZE / 2, ballY + BALL_SIZE / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw middle line
        g.setColor(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);

        // Draw paddles
        g.setColor(Color.RED);
        g.fillRect(0, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        g.setColor(Color.GREEN);
        g.fillRect(WIDTH - PADDLE_WIDTH, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw ball trail
        for (int i = 0; i < BALL_TRAIL_MAX_LENGTH; i++) {
            if (ballTrail[i] == null) continue;
            int trailSize = BALL_SIZE - (BALL_SIZE / BALL_TRAIL_MAX_LENGTH) * i;
            int alpha = (50 / BALL_TRAIL_MAX_LENGTH) * (BALL_TRAIL_MAX_LENGTH - i);
            Color trailColor = new Color(
                    BALL_TRAIL_COLOR_START.getRed(),
                    BALL_TRAIL_COLOR_START.getGreen(),
                    BALL_TRAIL_COLOR_START.getBlue(),
                    alpha
            );
            g.setColor(trailColor);
            g.fillOval(ballTrail[i].x - trailSize / 2, ballTrail[i].y - trailSize / 2, trailSize, trailSize);
        }

        // Draw ball
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Draw scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Player 1: " + player1Score, 20, 30);
        g.drawString("Player 2: " + player2Score, WIDTH - 140, 30);
    }

    private boolean isKeyDown(int keyCode) {
        return keys.getOrDefault(keyCode, false);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys.put(e.getKeyCode(), true);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.put(e.getKeyCode(), false);
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong");
        GamePong game = new GamePong();
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.startGame();
    }
}

