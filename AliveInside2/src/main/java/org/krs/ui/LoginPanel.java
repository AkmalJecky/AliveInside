package org.krs.ui;

import org.krs.model.Mahasiswa;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginPanel extends JPanel {

    private final MainFrame mainFrame;
    private JTextField tfNim;
    private JTextField tfNama;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    // Tombol dengan sudut melengkung
    private JButton styledButton() {
        JButton btn = new JButton("Masuk") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // warna background tombol
                g2.setColor(Color.black);
                Shape round = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 25, 25);
                g2.fill(round);

                // gambar teks
                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            public void setContentAreaFilled(boolean b) {
                // ignore supaya tidak menggambar background default
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
        // background luar biru
        setLayout(new GridBagLayout());
        setBackground(new Color(0x0D47A1));

        // panel putih di tengah sebagai kartu login
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

        String csvPath = "mahasiswa.csv";

        btnLogin.addActionListener(e -> {
            String nim  = tfNim.getText().trim();
            String nama = tfNama.getText().trim();

            if (nim.isEmpty() || nama.isEmpty()) {
                JOptionPane.showMessageDialog(this, "NIM dan Nama tidak boleh kosong");
                return;
            }

            String kelas = CsvUtil.getKelasByNimAndNama(csvPath, nim, nama);
            if (kelas == null) {
                JOptionPane.showMessageDialog(this,
                        "Data tidak ditemukan / NIM dan Nama tidak cocok");
                return;
            }

            int semester = 2;   // contoh
            int maxSks   = 24;  // contoh
            Mahasiswa m = new Mahasiswa(nim, nama, semester, maxSks);
            mainFrame.setCurrentStudent(m);

            if (kelas.equalsIgnoreCase("A")) {
                mainFrame.setTitle("Sistem KRS - Kelas A : " + nim + " - " + nama);
                mainFrame.showPage("KRS_A");
            } else if (kelas.equalsIgnoreCase("B")) {
                mainFrame.setTitle("Sistem KRS - Kelas B : " + nim + " - " + nama);
                mainFrame.showPage("KRS_B");
            } else {
                JOptionPane.showMessageDialog(this, "Kelas tidak valid di CSV (harus A/B)");
            }
        });

        // isi panel kartu
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

        // letakkan kartu di tengah panel biru
        add(card, new GridBagConstraints());
    }
}
