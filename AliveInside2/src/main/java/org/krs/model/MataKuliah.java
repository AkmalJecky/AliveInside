package org.krs.model;

public class MataKuliah {
    private final String code;
    private final String name;
    private final int sks;

    public MataKuliah(String code, String name, int sks) {
        this.code = code;
        this.name = name;
        this.sks = sks;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getSks() { return sks; }

    @Override
    public String toString() {
        return code + " - " + name + " (" + sks + " SKS)";
    }
}