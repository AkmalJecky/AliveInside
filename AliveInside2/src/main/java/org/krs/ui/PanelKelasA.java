package org.krs.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelKelasA extends JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;

    public PanelKelasA(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0x9C27B0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // judul / informasi
        JLabel lblInfo = new JLabel(
                "<html><center>KRS Kelas A (Internasional)<br>"+
                        "Semua mata kuliah sudah ditentukan otomatis oleh program studi.</center></html>",
                JLabel.CENTER
        );
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // tabel hanya untuk menampilkan daftar matkul yang sudah fixed
        String[] columns = {"Kode MK", "Nama MK", "Kelas", "Hari", "Jam", "SKS"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tidak bisa diedit, karena otomatis
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // contoh data otomatis (nanti boleh diganti sesuai kebutuhanmu)
        tableModel.addRow(new Object[]{"INT101", "Calculus I", "A", "Senin", "08:00-10:00", "3"});
        tableModel.addRow(new Object[]{"INT102", "Programming I", "A", "Selasa", "10:00-12:00", "3"});
        tableModel.addRow(new Object[]{"INT103", "Academic Writing", "A", "Rabu", "13:00-15:00", "2"});

        JPanel bottomPanel = new JPanel();
        JButton btnBack = styledButton("Kembali ke Login");
        btnBack.addActionListener(e -> {
            mainFrame.setTitle("Sistem KRS");
            mainFrame.showPage("LOGIN");
        });

        bottomPanel.add(btnBack);

        add(lblInfo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
