package org.krs.ui;

import org.krs.model.Mahasiswa;
import org.krs.repository.CsvKrsRepository;
import org.krs.repository.MahasiswaCsvRepository;
import org.krs.service.KrsService;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private final MainFrame mainFrame;
    private JTextField tfNim;
    private JTextField tfNama;
    private final MahasiswaCsvRepository mhsRepo = new MahasiswaCsvRepository();

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private JButton styledButton() {
        JButton btn = new JButton("Masuk");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0x4CAF50)); // hijau
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        return btn;
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBackground(Color.DARK_GRAY);
        centerWrapper.setBorder(
                BorderFactory.createEmptyBorder(60, 20, 20, 20));

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        java.net.URL logoUrl = getClass().getClassLoader().getResource("img/logokhs.png");
        if (logoUrl != null) {
            lblLogo.setIcon(new ImageIcon(logoUrl));
        } else {
            lblLogo.setText("LOGO");
            lblLogo.setForeground(Color.lightGray);
        }

        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.DARK_GRAY);
                int arc = 30;
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Login Mahasiswa", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.white);

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

        JLabel lblNim = new JLabel("NIM");
        lblNim.setForeground(Color.white);
        card.add(lblNim, gbc);

        gbc.gridx = 1;
        card.add(tfNim, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblNama = new JLabel("Nama");
        lblNama.setForeground(Color.white);
        card.add(lblNama, gbc);

        gbc.gridx = 1;
        card.add(tfNama, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        card.add(btnLogin, gbc);

        center.add(lblLogo);
        center.add(Box.createVerticalStrut(16));
        center.add(card);

        centerWrapper.add(center, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);
    }

    private void doLogin() {
        String nim = tfNim.getText().trim();
        String nama = tfNama.getText().trim();

        if (nim.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM dan Nama tidak boleh kosong");
            return;
        }

        String kelas = mhsRepo.getKelasByNimAndNama(nim, nama);
        if (kelas == null) {
            JOptionPane.showMessageDialog(this,
                    "Data tidak ditemukan / NIM dan Nama tidak cocok");
            return;
        }

        int semester;
        int maxSks;

        if (kelas.equalsIgnoreCase("A")) {
            semester = 2;
            maxSks = 24;
        } else if (kelas.equalsIgnoreCase("B")) {
            semester = 3;
            maxSks = 24;
        } else {
            JOptionPane.showMessageDialog(this, "Kelas tidak valid di CSV (harus A/B)");
            return;
        }

        Mahasiswa m = new Mahasiswa(nim, nama, semester, maxSks);
        mainFrame.setCurrentStudent(m);

        if (kelas.equalsIgnoreCase("A")) {
            mainFrame.setTitle("Sistem KRS - Kelas A : " + nim + " - " + nama);
            mainFrame.showPage("KRS_A");
        } else {
            mainFrame.setTitle("Sistem KRS - Kelas B : " + nim + " - " + nama);
            mainFrame.showPage("PILIH_B");
        }
    }

    private void doUpdateNama() {
        Mahasiswa mhs = mainFrame.getCurrentStudent();
        if (mhs == null) {
            JOptionPane.showMessageDialog(this, "Belum ada mahasiswa yang login");
            return;
        }

        String namaBaru = JOptionPane.showInputDialog(this,
                "Masukkan nama baru:", mhs.getName());
        if (namaBaru == null || namaBaru.isBlank()) {
            return; // dibatalkan
        }

        // akses KrsService lewat panel KRS A (atau buat instance sendiri)
        KrsService service = new KrsService(new CsvKrsRepository());
        service.updateNamaMahasiswa(mhs, namaBaru);

        JOptionPane.showMessageDialog(this, "Nama berhasil diperbarui.");
    }

}
