package org.krs.repository;

import org.krs.model.KelasKuliah;
import org.krs.model.KrsItem;
import org.krs.model.Mahasiswa;
import org.krs.model.MataKuliah;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvKrsRepository {

    private static final String BASE_PATH       = "data" + File.separator;
    private static final String PAKET_SEM2_FILE = BASE_PATH + "paket_sem2.csv";
    private static final String MK_SEM3_FILE    = BASE_PATH + "mk_sem3.csv";
    private static final String KRS_FILE        = BASE_PATH + "krs.csv";

    // ---------- master data (classes) ----------

    public List<KelasKuliah> loadPaketSemester2() {
        return loadClassesFromCsv(PAKET_SEM2_FILE);
    }

    public List<KelasKuliah> loadMkSemester3() {
        return loadClassesFromCsv(MK_SEM3_FILE);
    }

    private List<KelasKuliah> loadClassesFromCsv(String filePath) {
        List<KelasKuliah> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split(",");
                if (parts.length < 9) continue;

                // kode_mk,nama_mk,sks,kode_kelas,hari,jam_mulai,jam_selesai,ruangan,kapasitas
                String courseCode  = parts[0].trim();
                String courseName  = parts[1].trim();
                int sks            = Integer.parseInt(parts[2].trim());
                String classCode   = parts[3].trim();
                String day         = parts[4].trim();
                String startTime   = parts[5].trim();
                String endTime     = parts[6].trim();
                String room        = parts[7].trim();
                int capacity       = Integer.parseInt(parts[8].trim());

                MataKuliah mk = new MataKuliah(courseCode, courseName, sks);
                KelasKuliah kk = new KelasKuliah(mk, classCode, day, startTime, endTime, room, capacity);
                result.add(kk);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // ---------- KRS data (per student) ----------

    public List<KrsItem> loadKrsByNim(Mahasiswa student) {
        List<KrsItem> items = new ArrayList<>();
        File file = new File(KRS_FILE);
        if (!file.exists()) {
            return items;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split(",");
                if (parts.length < 12) continue;

                // nim,nama,kelas,semester,kode_mk,nama_mk,sks,kode_kelas,hari,jam_mulai,jam_selesai,ruangan
                String nim = parts[0].trim();
                if (!nim.equals(student.getNim())) continue;

                String name      = parts[1].trim();
                String kelas     = parts[2].trim();
                int semester     = Integer.parseInt(parts[3].trim());
                String codeMk    = parts[4].trim();
                String nameMk    = parts[5].trim();
                int sks          = Integer.parseInt(parts[6].trim());
                String classCode = parts[7].trim();
                String day       = parts[8].trim();
                String startTime = parts[9].trim();
                String endTime   = parts[10].trim();
                String room      = parts[11].trim();

                Mahasiswa m = new Mahasiswa(nim, name, kelas, semester, student.getMaxSks());
                MataKuliah mk = new MataKuliah(codeMk, nameMk, sks);
                KelasKuliah kk = new KelasKuliah(mk, classCode, day, startTime, endTime, room, 0);

                items.add(new KrsItem(m, kk));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    public void appendKrsItem(KrsItem item) {
        ensureKrsHeaderExists();

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(KRS_FILE, true), StandardCharsets.UTF_8))) {

            Mahasiswa m = item.getStudent();
            KelasKuliah k = item.getKelasKuliah();

            String line = String.join(",",
                    m.getNim(),
                    m.getName(),
                    m.getKelas(),
                    String.valueOf(m.getSemester()),
                    k.getCourse().getCode(),
                    k.getCourse().getName(),
                    String.valueOf(k.getCourse().getSks()),
                    k.getClassCode(),
                    k.getDay(),
                    k.getStartTime(),
                    k.getEndTime(),
                    k.getRoom()
            );

            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureKrsHeaderExists() {
        File file = new File(KRS_FILE);
        if (file.exists()) return;

        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write("nim,nama,kelas,semester,kode_mk,nama_mk,sks,kode_kelas,hari,jam_mulai,jam_selesai,ruangan");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}