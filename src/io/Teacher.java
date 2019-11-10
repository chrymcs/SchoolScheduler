package io;

import java.util.LinkedList;

public class Teacher {

    private int id;
    private String name;
    private LinkedList<Integer> lessons;
    private int dayHours;
    private int weekHours;

    public Teacher() { }

    public Teacher (int id, String name, LinkedList<Integer> courses, int dayHours, int weekHours) {
        this.id = id;
        this.name = name;
        this.lessons = courses;
        this.dayHours = dayHours;
        this.weekHours = weekHours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Integer> getLessons() {
        return lessons;
    }

    public void setLessons(LinkedList<Integer> courses) {
        this.lessons = courses;
    }

    public int getDayHours() {
        return dayHours;
    }

    public void setDayHours(int dayHours) {
        this.dayHours = dayHours;
    }

    public int getWeekHours() {
        return weekHours;
    }

    public void setWeekHours(int weekHours) {
        this.weekHours = weekHours;
    }

    @Override
    public String toString() {
        StringBuilder prefix = new StringBuilder("ID: " + id + " Name: " + name + " Lessons: [");
        String suffix = "] Daily hours: " + dayHours + " Weekly hours: " + weekHours;

        for (int i = 0; i < lessons.size() - 1; i++) {
            prefix.append(lessons.get(i)).append(", ");
        }
        prefix.append(lessons.getLast());
        return prefix + suffix;
    }
}
