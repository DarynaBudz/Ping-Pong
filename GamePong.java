import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class GamePong extends JPanel implements KeyListener {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 60;
    private static final int BALL_SIZE = 10;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED = 3;

    private int paddle1Y;
    private int paddle2Y;
    private int ballX;
    private int ballY;
    private int ballXSpeed;
    private int ballYSpeed;
    private boolean isRunning;
    private Map<Integer, Boolean> keys;
    private Timer timer;

    private int player1Score;
    private int player2Score;

    public GamePong() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        keys = new HashMap<>();

        initGame();
    }

    private void initGame() {
        paddle1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        paddle2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;

        ballXSpeed = BALL_SPEED;
        ballYSpeed = BALL_SPEED;

        isRunning = true;

        timer = new Timer(10, e -> {
            update();
            repaint();
        });
        timer.start();
    }

    public void startGame() {
        isRunning = true;
    }

    private void update() {
        updatePaddles();
        updateBall();
        checkCollision();
    }

    private void updatePaddles() {
        if (isKeyDown(KeyEvent.VK_W) && paddle1Y > 0) {
            paddle1Y -= PADDLE_SPEED;
        } else if (isKeyDown(KeyEvent.VK_S) && paddle1Y < HEIGHT - PADDLE_HEIGHT) {
            paddle1Y += PADDLE_SPEED;
        }

        if (paddle2Y + PADDLE_HEIGHT / 2 < ballY + BALL_SIZE / 2 && paddle2Y < HEIGHT - PADDLE_HEIGHT) {
            paddle2Y += PADDLE_SPEED;
        } else if (paddle2Y + PADDLE_HEIGHT / 2 > ballY + BALL_SIZE / 2 && paddle2Y > 0) {
            paddle2Y -= PADDLE_SPEED;
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
            ballXSpeed = -ballXSpeed;
        } else if (ballX >= WIDTH - PADDLE_WIDTH - BALL_SIZE && ballY + BALL_SIZE >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT) {
            ballXSpeed = -ballXSpeed;
        } else if (ballX < 0 || ballX > WIDTH - BALL_SIZE) {
            if (ballX < 0) {
                player2Score++;
            } else {
                player1Score++;
            }
            resetBall();
        }
    }

    private void resetBall() {
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;

        ballXSpeed = BALL_SPEED;
        ballYSpeed = BALL_SPEED;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        g.fillRect(0, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        g.setColor(Color.GREEN);
        g.fillRect(WIDTH - PADDLE_WIDTH, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

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
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.put(e.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
