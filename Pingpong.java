package pingpong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pingpong {
    private static JFrame frame;
    private static JRadioButton botModeButton;
    private static JRadioButton normalModeButton;
    private static JRadioButton acceleratedModeButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Pingpong());
    }

    public Pingpong() {
        frame = new JFrame("Ping Pong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);

        // Шлях до файлу з іконкою
        String iconPath = "images\\icon.png";
        ImageIcon icon = new ImageIcon(iconPath);

        // Встановлюємо іконку для фрейму
        frame.setIconImage(icon.getImage());

        // Панель
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.black);

        // Лейбл з гіфкою
        String gifPath = "images\\get-real-cat.gif";
        ImageIcon gifIcon = new ImageIcon(gifPath);

        Image gifImage = gifIcon.getImage();
        int newWidth = 90;
        int newHeight = 90;
        Image resizedImage = gifImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        ImageIcon resizedGifIcon = new ImageIcon(resizedImage);

        JLabel label = new JLabel("Welcome to Ping Pong!");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.white);
        
        JLabel Label2 = new JLabel("Choose a game mode:");
        Label2.setFont(new Font("Arial", Font.BOLD, 15));
        Label2.setForeground(Color.white);
        JLabel gifLabel = new JLabel(resizedGifIcon);
        gifLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Радіокнопки для вибору режиму гри
        botModeButton = new JRadioButton("Game vs bot");
        normalModeButton = new JRadioButton("Normal mode");
        acceleratedModeButton = new JRadioButton("Mode with acceleration");

        // Групуємо радіокнопки, щоб вибрати лише одну одночасно
        Font radioButtonFont = new Font("Arial", Font.PLAIN, 15);
        Color radioButtonTextColor = Color.white;
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(botModeButton);
        botModeButton.setFont(radioButtonFont);
        botModeButton.setForeground(radioButtonTextColor);
        botModeButton.setOpaque(false);
        buttonGroup.add(normalModeButton);
        normalModeButton.setFont(radioButtonFont);
        normalModeButton.setForeground(radioButtonTextColor);
        normalModeButton.setOpaque(false);
        buttonGroup.add(acceleratedModeButton);
        acceleratedModeButton.setFont(radioButtonFont);
        acceleratedModeButton.setForeground(radioButtonTextColor);
        acceleratedModeButton.setOpaque(false);

        // Кнопка старт
        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.BLACK);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButtonActionPerformed(e);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Зазори між елементами
        gbc.gridy = 0;
        panel.add(label, gbc);
        gbc.gridy = 1;
        panel.add(gifLabel, gbc);
        gbc.gridy = 2;
        panel.add(Label2, gbc);
        gbc.gridy = 3;

        // Додамо радіокнопки до панелі
        gbc.gridy = 4;
        panel.add(botModeButton, gbc);
        gbc.gridy = 5;
        panel.add(normalModeButton, gbc);
        gbc.gridy = 6;
        panel.add(acceleratedModeButton, gbc);

        gbc.gridy = 7;
        panel.add(startButton, gbc);

        frame.getContentPane().add(panel);

        frame.setVisible(true);
    }

    private static void startButtonActionPerformed(ActionEvent evt) {
        frame.getContentPane().removeAll();
        frame.repaint();

        GamePong game = new GamePong();

        // Отримуємо вибраний режим гри
        int selectedMode = getSelectedMode();
        game.setGameMode(selectedMode);

        frame.add(game);
        game.requestFocusInWindow();
        game.startGame();
        frame.revalidate();
    }

    private static int getSelectedMode() {
        // Отримуємо вибраний режим гри з радіокнопок
        if (botModeButton.isSelected()) {
            return GamePong.MODE_BOT;
        } else if (normalModeButton.isSelected()) {
            return GamePong.MODE_NORMAL;
        } else if (acceleratedModeButton.isSelected()) {
            return GamePong.MODE_ACCELERATED;
        } else {
            // За замовчуванням повертаємо звичайний режим
            return GamePong.MODE_NORMAL;
        }
    }
}
