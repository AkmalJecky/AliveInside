package org.krs.ui;

import org.krs.model.KelasKuliah;
import org.krs.model.KrsItem;
import org.krs.model.Mahasiswa;
import org.krs.repository.CsvKrsRepository;
import org.krs.service.KrsService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelKelasA extends JPanel {

    private final MainFrame mainFrame;
    private final KrsService krsService;

    private JLabel lblNamaValue;
    private JLabel lblNimValue;
    private JLabel lblSemValue;
    private JLabel lblSksInfo;

    private JTable table;
    private DefaultTableModel tableModel;

    private final List<KrsItem> paketItems = new ArrayList<>();

    public PanelKelasA(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.krsService = new KrsService(new CsvKrsRepository());
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));

        add(buildTopPanel(), BorderLayout.NORTH);              // identitas
        add(buildTableAndNoticePanel(), BorderLayout.CENTER);  // tabel + pengumuman berurutan
        add(buildBottomButtons(), BorderLayout.SOUTH);         // tombol kembali

        loadPaketSemester2();
    }

    // ====== TOP: identitas mahasiswa + info SKS ======
    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // ========== KIRI: identitas (Nama, NIM, Semester) ==========
        JPanel identPanel = new JPanel(new GridLayout(3, 2, 8, 4));

        JLabel lblNamaTitle = new JLabel("Nama");
        lblNamaTitle.setFont(lblNamaTitle.getFont().deriveFont(Font.BOLD));
        lblNamaValue = new JLabel("-");

        JLabel lblNimTitle = new JLabel("NIM");
        lblNimTitle.setFont(lblNimTitle.getFont().deriveFont(Font.BOLD));
        lblNimValue = new JLabel("-");

        JLabel lblSemTitle = new JLabel("Semester");
        lblSemTitle.setFont(lblSemTitle.getFont().deriveFont(Font.BOLD));
        lblSemValue = new JLabel("-");

        identPanel.add(lblNamaTitle);
        identPanel.add(lblNamaValue);   // value di SAMPING kanan
        identPanel.add(lblNimTitle);
        identPanel.add(lblNimValue);
        identPanel.add(lblSemTitle);
        identPanel.add(lblSemValue);

        identPanel.setPreferredSize(new Dimension(220, identPanel.getPreferredSize().height));
        topPanel.add(identPanel, BorderLayout.WEST);

        // ========== TENGAH: logo ==========
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/logokhs.png"));
        JLabel lblLogo = new JLabel(logoIcon);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setVerticalAlignment(SwingConstants.CENTER);

        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.add(lblLogo, BorderLayout.CENTER);
        topPanel.add(logoPanel, BorderLayout.CENTER);

        // ========== KANAN: total SKS ==========
        lblSksInfo = new JLabel("Total SKS paket: 0", JLabel.RIGHT);
        lblSksInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel sksPanel = new JPanel(new BorderLayout());
        sksPanel.add(lblSksInfo, BorderLayout.NORTH);
        sksPanel.setPreferredSize(new Dimension(170, sksPanel.getPreferredSize().height));
        topPanel.add(sksPanel, BorderLayout.EAST);

        return topPanel;
    }

    // ====== CENTER: Tabel + Pengumuman vertikal ======
    private JPanel buildTableAndNoticePanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // --- tabel ---
        String[] columns = {"Kode MK", "Nama MK", "Kelas", "Hari", "Jam", "Ruangan", "SKS"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(60);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- pengumuman di bawah tabel ---
        JPanel noticePanel = new JPanel(new BorderLayout());
        noticePanel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        noticePanel.setBackground(new Color(0x00BFFF)); // biru muda
        noticePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNotice = new JLabel(
                "<html><b>Informasi:</b> Mahasiswa Semester 1 &amp; 2 mendapatkan paket mata kuliah yang sudah ditentukan oleh program studi" +
                        "                        dan tidak dapat diubah.</html>"
        );
        lblNotice.setForeground(Color.WHITE);
        noticePanel.add(lblNotice, BorderLayout.CENTER);

        // urutan: tabel lalu pengumuman
        container.add(scroll);
        container.add(Box.createVerticalStrut(4));
        container.add(noticePanel);

        return container;
    }

    // ====== BOTTOM: tombol kembali ======
    private JPanel buildBottomButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnEditUser = new JButton("Edit User");
        btnEditUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEditUser.addActionListener(_ -> editCurrentUser());

        JButton btnBack = new JButton("Kembali");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setBackground(new Color(0x9C27B0));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(_ -> {
            mainFrame.setTitle("Sistem KRS");
            mainFrame.showPage("LOGIN");
        });

        buttonPanel.add(btnEditUser);
        buttonPanel.add(btnBack);
        return buttonPanel;
    }

    // ====== Load paket semester 2 ke tabel ======
    private void loadPaketSemester2() {
        paketItems.clear();
        tableModel.setRowCount(0);

        Mahasiswa mhs = mainFrame.getCurrentStudent();
        if (mhs == null) {
            return;
        }

        List<KelasKuliah> paketClasses = krsService.getPaketSemester2();
        int totalSks = 0;

        for (KelasKuliah k : paketClasses) {
            tableModel.addRow(new Object[]{
                    k.getCourse().getCode(),
                    k.getCourse().getName(),
                    k.getClassCode(),
                    k.getDay(),
                    k.getStartTime() + "-" + k.getEndTime(),
                    k.getRoom(),
                    k.getCourse().getSks()
            });
            totalSks += k.getCourse().getSks();
            paketItems.add(new KrsItem(mhs, k));
        }

        lblSksInfo.setText("Total SKS paket: " + totalSks);
    }

    public void refreshMahasiswaInfo() {
        Mahasiswa mhs = mainFrame.getCurrentStudent();
        if (mhs == null) {
            lblNamaValue.setText("-");
            lblNimValue.setText("-");
            lblSemValue.setText("-");
            lblSksInfo.setText("Total SKS paket: 0");
            tableModel.setRowCount(0);
            paketItems.clear();
            return;
        }

        lblNamaValue.setText(mhs.getName());
        lblNimValue.setText(mhs.getNim());
        lblSemValue.setText(String.valueOf(mhs.getSemester()));

        loadPaketSemester2();
    }

    private void editCurrentUser() {
        Mahasiswa mhs = mainFrame.getCurrentStudent();
        if (mhs == null) {
            JOptionPane.showMessageDialog(this, "Belum ada mahasiswa yang login");
            return;
        }

        String namaBaru = JOptionPane.showInputDialog(
                this,
                "Nama baru:",
                mhs.getName()
        );
        if (namaBaru == null || namaBaru.isBlank()) return;

        krsService.updateNamaMahasiswa(mhs, namaBaru);

        JOptionPane.showMessageDialog(this, "Nama berhasil diubah menjadi: " + namaBaru);
    }
}