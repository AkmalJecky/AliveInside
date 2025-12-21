package org.krs.repository;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MahasiswaCsvRepository {

    private static final String FILE_PATH = "data" + File.separator + "mahasiswa.csv";

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
}