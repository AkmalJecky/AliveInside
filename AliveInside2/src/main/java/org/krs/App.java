package org.krs;

import org.krs.model.KelasKuliah;
import org.krs.model.KrsItem;
import org.krs.model.Mahasiswa;
import org.krs.repository.CsvKrsRepository;
import org.krs.service.KrsService;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        CsvKrsRepository repository = new CsvKrsRepository();
        KrsService service = new KrsService(repository);

        // --- Test 1: generate paket semester 2 ---
        Mahasiswa mhsS2 = new Mahasiswa("220001", "Alice", 2, 24);
        List<KrsItem> paket = service.generatePackageKrs(mhsS2);

        System.out.println("=== Paket KRS Semester 2 ===");
        for (KrsItem item : paket) {
            System.out.println(item.getKelasKuliah());
        }
        System.out.println("Total SKS: " + service.calculateTotalSks(paket));

        // Simpan paket ke krs.csv
        service.saveKrsItems(paket);
        System.out.println("Paket S2 disimpan ke data/krs.csv\n");

        // --- Test 2: manual semester 3 (tanpa UI) ---
        Mahasiswa mhsS3 = new Mahasiswa("220002", "Bob", 3, 24);
        List<KelasKuliah> sem3Classes = service.getSemester3Classes();
        List<KrsItem> krsS3 = new ArrayList<>();

        System.out.println("=== Daftar kelas semester 3 ===");
        for (int i = 0; i < sem3Classes.size(); i++) {
            System.out.println((i + 1) + ". " + sem3Classes.get(i));
        }

        // contoh: coba ambil 2 kelas pertama kalau boleh
        for (int i = 0; i < Math.min(2, sem3Classes.size()); i++) {
            KelasKuliah kelas = sem3Classes.get(i);
            if (service.canAddCourse(mhsS3, krsS3, kelas)) {
                kelas.incrementEnrolled();
                krsS3.add(new KrsItem(mhsS3, kelas));
            } else {
                System.out.println("Tidak bisa ambil: " + kelas);
            }
        }

        System.out.println("\n=== KRS S3 (simulasi manual) ===");
        for (KrsItem item : krsS3) {
            System.out.println(item.getKelasKuliah());
        }
        System.out.println("Total SKS S3: " + service.calculateTotalSks(krsS3));

        // Simpan KRS S3
        service.saveKrsItems(krsS3);
        System.out.println("KRS S3 disimpan ke data/krs.csv");

        // --- Test 3: load kembali dari krs.csv ---
        System.out.println("\n=== Load KRS Alice dari file ===");
        List<KrsItem> loaded = service.loadExistingKrs(mhsS2);
        for (KrsItem item : loaded) {
            System.out.println(item.getKelasKuliah());
        }
        System.out.println("Total SKS dari file: " + service.calculateTotalSks(loaded));
    }
}