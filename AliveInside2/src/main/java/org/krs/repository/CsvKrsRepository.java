package org.krs.repository;

import org.krs.model.KelasKuliah;
import org.krs.model.KrsItem;
import org.krs.model.Mahasiswa;
import org.krs.model.MataKuliah;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvKrsRepository {

    private static final String BASE_PATH       = "data" + File.separator;
    private static final String PAKET_SEM2_FILE = BASE_PATH + "paket_sem2.csv";
    private static final String MK_SEM3_FILE    = BASE_PATH + "mk_sem3.csv";
    private static final String KRS_FILE        = BASE_PATH + "krs.csv";

    // ---------- load master classes ----------
    public List<KelasKuliah> loadPaketSemester2() {
        return loadClassesFromCsv(PAKET_SEM2_FILE);
    }

    public List<KelasKuliah> loadMkSemester3() {
        return loadClassesFromCsv(MK_SEM3_FILE);
    }

    private List<KelasKuliah> loadClassesFromCsv(String filePath) {
        List<KelasKuliah> result = new ArrayList<>();
        try (BufferedReader reader = newReader(filePath)) {
            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split(",");
                if (p.length < 9) continue;

                MataKuliah mk = new MataKuliah(
                        p[0].trim(),
                        p[1].trim(),
                        Integer.parseInt(p[2].trim())
                );
                KelasKuliah kk = new KelasKuliah(
                        mk,
                        p[3].trim(),
                        p[4].trim(),
                        p[5].trim(),
                        p[6].trim(),
                        p[7].trim(),
                        Integer.parseInt(p[8].trim())
                );
                result.add(kk);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // ---------- KRS per student ----------
    public List<KrsItem> loadKrsByNim(Mahasiswa student) {
        List<KrsItem> items = new ArrayList<>();
        File file = new File(KRS_FILE);
        if (!file.exists()) return items;

        try (BufferedReader reader = newReader(KRS_FILE)) {
            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split(",");
                if (p.length < 12) continue;
                if (!p[0].trim().equals(student.getNim())) continue;

                Mahasiswa m = new Mahasiswa(
                        p[0].trim(),
                        p[1].trim(),
                        Integer.parseInt(p[2].trim()),
                        student.getMaxSks()
                );
                MataKuliah mk = new MataKuliah(
                        p[4].trim(),
                        p[5].trim(),
                        Integer.parseInt(p[6].trim())
                );
                KelasKuliah kk = new KelasKuliah(
                        mk,
                        p[7].trim(),
                        p[8].trim(),
                        p[9].trim(),
                        p[10].trim(),
                        p[11].trim(),
                        0
                );
                items.add(new KrsItem(m, kk));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void appendKrsItem(KrsItem item) {
        ensureKrsHeaderExists();

        try (BufferedWriter writer = newWriter(KRS_FILE, true)) {
            Mahasiswa m = item.getStudent();
            KelasKuliah k = item.getKelasKuliah();

            String line = String.join(",",
                    m.getNim(),
                    m.getName(),
                    String.valueOf(m.getSemester()),
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

    public Map<String, Integer> countEnrollmentPerClass() {
        Map<String, Integer> counts = new HashMap<>();
        File file = new File(KRS_FILE);
        if (!file.exists()) return counts;

        try (BufferedReader reader = newReader(KRS_FILE)) {
            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split(",");
                if (p.length < 12) continue;

                String kodeKelas = p[7].trim();
                counts.merge(kodeKelas, 1, Integer::sum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return counts;
    }

    private void ensureKrsHeaderExists() {
        File file = new File(KRS_FILE);
        if (file.exists()) return;

        try (BufferedWriter writer = newWriter(KRS_FILE, false)) {
            writer.write(KRS_HEADER);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader newReader(String filePath) throws IOException {
        return new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)
        );
    }

    private static final String KRS_HEADER =
            "nim,nama,semester,semester_copy,kode_mk,nama_mk,sks," +
                    "kode_kelas,hari,jam_mulai,jam_selesai,ruangan";

    private BufferedWriter newWriter(String filePath, boolean append) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        return new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8)
        );
    }

    // Delete KRS
    public void deleteKrsItem(KrsItem target) {
        File file = new File(KRS_FILE);
        if (!file.exists()) return;

        List<String> allLines = new ArrayList<>();
        try (BufferedReader reader = newReader(KRS_FILE)) {
            String line = reader.readLine(); // header
            if (line == null) return;
            String header = line;
            allLines.add(header);

            String nimTarget = target.getStudent().getNim();
            String kodeMkTarget = target.getKelasKuliah().getCourse().getCode();
            String kodeKelasTarget = target.getKelasKuliah().getClassCode();

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split(",");
                if (p.length < 12) continue;

                String nim = p[0].trim();
                String kodeMk = p[4].trim();
                String kodeKelas = p[7].trim();

                if (nim.equals(nimTarget)
                        && kodeMk.equals(kodeMkTarget)
                        && kodeKelas.equals(kodeKelasTarget)) {
                    continue; // skip baris yang dihapus
                }
                allLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedWriter writer = newWriter(KRS_FILE, false)) {
            for (String l : allLines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------- UPDATE NAMA MAHASISWA DI KRS.CSV ----------
    public void updateNamaMahasiswa(String nimTarget, String namaBaru) {
        List<String> lines = new ArrayList<>();
        File file = new File(KRS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = newReader(KRS_FILE)) {
            String header = reader.readLine();
            if (header != null) {
                lines.add(header); // simpan header
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split(",");
                if (p.length < 12) {
                    lines.add(line);
                    continue;
                }

                if (p[0].trim().equals(nimTarget)) {
                    p[1] = namaBaru;          // kolom nama
                    line = String.join(",", p);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedWriter writer = newWriter(KRS_FILE, false)) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}