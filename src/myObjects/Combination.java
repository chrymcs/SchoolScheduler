package myObjects;

import java.util.LinkedList;

/**
 Class to represent combinations of lessons-teachers.
 Based on the hypothesis declaration that no teacher can be assigned
 to lessons that he is not eligible to teach.
*/
public class Combination {
    private Lesson lesson;
    private Teacher teacher;

    public Combination (Lesson lesson, Teacher teacher) {
        this.lesson = lesson;
        this.teacher = teacher;
    }

    /**
     This is the main method of the Class. It creates all possible combinations
     of lessons-teachers based on each teacher's potential on teaching a specific
     (or more) subject.
     @param lessons LinkedList of lessons (assuming all)
     @param teachers LinkedList of teachers (assuming all)
     @return LinkedList of Combination
     */
    public static LinkedList<Combination> createAllCombinations (LinkedList<Lesson> lessons,
                                                          LinkedList<Teacher> teachers ) {
        LinkedList<Combination> combinations = new LinkedList<>();
        for (Lesson lesson: lessons) {
            for (Teacher teacher: teachers) {
                if (teacher.getLessons().contains(lesson.getId()))
                    combinations.add(new Combination(lesson, teacher));
            }
        }
        return combinations;
    }

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
