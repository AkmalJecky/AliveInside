package org.krs.model;

public class KrsItem {
    private final Mahasiswa student;
    private final KelasKuliah kelasKuliah;

    public KrsItem(Mahasiswa student, KelasKuliah kelasKuliah) {
        this.student = student;
        this.kelasKuliah = kelasKuliah;
    }

    public Mahasiswa getStudent() { return student; }
    public KelasKuliah getKelasKuliah() { return kelasKuliah; }

    public int getSks() {
        return kelasKuliah.getCourse().getSks();
    }
}