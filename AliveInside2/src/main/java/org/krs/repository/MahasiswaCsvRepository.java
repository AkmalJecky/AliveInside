package org.krs.repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaCsvRepository {

    private static final String FILE_PATH = "data" + File.separator + "mahasiswa.csv";

    // READ: cari kelas berdasarkan NIM dan Nama (login)
    public String getKelasByNimAndNama(String nim, String nama) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.err.println("mahasiswa.csv tidak ditemukan: " + FILE_PATH);
            return null;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { // lewati header
                    first = false;
                    continue;
                }
                if (line.isBlank()) continue;

                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String nimCsv = parts[0].trim();
                String namaCsv = parts[1].trim();
                String kelasCsv = parts[2].trim();

                if (nimCsv.equals(nim) && namaCsv.equalsIgnoreCase(nama)) {
                    return kelasCsv;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE: ganti nama berdasarkan NIM
    public void updateNamaMahasiswa(String nimTarget, String namaBaru) {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String header = reader.readLine();
            if (header != null) {
                lines.add(header);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = line.split(",");
                if (p.length < 3) {
                    lines.add(line);
                    continue;
                }

                if (p[0].trim().equals(nimTarget)) {
                    p[1] = namaBaru;
                    line = String.join(",", p);
                }
                lines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

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