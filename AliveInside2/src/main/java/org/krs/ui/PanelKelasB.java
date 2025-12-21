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

public class PanelKelasB extends JPanel {

    private final MainFrame mainFrame;
    private final KrsService krsService;
    private DefaultTableModel tableModel;
    private JTable table;
    private List<KrsItem> currentKrsItems = new ArrayList<>();

    public PanelKelasB(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.krsService = new KrsService(new CsvKrsRepository());
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("KRS Saya", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"Kode MK", "Nama MK", "Kelas", "Hari", "Jam", "Ruangan", "SKS"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton btnDelete    = new JButton("Hapus");
        JButton btnEditUser  = new JButton("Edit User");
        JButton btnBack      = new JButton("Kembali");

        btnDelete.addActionListener(_ -> hapusKrsTerpilih());
        btnEditUser.addActionListener(_ -> editCurrentUser());
        btnBack.addActionListener(_ -> {
            mainFrame.setTitle("Sistem KRS");
            mainFrame.showPage("PILIH_B");
        });

        buttonPanel.add(btnDelete);
        buttonPanel.add(btnEditUser);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshKrs() {
        Mahasiswa mhs = mainFrame.getCurrentStudent();
        if (mhs == null) {
            tableModel.setRowCount(0);
            currentKrsItems.clear();
            JOptionPane.showMessageDialog(this, "Silakan login terlebih dahulu.");
            return;
        }

        currentKrsItems = krsService.loadExistingKrs(mhs);
        tableModel.setRowCount(0);

        for (KrsItem item : currentKrsItems) {
            KelasKuliah k = item.getKelasKuliah();
            tableModel.addRow(new Object[]{
                    k.getCourse().getCode(),
                    k.getCourse().getName(),
                    k.getClassCode(),
                    k.getDay(),
                    k.getStartTime() + "-" + k.getEndTime(),
                    k.getRoom(),
                    k.getCourse().getSks()
            });
        }
    }

    private void hapusKrsTerpilih() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih baris KRS yang akan dihapus.");
            return;
        }
        if (row < 0 || row >= currentKrsItems.size()) return;

        KrsItem item = currentKrsItems.get(row);

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus mata kuliah " +
                        item.getKelasKuliah().getCourse().getCode() +
                        " (" + item.getKelasKuliah().getClassCode() + ") dari KRS?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi != JOptionPane.YES_OPTION) return;

        krsService.deleteKrsItem(item);

        currentKrsItems.remove(row);
        tableModel.removeRow(row);

        if (mainFrame.getPanelPilihB() != null) {
            mainFrame.getPanelPilihB().refreshMahasiswaInfo();
            mainFrame.getPanelPilihB().reloadClasses();
        }
    }

    // ---- EDIT USER: panggil service updateNamaMahasiswa ----
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