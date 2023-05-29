package pingpong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;
import javax.swing.JOptionPane;


public class GamePong extends JPanel implements KeyListener {
    private static final int WIDTH = 683;
    private static final int HEIGHT = 555;
    private static final int PADDLE_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 100;
    private static final int BALL_SIZE = 20;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_BASE_SPEED = 3;
    private static final double BALL_SPEED_INCREMENT = 0.001;
    private static final int BALL_TRAIL_MAX_LENGTH = 10;

    public static final int MODE_BOT = 1;
    public static final int MODE_NORMAL = 2;
    public static final int MODE_ACCELERATED = 3;

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

    private boolean isPaused;
    private int gameMode;

    


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

    public void setGameMode(int mode) {
        gameMode = mode;
    }

    public void startGame() {
        if (!timer.isRunning()) {
            timer.start();
            isPaused = false;
            repaint();
        }
    }

    private void pauseGame() {
        if (timer.isRunning()) {
            timer.stop();
            isPaused = true;
            repaint();
        }
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

        if (gameMode == MODE_BOT) {
            // Bot mode: Control the second paddle automatically
            updateBotPaddle();
        } else {
            // Player mode: Control the second paddle based on user input
            if (isKeyDown(KeyEvent.VK_UP) && paddle2Y > 0) {
                paddle2Y -= PADDLE_SPEED;
            } else if (isKeyDown(KeyEvent.VK_DOWN) && paddle2Y < HEIGHT - PADDLE_HEIGHT) {
                paddle2Y += PADDLE_SPEED;
            }
        }
    }

    private void updateBotPaddle() {
    int paddle2CenterY = paddle2Y + PADDLE_HEIGHT / 2;
    int ballCenterY = ballY + BALL_SIZE / 2;

    if (paddle2CenterY < ballCenterY && paddle2Y < HEIGHT - PADDLE_HEIGHT) {
        paddle2Y += Math.ceil((ballCenterY - paddle2CenterY) / 10.0); // Smooth movement
    } else if (paddle2CenterY > ballCenterY && paddle2Y > 0) {
        paddle2Y -= Math.ceil((paddle2CenterY - ballCenterY) / 10.0); // Smooth movement
    }
}


   private void updateBall() {
    ballX += ballXSpeed;
    ballY += ballYSpeed;

    if (gameMode == MODE_ACCELERATED) {
        // Accelerated mode: Increase ball speed with each hit until collision with paddle
        if (!isBallCollidingWithPaddle()) {
            ballXSpeed += (ballXSpeed > 0) ? BALL_SPEED_INCREMENT : -BALL_SPEED_INCREMENT;
            ballYSpeed += (ballYSpeed > 0) ? BALL_SPEED_INCREMENT : -BALL_SPEED_INCREMENT;
        }
    }

}

private boolean isBallCollidingWithPaddle() {
    if (ballXSpeed > 0) {
        // Ball is moving to the right, check collision with the right paddle
        return ballX + BALL_SIZE >= WIDTH - PADDLE_WIDTH && ballY + BALL_SIZE >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT;
    } else {
        // Ball is moving to the left, check collision with the left paddle
        return ballX <= PADDLE_WIDTH && ballY + BALL_SIZE >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT;
    }
    
}

    private void checkCollision() {
        if (ballX <= PADDLE_WIDTH && ballY + BALL_SIZE >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) {
            ballXSpeed = Math.abs(ballXSpeed);
        } else if (ballX + BALL_SIZE >= WIDTH - PADDLE_WIDTH && ballY + BALL_SIZE >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT) {
            ballXSpeed = -Math.abs(ballXSpeed);
        }
        if (ballYSpeed > 0) {
        ballYSpeed = Math.abs(ballYSpeed);
        } else {
        ballYSpeed = -Math.abs(ballYSpeed);
        }

        if (ballY <= 0 || ballY + BALL_SIZE >= HEIGHT) {
            ballYSpeed = -ballYSpeed;
        }

        if (ballX <= 0) {
            player2Score++;
            resetBall();
            resetPlatforms(); // Reset the platforms to their original positions
            ballXSpeed = -Math.abs(ballXSpeed); // Change the ball direction towards the losing platform
        } else if (ballX + BALL_SIZE >= WIDTH) {
            player1Score++;
            resetBall();
            resetPlatforms(); // Reset the platforms to their original positions
            ballXSpeed = +Math.abs(ballXSpeed); // Change the ball direction towards the losing platform
        }

        // Check if any player reached 10 points
        if (player1Score >= 10 || player2Score >= 10) {
            pauseGame();
            String winner = (player1Score >= 10) ? "Player 1" : "Player 2";
            int choice = JOptionPane.showConfirmDialog(this, "Congratulations! " + winner + " won the game!\n\nOne more game?", "Game Over", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }
    

    private void resetPlatforms() {
        paddle1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
        paddle2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    }


    private void resetBall() {
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballXSpeed = BALL_BASE_SPEED;
        ballYSpeed = BALL_BASE_SPEED;
    }
    

    private void resetGame() {
        player1Score = 0;
        player2Score = 0;
        resetBall();
        startGame();
    }
    

    private void updateBallTrail() {
        for (int i = BALL_TRAIL_MAX_LENGTH - 1; i > 0; i--) {
            ballTrail[i] = ballTrail[i - 1];
        }
        ballTrail[0] = new Point(ballX + BALL_SIZE / 2, ballY + BALL_SIZE / 2);
    }

    private boolean isKeyDown(int keyCode) {
        return keys.getOrDefault(keyCode, false);
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
            int trailSize = BALL_SIZE - (BALL_SIZE / BALL_TRAIL_MAX_LENGTH * i);
            int alpha = (50 / BALL_TRAIL_MAX_LENGTH) * (BALL_TRAIL_MAX_LENGTH - i);
            Color trailColor = new Color(BALL_TRAIL_COLOR_START.getRed(), BALL_TRAIL_COLOR_START.getGreen(), BALL_TRAIL_COLOR_START.getBlue(), alpha);
            g.setColor(trailColor);
            g.fillOval(ballTrail[i].x - trailSize / 2, ballTrail[i].y - trailSize / 2, trailSize, trailSize);
        }

        // Draw ball
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Draw scores
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Player 1: " + player1Score, 20, 20);
        g.drawString("Player 2: " + player2Score, WIDTH - 110, 20);


        // Pause text
        if (isPaused) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics fontMetrics = g.getFontMetrics();
            String pauseText = "Game Paused";
            int pauseTextWidth = fontMetrics.stringWidth(pauseText);
            int pauseTextHeight = fontMetrics.getHeight();
            g.drawString(pauseText, WIDTH / 2 - pauseTextWidth / 2, HEIGHT / 2 - pauseTextHeight / 2);
        }
        // At the end of the method, add the following code

            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.setColor(Color.WHITE);
            g.drawString("Esc - Exit", 10, getHeight() - 20);



            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.setColor(Color.WHITE);
            g.drawString("Space - Pause", getWidth() - 100, getHeight() - 20);


     
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    if (keys.containsKey(e.getKeyCode())) {
        keys.put(e.getKeyCode(), true);
    }

    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        if (isPaused) {
            startGame();
        } else {
            pauseGame();
        }
    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit the game?", "Exit Game", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}

    @Override
    public void keyReleased(KeyEvent e) {
        if (keys.containsKey(e.getKeyCode())) {
            keys.put(e.getKeyCode(), false);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        GamePong game = new GamePong();
        game.setGameMode(GamePong.MODE_NORMAL); // Set the game mode
        frame.add(game, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.startGame();
        
    }
}
