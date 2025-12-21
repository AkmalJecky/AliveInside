package org.krs.repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvMahasiswaRepository {

    private static final String MAHASISWA_FILE = "data" + File.separator + "mahasiswa.csv";

    // UPDATE nama mahasiswa di mahasiswa.csv berdasarkan NIM
    public void updateNamaMahasiswa(String nimTarget, String namaBaru) {
        File file = new File(MAHASISWA_FILE);
        if (!file.exists()) {
            return; // belum ada file master
        }

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String header = reader.readLine();
            if (header != null) {
                lines.add(header); // simpan header
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = line.split(",");
                if (p.length < 3) {   // nim,nama,kelas
                    lines.add(line);
                    continue;
                }

                if (p[0].trim().equals(nimTarget)) {
                    p[1] = namaBaru;               // kolom nama
                    line = String.join(",", p);
                }

                lines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // tulis ulang file
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8))) {

            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}