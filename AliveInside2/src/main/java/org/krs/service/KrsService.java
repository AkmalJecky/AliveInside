package org.krs.service;

import org.krs.model.KelasKuliah;
import org.krs.model.KrsItem;
import org.krs.model.Mahasiswa;
import org.krs.repository.CsvKrsRepository;

import java.util.ArrayList;
import java.util.List;

public class KrsService {

    private final CsvKrsRepository repository;

    public KrsService(CsvKrsRepository repository) {
        this.repository = repository;
    }

    // ---------- load available classes ----------

    public List<KelasKuliah> getPaketSemester2() {
        return repository.loadPaketSemester2();
    }

    public List<KelasKuliah> getSemester3Classes() {
        return repository.loadMkSemester3();
    }

    // ---------- business logic ----------

    public int calculateTotalSks(List<KrsItem> items) {
        return items.stream()
                .mapToInt(KrsItem::getSks)
                .sum();
    }

    public boolean hasScheduleConflict(List<KrsItem> currentItems, KelasKuliah newClass) {
        return currentItems.stream().anyMatch(item ->
                item.getKelasKuliah().getDay().equalsIgnoreCase(newClass.getDay()) &&
                        timeOverlap(
                                item.getKelasKuliah().getStartTime(),
                                item.getKelasKuliah().getEndTime(),
                                newClass.getStartTime(),
                                newClass.getEndTime()
                        )
        );
    }

    private boolean timeOverlap(String start1, String end1, String start2, String end2) {
        int s1 = toMinutes(start1);
        int e1 = toMinutes(end1);
        int s2 = toMinutes(start2);
        int e2 = toMinutes(end2);
        return s1 < e2 && s2 < e1;
    }

    private int toMinutes(String hhmm) {
        String[] parts = hhmm.split(":");
        int h = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        return h * 60 + m;
    }

    public boolean canAddCourse(Mahasiswa student,
                                List<KrsItem> currentItems,
                                KelasKuliah newClass) {

        if (hasScheduleConflict(currentItems, newClass)) {
            return false;
        }

        int currentSks = calculateTotalSks(currentItems);
        int newTotal = currentSks + newClass.getCourse().getSks();
        return newTotal <= student.getMaxSks();
    }

    // ---------- package KRS for class A ----------

    public List<KrsItem> generatePackageKrs(Mahasiswa student) {
        List<KelasKuliah> paket = getPaketSemester2();
        List<KrsItem> result = new ArrayList<>();
        for (KelasKuliah k : paket) {
            result.add(new KrsItem(student, k));
        }
        return result;
    }

    // ---------- persistence helpers (optional) ----------

    public void savePackageKrs(Mahasiswa student) {
        List<KrsItem> items = generatePackageKrs(student);
        for (KrsItem item : items) {
            repository.appendKrsItem(item);
        }
    }

    public List<KrsItem> loadExistingKrs(Mahasiswa student) {
        return repository.loadKrsByNim(student);
    }
}