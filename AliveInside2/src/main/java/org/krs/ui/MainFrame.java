package org.krs.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    public MainFrame() {
        setTitle("Sistem KRS");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        LoginPanel masuk =  new LoginPanel(this);
        PanelKelasA panelKrsA = new PanelKelasA(this);
        PanelKelasB panelKrsB = new PanelKelasB(this);

        mainPanel.add(splashScreen(), "SPLASH");
        mainPanel.add(panelKrsA, "KRS_A");
        mainPanel.add(masuk, "LOGIN");
        mainPanel.add(panelKrsB, "KRS_B");

        add(mainPanel);
        showPage("LOGIN");

        cardLayout.show(mainPanel, "SPLASH");

        showLoading();
    }

    public void showPage(String name) {
        cardLayout.show(mainPanel, name);
    }


    JPanel splashScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        JLabel title = new JLabel("Temperature App", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setForeground(Color.WHITE);

        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);

        panel.add(title, BorderLayout.CENTER);
        panel.add(bar, BorderLayout.SOUTH);

        return panel;
    }

    void showLoading() {
        Timer t = new Timer(2500, e -> cardLayout.show(mainPanel, "LOGIN"));
        t.setRepeats(false);
        t.start();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
