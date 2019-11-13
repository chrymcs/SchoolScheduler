package myObjects;

import java.util.LinkedList;

public class Lesson {

    private int id;
    private String title;
    private String classGrade;
    private int weekHours;
    private LinkedList<Integer> availableTeachers;


    public Lesson() {    }

    public Lesson(int id, String title, String classGrade, int weekHours) {
        this.id = id;
        this.title = title;
        this.classGrade = classGrade;
        this.weekHours = weekHours;
    }

    public void updateAvailableTeachers (Teacher teacher) {
        this.availableTeachers.add(teacher.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassGrade() {
        return classGrade;
    }

    public void setClassGrade(String classGrade) {
        this.classGrade = classGrade;
    }

    public int getWeekHours() {
        return weekHours;
    }

    public void setWeekHours(int weekHours) {
        this.weekHours = weekHours;
    }

    public LinkedList<Integer> getLessons() {
        return availableTeachers;
    }

    public void setLessons(LinkedList<Integer> lessons) {
        this.availableTeachers = lessons;
    }

    public LinkedList<Integer> getAvailableTeachers() {
        return availableTeachers;
    }

    public void setAvailableTeachers(LinkedList<Integer> availableTeachers) {
        this.availableTeachers = availableTeachers;
    }

    @Override
    public String toString() {
        return "ID: " + id + " Title: " + title + " Class: " + classGrade + " Hours per Week: " + weekHours;
    }
}
