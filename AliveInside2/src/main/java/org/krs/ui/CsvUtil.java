// CsvUtil.java (org.krs.ui)
package org.krs.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CsvUtil {

    public static String getKelasByNimAndNama(String csvPath, String nim, String nama) {
        InputStream in = CsvUtil.class.getClassLoader().getResourceAsStream(csvPath);
        if (in == null) {
            System.err.println("CSV tidak ditemukan di classpath: " + csvPath);
            return null;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { // lewati header
                    first = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String nimCsv   = parts[0].trim();
                String namaCsv  = parts[1].trim();
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
