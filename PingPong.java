import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PingPong{

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> startWindow());
    }

    private static void startWindow() {
        // Вікно (я взяла 700х600, можем поміняти)
        JFrame frame = new JFrame("Ping Pong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);

        // Панелька (я думаю чорний фон норм, але то тоже якщо що змінимо)
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.black);


        // Лейбл з гіфкою
        String gifPath = "images/get-real-cat.gif";      //шлях до гіфки(далі я її трошки зменшила,90х90)
        ImageIcon gifIcon = new ImageIcon(gifPath);

        Image gifImage = gifIcon.getImage();

        int newWidth = 90;
        int newHeight = 90;

        Image resizedImage = gifImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);

        // тут вже зменшений розмір
        ImageIcon resizedGifIcon = new ImageIcon(resizedImage);

        JLabel label = new JLabel("Welcome to Ping Pong!", resizedGifIcon, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(Color.white);

        // Кнопка старт
        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.black);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //ну тут вже буде двіж при натисканні на кнопку
            }
        });
        //додаєм все на панель
        panel.add(Box.createVerticalGlue()); // Вирівнюєм вертикально текст і кнопку
        panel.add(label);
        panel.add(Box.createVerticalStrut(20)); // це просто відстань між кнопкою і написом
        panel.add(startButton);
        panel.add(Box.createVerticalGlue());

        frame.getContentPane().add(panel);

        frame.setVisible(true);
    }
}
