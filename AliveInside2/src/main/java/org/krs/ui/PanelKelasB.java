package org.krs.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelKelasB extends JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private JTable table;

    public PanelKelasB(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0x4CAF50));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void initUI() {
        setLayout(new BorderLayout());

        String[] columns = {"Kode MK", "Nama MK", "Kelas", "Hari", "Jam", "SKS"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel formPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        JTextField tfKode = new JTextField();
        JTextField tfNama = new JTextField();
        JTextField tfKelas = new JTextField("B");
        JTextField tfHari = new JTextField();
        JTextField tfJam = new JTextField();
        JTextField tfSks = new JTextField();

        formPanel.add(new JLabel("Kode MK"));
        formPanel.add(new JLabel("Nama MK"));
        formPanel.add(new JLabel("Kelas"));
        formPanel.add(new JLabel("Hari"));
        formPanel.add(new JLabel("Jam"));
        formPanel.add(new JLabel("SKS"));

        formPanel.add(tfKode);
        formPanel.add(tfNama);
        formPanel.add(tfKelas);
        formPanel.add(tfHari);
        formPanel.add(tfJam);
        formPanel.add(tfSks);

        JPanel buttonPanel = new JPanel();
        JButton btnAdd = styledButton("Tambah");
        JButton btnDelete = styledButton("Hapus");
        JButton btnBack = styledButton("Keluar (Kembali Login)");

        btnAdd.addActionListener(e -> {
            if (tfKode.getText().isEmpty() || tfNama.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kode MK dan Nama MK wajib diisi");
                return;
            }
            tableModel.addRow(new Object[]{
                    tfKode.getText(),
                    tfNama.getText(),
                    tfKelas.getText(),
                    tfHari.getText(),
                    tfJam.getText(),
                    tfSks.getText()
            });

            tfKode.setText("");
            tfNama.setText("");
            tfHari.setText("");
            tfJam.setText("");
            tfSks.setText("");
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus");
                return;
            }
            tableModel.removeRow(row);
        });

        btnBack.addActionListener(e -> {
            mainFrame.setTitle("Sistem KRS");
            mainFrame.showPage("LOGIN");
        });

        table.getSelectionModel().addListSelectionListener(ev -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                tfKode.setText(String.valueOf(tableModel.getValueAt(row, 0)));
                tfNama.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                tfKelas.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                tfHari.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                tfJam.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                tfSks.setText(String.valueOf(tableModel.getValueAt(row, 5)));
            }
        });

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
