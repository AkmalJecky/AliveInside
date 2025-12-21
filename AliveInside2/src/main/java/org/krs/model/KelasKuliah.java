package org.krs.model;

public class KelasKuliah {
    private final MataKuliah course;
    private final String classCode;   // e.g. "IF201-2A"
    private final String day;
    private final String startTime;
    private final String endTime;
    private final String room;
    private final int capacity;
    private int currentEnrolled;

    public KelasKuliah(MataKuliah course,
                       String classCode,
                       String day,
                       String startTime,
                       String endTime,
                       String room,
                       int capacity) {
        this.course = course;
        this.classCode = classCode;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.capacity = capacity;
        this.currentEnrolled = 0;
    }

    public MataKuliah getCourse() { return course; }
    public String getClassCode() { return classCode; }
    public String getDay() { return day; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getRoom() { return room; }
    public int getCapacity() { return capacity; }
    public int getCurrentEnrolled() {
        return currentEnrolled;
    }
    public void setCurrentEnrolled(int currentEnrolled) {
        this.currentEnrolled = currentEnrolled;
    }
    public boolean isFull() {
        return currentEnrolled >= capacity;
    }

    public void incrementEnrolled() {
        if (currentEnrolled < capacity) {
            currentEnrolled++;
        }
    }

    public void decrementEnrolled() {
        if (currentEnrolled > 0) {
            currentEnrolled--;
        }
    }

    @Override
    public String toString() {
        return "%s - %s %s %s-%s @ %s (%d SKS)"
                .formatted(
                        course.getCode(),
                        classCode,
                        day,
                        startTime,
                        endTime,
                        room,
                        course.getSks()
                );
    }
}