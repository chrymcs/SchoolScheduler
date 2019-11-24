package search;

import myObjects.Lesson;
import myObjects.Teacher;

/**
 * A gene is a lesson-teacher combination.
*/
public class Gene {

    private Lesson lesson;
    private Teacher teacher;

    public Gene(Lesson lesson, Teacher teacher) {
        this.lesson = lesson;
        this.teacher = teacher;
    }

//Setters - Getters

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}