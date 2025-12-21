package org.krs.ui;

import org.krs.model.Mahasiswa;
import org.krs.repository.MahasiswaCsvRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginPanel extends JPanel {

    private final MainFrame mainFrame;
    private JTextField tfNim;
    private JTextField tfNama;
    private final MahasiswaCsvRepository mhsRepo = new MahasiswaCsvRepository(); // repo baru baca data/mahasiswa.csv

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private JButton styledButton() {
        JButton btn = new JButton("Masuk") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.black);
                Shape round = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 25, 25);
                g2.fill(round);
                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            public void setContentAreaFilled(boolean b) {
                // ignore default background
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.DARK_GRAY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setOpaque(false);
        return btn;
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(0x0D47A1));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Login Mahasiswa", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0x1A237E));

        tfNim = new JTextField(20);
        tfNama = new JTextField(20);

        JButton btnLogin = styledButton();
        btnLogin.addActionListener(e -> doLogin());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        card.add(new JLabel("NIM"), gbc);
        gbc.gridx = 1;
        card.add(tfNim, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        card.add(new JLabel("Nama"), gbc);
        gbc.gridx = 1;
        card.add(tfNama, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        card.add(btnLogin, gbc);

        add(card, new GridBagConstraints());
    }

    private void doLogin() {
        String nim = tfNim.getText().trim();
        String nama = tfNama.getText().trim();

        if (nim.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM dan Nama tidak boleh kosong");
            return;
        }

        String kelas = mhsRepo.getKelasByNimAndNama(nim, nama); // baca dari data/mahasiswa.csv
        if (kelas == null) {
            JOptionPane.showMessageDialog(this,
                    "Data tidak ditemukan / NIM dan Nama tidak cocok");
            return;
        }

        int semester;
        int maxSks;

        if (kelas.equalsIgnoreCase("A")) {
            semester = 2;   // Kelas A = Semester 2
            maxSks = 24;    // contoh
        } else if (kelas.equalsIgnoreCase("B")) {
            semester = 3;   // Kelas B = Semester 3
            maxSks = 24;    // contoh
        } else {
            JOptionPane.showMessageDialog(this, "Kelas tidak valid di CSV (harus A/B)");
            return;
        }

        Mahasiswa m = new Mahasiswa(nim, nama, semester, maxSks);
        mainFrame.setCurrentStudent(m);

        if (kelas.equalsIgnoreCase("A")) {
            mainFrame.setTitle("Sistem KRS - Kelas A : " + nim + " - " + nama);
            mainFrame.showPage("KRS_A");
        } else { // kelas B
            mainFrame.setTitle("Sistem KRS - Kelas B : " + nim + " - " + nama);
            mainFrame.showPage("PILIH_B");
        }
    }
}