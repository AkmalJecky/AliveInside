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
import java.util.Collections;
import java.util.List;

public class PanelPilihMatkulB extends JPanel {

    private final MainFrame mainFrame;
    private final KrsService krsService;

    private JLabel lblNamaValue;
    private JLabel lblNimValue;
    private JLabel lblSemValue;
    private JLabel lblSksInfo;

    private final List<KelasKuliah> allClasses = new ArrayList<>();
    private final List<KrsItem> currentItems = new ArrayList<>();

    private JTable table;
    private DefaultTableModel tableModel;

    public PanelPilihMatkulB(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.krsService = new KrsService(new CsvKrsRepository());
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        loadAllClasses();
    }

    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        JPanel identPanel = new JPanel(new GridLayout(2, 4, 12, 4));

        JLabel lblNimTitle = new JLabel("NIM");
        lblNimTitle.setFont(lblNimTitle.getFont().deriveFont(Font.BOLD));
        lblNimValue = new JLabel("-");

        JLabel lblSemTitle = new JLabel("Semester");
        lblSemTitle.setFont(lblSemTitle.getFont().deriveFont(Font.BOLD));
        lblSemValue = new JLabel("-");

        JLabel lblNamaTitle = new JLabel("Nama");
        lblNamaTitle.setFont(lblNamaTitle.getFont().deriveFont(Font.BOLD));
        lblNamaValue = new JLabel("-");

        identPanel.add(lblNimTitle);
        identPanel.add(lblNimValue);
        identPanel.add(lblSemTitle);
        identPanel.add(lblSemValue);

        identPanel.add(lblNamaTitle);
        identPanel.add(lblNamaValue);
        identPanel.add(new JLabel());
        identPanel.add(new JLabel());

        lblSksInfo = new JLabel("Total SKS diambil: 0 / 0", JLabel.RIGHT);
        lblSksInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        topPanel.add(identPanel, BorderLayout.WEST);
        topPanel.add(lblSksInfo, BorderLayout.EAST);
        return topPanel;
    }

    private JScrollPane buildTablePanel() {
        String[] columns = {
                "Kode MK", "Nama MK", "Kelas", "Hari", "Jam", "Ruangan", "Kapasitas"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(80);

        return new JScrollPane(table);
    }

    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAmbil = new JButton("Ambil");
        JButton btnLihatKrs = new JButton("Lihat KRS saya");
        JButton btnKembali = new JButton("Logout");

        btnAmbil.addActionListener(e -> ambilKelasTerpilih());
        btnLihatKrs.addActionListener(e -> {
            mainFrame.getPanelKrsB().refreshKrs();
            mainFrame.showPage("KRS_B");
        });
        btnKembali.addActionListener(e -> {
            mainFrame.setTitle("Sistem KRS");
            mainFrame.setCurrentStudent(null);
            mainFrame.showPage("LOGIN");
        });

        bottom.add(btnAmbil);
        bottom.add(btnLihatKrs);
        bottom.add(btnKembali);
        return bottom;
    }

    private void loadAllClasses() {
        allClasses.clear();
        allClasses.addAll(krsService.getSemester3ClassesWithEnrollment());

        tableModel.setRowCount(0);
        for (KelasKuliah k : allClasses) {
            int cap = k.getCapacity();
            int enrolled = k.getCurrentEnrolled();
            String kapasitasText = enrolled + " / " + cap;

            tableModel.addRow(new Object[]{
                    k.getCourse().getCode(),
                    k.getCourse().getName(),
                    k.getClassCode(),
                    k.getDay(),
                    k.getStartTime() + "-" + k.getEndTime(),
                    k.getRoom(),
                    kapasitasText
            });
        }
    }

    public void reloadClasses() {
        loadAllClasses();
    }

    public void refreshMahasiswaInfo() {
        Mahasiswa mhs = mainFrame.getCurrentStudent();
        currentItems.clear();

        if (mhs == null) {
            lblNamaValue.setText("-");
            lblNimValue.setText("-");
            lblSemValue.setText("-");
            lblSksInfo.setText("Total SKS diambil: 0 / 0");
            return;
        }

        lblNamaValue.setText(mhs.getName());
        lblNimValue.setText(mhs.getNim());
        lblSemValue.setText(String.valueOf(mhs.getSemester()));

        currentItems.addAll(krsService.loadExistingKrs(mhs));
        int totalSks = krsService.calculateTotalSks(currentItems);
        lblSksInfo.setText("Total SKS diambil: " + totalSks + " / " + mhs.getMaxSks());
    }

    private void ambilKelasTerpilih() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih salah satu baris kelas terlebih dahulu.");
            return;
        }

        Mahasiswa mhs = mainFrame.getCurrentStudent();
        if (mhs == null) {
            JOptionPane.showMessageDialog(this, "Data mahasiswa tidak ditemukan. Silakan login ulang.");
            return;
        }

        KelasKuliah selectedKelas = allClasses.get(row);
        String kodeMkBaru = selectedKelas.getCourse().getCode();
        String kodeKelasBaru = selectedKelas.getClassCode();

        // ====== VALIDASI: tidak boleh ambil mata kuliah yang sama dua kali ======
        for (KrsItem item : currentItems) {
            String kodeMkLama = item.getKelasKuliah().getCourse().getCode();
            String kodeKelasLama = item.getKelasKuliah().getClassCode();

            // sama persis kelasnya
            if (kodeKelasLama.equals(kodeKelasBaru)) {
                JOptionPane.showMessageDialog(this,
                        "Kelas " + kodeKelasBaru + " sudah Anda ambil.");
                return;
            }

            // beda kelas tapi mata kuliahnya sama (IF301-3B1 vs IF301-3B2)
            if (kodeMkLama.equals(kodeMkBaru)) {
                JOptionPane.showMessageDialog(this,
                        "Mata kuliah " + kodeMkBaru + " sudah Anda ambil di kelas lain.");
                return;
            }
        }

        // cek aturan bisnis: SKS, bentrok, kapasitas
        if (!krsService.canAddCourse(mhs, currentItems, selectedKelas)) {
            JOptionPane.showMessageDialog(this,
                    "Tidak bisa menambah kelas: SKS penuh / bentrok jadwal / kelas penuh.");
            return;
        }

        KrsItem newItem = new KrsItem(mhs, selectedKelas);
        krsService.saveKrsItems(Collections.singletonList(newItem));

        currentItems.add(newItem);
        int totalSks = krsService.calculateTotalSks(currentItems);
        lblSksInfo.setText("Total SKS diambil: " + totalSks + " / " + mhs.getMaxSks());

        JOptionPane.showMessageDialog(this,
                "Kelas " + selectedKelas.getClassCode() + " berhasil ditambahkan ke KRS.");

        loadAllClasses(); // reload kapasitas
    }
}