package org.krs.service;

import org.krs.model.KelasKuliah;
import org.krs.model.KrsItem;
import org.krs.model.Mahasiswa;
import org.krs.repository.CsvKrsRepository;
import org.krs.repository.MahasiswaCsvRepository;

import java.util.List;
import java.util.Map;

public class KrsService {

    private final CsvKrsRepository repository;
    private final MahasiswaCsvRepository mahasiswaRepository;

    public KrsService(CsvKrsRepository repository) {
        this.repository = repository;
        this.mahasiswaRepository = new MahasiswaCsvRepository();
    }

    public List<KelasKuliah> getPaketSemester2() {
        return repository.loadPaketSemester2();
    }


    public int calculateTotalSks(List<KrsItem> items) {
        return items.stream()
                .mapToInt(KrsItem::getSks)
                .sum();
    }

    /**
     * Checks if adding a new class will cause a schedule conflict.
     * @param currentItems Current KRS items.
     * @param newClass New class to add.
     * @return true if there is a conflict, false otherwise.
     */
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

        if (newClass.isFull()) return false;
        if (hasScheduleConflict(currentItems, newClass)) return false;

        int newTotal = calculateTotalSks(currentItems) + newClass.getCourse().getSks();
        return newTotal <= student.getMaxSks();
    }

    /**
     * Saves a list of KRS items to the repository.
     * @param items List of KRS items to save.
     */
    public void saveKrsItems(List<KrsItem> items) {
        for (KrsItem item : items) {
            repository.appendKrsItem(item);
        }
    }

    public List<KrsItem> loadExistingKrs(Mahasiswa student) {
        return repository.loadKrsByNim(student);
    }


    public List<KelasKuliah> getSemester3ClassesWithEnrollment() {
        List<KelasKuliah> classes = repository.loadMkSemester3();
        Map<String, Integer> counts = repository.countEnrollmentPerClass();

        for (KelasKuliah k : classes) {
            Integer c = counts.get(k.getClassCode());
            if (c != null) {
                k.setCurrentEnrolled(c);
            }
        }
        return classes;
    }

    public void deleteKrsItem(KrsItem item) {
        repository.deleteKrsItem(item);
    }

    /**
     * Updates a student's name in both KRS and master files.
     * @param student Student to update.
     * @param namaBaru New name.
     */
    public void updateNamaMahasiswa(Mahasiswa student, String namaBaru) {
        student.setName(namaBaru);

        repository.updateNamaMahasiswa(student.getNim(), namaBaru);
        mahasiswaRepository.updateNamaMahasiswa(student.getNim(), namaBaru);
    }
}