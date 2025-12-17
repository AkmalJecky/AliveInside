package org.krs.repository;

import org.junit.jupiter.api.Test;
import org.krs.model.KelasKuliah;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvKrsRepositoryTest {

    @Test
    void loadPaketSemester2_shouldReadCsv() {
        CsvKrsRepository repo = new CsvKrsRepository();

        List<KelasKuliah> paket = repo.loadPaketSemester2();

        assertFalse(paket.isEmpty(), "paket_sem2.csv should contain at least one class");
        KelasKuliah first = paket.get(0);
        assertNotNull(first.getCourse().getCode());
        assertTrue(first.getCourse().getSks() > 0);
    }
}