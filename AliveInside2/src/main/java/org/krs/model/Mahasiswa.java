package org.krs.model;

public class Mahasiswa {
    private final String nim;
    private final String name;
    private final String kelas;    // "A" or "B"
    private final int semester;
    private final int maxSks;

    public Mahasiswa(String nim, String name, String kelas, int semester, int maxSks) {
        this.nim = nim;
        this.name = name;
        this.kelas = kelas;
        this.semester = semester;
        this.maxSks = maxSks;
    }

    public String getNim() { return nim; }
    public String getName() { return name; }
    public String getKelas() { return kelas; }
    public int getSemester() { return semester; }
    public int getMaxSks() { return maxSks; }

    @Override
    public String toString() {
        return nim + " - " + name + " (Class " + kelas + ", Sem " + semester + ")";
    }
}