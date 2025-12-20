package org.krs.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelKelasB extends JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> cbMataKuliah;

    public PanelKelasB(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private JButton redButton() {
        JButton btn = new JButton("Hapus");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0xF44336)); // merah
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton greenButton() {
        JButton btn = new JButton("Tambah");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0x4CAF50)); // hijau
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton whiteButton() {
        JButton btn = new JButton("Kembali ke Login");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        return btn;
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // ====== COMBOBOX MATA KULIAH (ATAS) ======
        String[] daftarMk = {
                "IF101 - Dasar Pemrograman (3)",
                "IF102 - Struktur Data (3)",
                "IF103 - Matematika Diskrit (3)"
        };
        cbMataKuliah = new JComboBox<>(daftarMk);
        cbMataKuliah.setPreferredSize(new Dimension(350, 28));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Pilih Mata Kuliah:"));
        topPanel.add(cbMataKuliah);

        // ====== TABEL KRS (TENGAH) ======
        String[] columns = {"Kode MK", "Nama MK", "Kelas", "Hari", "Jam", "SKS"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabel hanya display
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(new Color(0xBDBDBD));
        table.setSelectionBackground(new Color(0xC8E6C9)); // hijau muda
        table.setSelectionForeground(Color.BLACK);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);

        // ====== PANEL TOMBOL (BAWAH) ======
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = greenButton();
        JButton btnDelete = redButton();
        JButton btnBack = whiteButton();

        // Tambah dari comboBox ke tabel (dengan cek duplikat)
        btnAdd.addActionListener(_ -> tambahDariCombo());

        // Hapus baris terpilih
        btnDelete.addActionListener(_ -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus");
                return;
            }
            tableModel.removeRow(row);
        });

        // Kembali ke login
        btnBack.addActionListener(_ -> {
            mainFrame.setTitle("Sistem KRS");
            mainFrame.showPage("LOGIN");
        });

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);

        // ====== RANGKAI KE PANEL ======
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // ====== LOGIKA: isi detail MK berdasarkan pilihan comboBox + CEK DUPLIKAT ======
    private void tambahDariCombo() {
        String selected = (String) cbMataKuliah.getSelectedItem();
        if (selected == null) return;

        String kode = "";
        String nama = "";
        String kelas = "B";
        String hari = "";
        String jam = "";
        String sks = "";

        if (selected.startsWith("IF101")) {
            kode = "IF101";
            nama = "Dasar Pemrograman";
            sks  = "3";
            hari = "Senin";
            jam  = "08:00-10:00";
        } else if (selected.startsWith("IF102")) {
            kode = "IF102";
            nama = "Struktur Data";
            sks  = "3";
            hari = "Selasa";
            jam  = "10:00-12:00";
        } else if (selected.startsWith("IF103")) {
            kode = "IF103";
            nama = "Matematika Diskrit";
            sks  = "3";
            hari = "Rabu";
            jam  = "13:00-15:00";
        }

        // ----- CEK DUPLIKAT BERDASARKAN KODE MK -----
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String kodeExisting = String.valueOf(tableModel.getValueAt(i, 0));
            if (kodeExisting.equals(kode)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Mata kuliah " + nama + " sudah diambil.",
                        "Duplikat Mata Kuliah",
                        JOptionPane.WARNING_MESSAGE
                );
                return; // batalkan penambahan
            }
        }

        // kalau tidak duplikat, baru tambahkan ke tabel
        tableModel.addRow(new Object[]{kode, nama, kelas, hari, jam, sks});
    }
}
