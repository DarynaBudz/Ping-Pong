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
        JPanel panel = new JPanel(new GridBagLayout()) {
            // Переопределение метода paintComponent для рисования фонового изображения
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Загрузка изображения фона
                String backgroundPath = "images\\background2.jpg";
                ImageIcon backgroundImage = new ImageIcon(backgroundPath);
                // Масштабирование изображения фона
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        panel.setBackground(Color.black);

        // Лейбл з гіфкою
        String gifPath = "images\\get-real-cat.gif";
        ImageIcon gifIcon = new ImageIcon(gifPath);

        Image gifImage = gifIcon.getImage();
        int newWidth = 90;
        int newHeight = 90;
        Image resizedImage = gifImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        ImageIcon resizedGifIcon = new ImageIcon(resizedImage);

        JLabel Label2 = new JLabel("Choose a game mode:");
        Label2.setFont(new Font("Century Gothic", Font.BOLD, 15));
        Label2.setForeground(Color.white);
        JLabel gifLabel = new JLabel(resizedGifIcon);
        gifLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Радіокнопки для вибору режиму гри
        botModeButton = new JRadioButton("Game vs bot");
        normalModeButton = new JRadioButton("Normal mode");
        acceleratedModeButton = new JRadioButton("Mode with acceleration");

        // Групуємо радіокнопки, щоб вибрати лише одну одночасно
        Font radioButtonFont = new Font("Century Gothic", Font.BOLD, 15);
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
        JButton startButton = new JButton();
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setOpaque(false);
        startButton.setIcon(new ImageIcon("images\\button2.png"));
        startButton.setRolloverIcon(new ImageIcon("images\\button.png"));
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButtonActionPerformed(e);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 0, 0); // Зазори між елементами
        gbc.gridy = 0;
        panel.add(gifLabel, gbc);
        gbc.gridy = 1;
        panel.add(Label2, gbc);
        gbc.gridy = 2;

        // Додамо радіокнопки до панелі
        gbc.gridy = 3;
        panel.add(botModeButton, gbc);
        gbc.gridy = 4;
        panel.add(normalModeButton, gbc);
        gbc.gridy = 5;
        panel.add(acceleratedModeButton, gbc);

        gbc.gridy = 6;
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
