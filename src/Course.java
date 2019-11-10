public class Course {
    private int id;
    private String title;
    private String classGrade;
    private int weekHours;

    public Course() {    }

    public Course(int id, String title, String classGrade, int weekHours) {
        this.id = id;
        this.title = title;
        this.classGrade = classGrade;
        this.weekHours = weekHours;
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
}
