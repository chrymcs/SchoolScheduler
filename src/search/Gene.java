package search;

import myObjects.Lesson;
import myObjects.Teacher;

import java.util.HashMap;
import java.util.LinkedList;

/**
 Class to represent combinations of lessons-teachers.
 Based on the hypothesis declaration that no teacher can be assigned
 to lessons that he is not eligible to teach.
*/
public class Gene {
    private Lesson lesson;
    private Teacher teacher;

    public Gene(Lesson lesson, Teacher teacher) {
        this.lesson = lesson;
        this.teacher = teacher;
    }

    /**
     This is the main method of the Class. It creates all possible combinations
     of lessons-teachers based on each teacher's potential on teaching a specific
     (or more) subject.
     @param lessons LinkedList of lessons (assuming all)
     @param teachers LinkedList of teachers (assuming all)
     @param classGrade String grade of class (A, B or C)
     @return LinkedList of Combination
     */
    public static LinkedList<Gene> createAllCombinationsPerClass (HashMap<Integer,Lesson> lessons,
                                                                  HashMap<Integer,Teacher> teachers,
                                                                 String classGrade) {

        LinkedList<Gene> genes = new LinkedList<>();
        for (int lessonId: lessons.keySet()) {
            if (lessons.get(lessonId).getClassGrade().equals(classGrade)) {
                for (int teacherId : teachers.keySet()) {
                    if (teachers.get(teacherId).getLessons().contains(lessonId))
                        genes.add(new Gene(lessons.get(lessonId), teachers.get(teacherId)));
                    lessons.get(lessonId).updateAvailableTeachers(teachers.get(teacherId));
                }
            }
        }
        genes.add(null); //Null combination to represent no assignment
        return genes;
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
