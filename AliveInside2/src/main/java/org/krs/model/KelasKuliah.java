package org.krs.model;

public class KelasKuliah {
    private final MataKuliah course;
    private final String classCode;   // e.g. "IF201-2A"
    private final String day;
    private final String startTime;
    private final String endTime;
    private final String room;
    private final int capacity;

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
    }

    public MataKuliah getCourse() { return course; }
    public String getClassCode() { return classCode; }
    public String getDay() { return day; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getRoom() { return room; }
    public int getCapacity() { return capacity; }

    @Override
    public String toString() {
        return course.getCode() + "-" + classCode + " " +
                day + " " + startTime + "-" + endTime + " @ " + room;
    }
}