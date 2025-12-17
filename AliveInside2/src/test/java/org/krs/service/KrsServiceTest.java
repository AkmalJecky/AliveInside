package org.krs.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.krs.model.KelasKuliah;
import org.krs.model.KrsItem;
import org.krs.model.Mahasiswa;
import org.krs.repository.CsvKrsRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KrsServiceTest {

    private KrsService service;

    @BeforeEach
    void setUp() {
        CsvKrsRepository repository = new CsvKrsRepository();
        service = new KrsService(repository);
    }

    @Test
    void generatePackageKrs_shouldCreateItemsFromCsv() {
        Mahasiswa m = new Mahasiswa("220001", "Test Student", "A", 2, 24);

        List<KrsItem> items = service.generatePackageKrs(m);

        assertFalse(items.isEmpty(), "Package KRS should not be empty");
        int totalSks = service.calculateTotalSks(items);
        assertTrue(totalSks > 0, "Total SKS should be > 0");
    }

    @Test
    void canAddCourse_shouldRejectWhenSksExceeded() {
        Mahasiswa m = new Mahasiswa("220002", "Max SKS", "B", 3, 3);

        List<KrsItem> current = new ArrayList<>();
        List<KelasKuliah> sem3 = service.getSemester3Classes();
        assertFalse(sem3.isEmpty(), "Semester 3 class list must not be empty");

        KelasKuliah first = sem3.get(0); // 3 SKS
        current.add(new KrsItem(m, first));

        // try to add another 3 SKS class
        KelasKuliah second = sem3.get(1);
        boolean canAdd = service.canAddCourse(m, current, second);

        assertFalse(canAdd, "Should not be allowed to add course if SKS limit exceeded");
    }
}