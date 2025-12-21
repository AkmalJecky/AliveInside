package org.krs.ui;

import org.krs.model.Mahasiswa;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    private Mahasiswa currentStudent;

    private PanelPilihMatkulB panelPilihB;
    private PanelKelasB panelKrsB;
    private PanelKelasA panelKrsA;
    private LoginPanel masuk;

    public MainFrame() {
        setTitle("Sistem KRS");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        masuk = new LoginPanel(this);
        panelKrsA = new PanelKelasA(this);
        panelKrsB = new PanelKelasB(this);
        panelPilihB = new PanelPilihMatkulB(this);

        mainPanel.add(splashScreen(), "SPLASH");
        mainPanel.add(masuk, "LOGIN");
        mainPanel.add(panelKrsA, "KRS_A");
        mainPanel.add(panelKrsB, "KRS_B");
        mainPanel.add(panelPilihB, "PILIH_B");

        add(mainPanel);
        cardLayout.show(mainPanel, "SPLASH");
        showLoading();
    }

    public void showPage(String name) {
        if ("PILIH_B".equals(name) && panelPilihB != null) {
            panelPilihB.refreshMahasiswaInfo();
        } else if ("KRS_A".equals(name) && panelKrsA != null) {
            panelKrsA.refreshMahasiswaInfo();
        }
        cardLayout.show(mainPanel, name);
    }

    public void setCurrentStudent(Mahasiswa m) {
        this.currentStudent = m;
    }

    public Mahasiswa getCurrentStudent() {
        return currentStudent;
    }

    public PanelPilihMatkulB getPanelPilihB() {
        return panelPilihB;
    }

    public PanelKelasB getPanelKrsB() {
        return panelKrsB;
    }

    JPanel splashScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        JLabel title = new JLabel("Please Wait", JLabel.CENTER);
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

}