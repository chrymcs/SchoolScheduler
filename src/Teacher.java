import java.util.LinkedList;

public class Teacher {
    private int id;
    private String name;
    private LinkedList<Integer> courses;
    private int dayHours;
    private int weekHours;

    public Teacher () { }

    public Teacher (int id, String name, LinkedList<Integer> courses, int dayHours, int weekHours) {
        this.id = id;
        this.name = name;
        this.courses = courses;
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

    public LinkedList<Integer> getCourses() {
        return courses;
    }

    public void setCourses(LinkedList<Integer> courses) {
        this.courses = courses;
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

}
