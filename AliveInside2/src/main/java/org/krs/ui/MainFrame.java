package org.krs.ui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class MainFrame extends JFrame {

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    DefaultTableModel tableModel;
    JTable table;

    JTextField textField;


    public MainFrame() {
        setTitle("Temperature Management App");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel.add(splashScreen(), "SPLASH");
        /*mainPanel.add(mainMenu(), "MENU");
        mainPanel.add(converterPage(), "CONVERTER");
        mainPanel.add(tablePage(), "TABLE");*/

        add(mainPanel);
        cardLayout.show(mainPanel, "Splash");

        showLoading();
    }

    JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(Color.LIGHT_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    void showLoading() {
        Timer t = new Timer(2500, e -> cardLayout.show(mainPanel, "MENU"));
        t.setRepeats(false);
        t.start();
    }


    JPanel splashScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        JLabel title = new JLabel("KRS", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setForeground(Color.WHITE);

        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);

        panel.add(title, BorderLayout.CENTER);
        panel.add(bar, BorderLayout.SOUTH);

        return panel;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

}


