package myObjects;

import java.util.LinkedList;

public class Teacher {

    private int id;
    private String name;
    private LinkedList<Integer> lessons; //a teacher can teach a specific list of lessons
    private int dayHours; //max hours per day
    private int weekHours; //max hours per week

    public Teacher() { }

    public Teacher (int id, String name, LinkedList<Integer> lessons, int dayHours, int weekHours) {
        this.id = id;
        this.name = name;
        this.lessons = lessons;
        this.dayHours = dayHours;
        this.weekHours = weekHours;
    }

// Setters - Getters

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

    public void setLessons(LinkedList<Integer> lessons) {
        this.lessons = lessons;
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
        StringBuilder prefix = new StringBuilder("ID: " + id + " - Name: " + name + " - Lessons' ids: [");
        String suffix = "] - Max daily hours: " + dayHours + " - Max weekly hours: " + weekHours;

        for (int i = 0; i < lessons.size() - 1; i++) {
            prefix.append(lessons.get(i)).append(", ");
        }
        prefix.append(lessons.getLast());
        return prefix + suffix;
    }
}