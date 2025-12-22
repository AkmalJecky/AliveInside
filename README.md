# 📘 KRS Desktop App

A simple desktop application for managing **Kartu Rencana Studi (KRS)** using **Java Swing** and **CSV** as the data store.  
The app implements a clear separation between **UI**, **service (business logic)**, and **repository (data access)** layers.

---

## ✨ Features

- 🔐 **Student Login**
  - Login with NIM + Name, validated against `mahasiswa.csv`.
- 🎓 **Class A (Semester 2)**
  - Gets a **fixed package** of courses that cannot be modified.
- 📚 **Class B (Semester 3)**
  - Can select courses freely from available classes (`mk_sem3.csv`).
- ✅ **Business Rules**
  - Prevent duplicate class or course in KRS.
  - Check schedule conflicts (day + time overlap).
  - Enforce student SKS limit.
  - Enforce class capacity using enrollment count.
- 📝 **KRS Management**
  - View current KRS and delete selected items (Class B).
- ✏️ **Update Student Name**
  - Update name in memory and sync to both `krs.csv` and `mahasiswa.csv`.

---

## 🗂 Project Structure

    ├── data/
    │ ├── krs.csv
    │ ├── mahasiswa.csv
    │ ├── mk_sem3.csv
    │ └── paket_sem2.csv
    ├── src/
    │ └── main/
    │ ├── java/
    │ │ └── org/
    │ │ └── krs/
    │ │ ├── model/
    │ │ │ ├── KelasKuliah.java
    │ │ │ ├── KrsItem.java
    │ │ │ ├── Mahasiswa.java
    │ │ │ └── MataKuliah.java
    │ │ ├── repository/
    │ │ │ ├── CsvKrsRepository.java
    │ │ │ └── MahasiswaCsvRepository.java
    │ │ ├── service/
    │ │ │ └── KrsService.java
    │ │ └── ui/
    │ │ ├── LoginPanel.java
    │ │ ├── MainFrame.java
    │ │ ├── PanelKelasA.java
    │ │ ├── PanelKelasB.java
    │ │ └── PanelPilihMatkulB.java
    │ └── resources/
    │ └── img/
    │ └── logokhs.png


---

## 🧩 Layer Overview

| Layer        | Package              | Responsibility                                                |
|-------------|----------------------|----------------------------------------------------------------|
| Model       | `org.krs.model`      | Domain objects: `Mahasiswa`, `MataKuliah`, `KelasKuliah`, `KrsItem`. |
| Repository  | `org.krs.repository` | Read/write CSV for students and KRS data.                     |
| Service     | `org.krs.service`    | Business rules: SKS, conflict, capacity, update name.        |
| UI          | `org.krs.ui`         | Swing panels and navigation via `MainFrame`.                 |

---

## 💾 Data Files

All CSV files are stored under `data/`.

- **`mahasiswa.csv`**  
  - Columns: `nim,nama,kelas` (A/B). Used for login and class detection.
- **`paket_sem2.csv`**  
  - Master data for semester 2 package classes.
- **`mk_sem3.csv`**  
  - Master data for semester 3 classes.
- **`krs.csv`**  
  - All KRS records. Header:  
    `nim,nama,semester,semester_copy,kode_mk,nama_mk,sks,kode_kelas,hari,jam_mulai,jam_selesai,ruangan`.

---

## 🚀 How to Run

1. Ensure **JDK 8+** is installed.
2. Make sure the `data/` folder and CSV files exist with valid content.
3. Open the project in your IDE (IntelliJ/Eclipse).
4. Set `MainFrame` as the main class and run the application.
5. Login using a NIM and name that exist in `mahasiswa.csv`.

---

## 🔍 Main Flows

- **Login** → `LoginPanel` → validate to `MahasiswaCsvRepository` → set `currentStudent` in `MainFrame`.  
- **Class A** → `PanelKelasA` → load fixed package via `KrsService.getPaketSemester2()` → display only.  
- **Class B Select Courses** → `PanelPilihMatkulB` → show available classes with enrollment → validate using `KrsService.canAddCourse()` → save to `krs.csv`.  
- **Class B View/Delete KRS** → `PanelKelasB` → load `loadExistingKrs()` → delete via `deleteKrsItem()`.

---

## 👨‍💻 Authors

- **AkmalJecky (Jeckz)** – Frontend / UI (Java Swing panels, layout, and visual design).
- **xzilliazia (Zia)** – Backend / Logic (services, repositories, CSV handling, and business rules).
