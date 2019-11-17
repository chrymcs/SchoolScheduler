package myObjects;

import java.util.LinkedList;

public class Lesson {

    private int id;
    private String title;
    private String classGrade; //taksi A,B,G
    private int weekHours;
    //poioi kathigites mporoun na didaksoun to mathima
    //dineis kathigiti kai ton petaei sti lista
    private LinkedList<Teacher> availableTeachers;


    public Lesson() {    }

    public Lesson(int id, String title, String classGrade, int weekHours) {
        this.id = id;
        this.title = title;
        this.classGrade = classGrade;
        this.weekHours = weekHours;
    }


// Setters - Getters

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


/*    public LinkedList<Integer> getLessons() {
        return availableTeachers;
    }*/

/*    public void setLessons(LinkedList<Integer> lessons) {
        this.availableTeachers = lessons;
    }*/


    public LinkedList<Teacher> getAvailableTeachers() {
        return availableTeachers;
    }


    public void setAvailableTeachers (Teacher teacher) {
        availableTeachers.add(teacher);
    }


    @Override
    public String toString() {
        return "\n - ID: " + id + "\n - Title: " + title+ "\n - Class: " + classGrade + "\n - Hours per Week: " + weekHours;
    }
}
