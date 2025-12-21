package org.krs.model;

public class Mahasiswa {
    private final String nim;
    private String name;
    private final int semester;
    private final int maxSks;

    public Mahasiswa(String nim, String name, int semester, int maxSks) {
        this.nim = nim;
        this.name = name;
        this.semester = semester;
        this.maxSks = maxSks;
    }

    public String getNim() { return nim; }
    public String getName() { return name; }
    public int getSemester() { return semester; }
    public int getMaxSks() { return maxSks; }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return nim + " - " + name + " (Sem " + semester + ")";
    }
}