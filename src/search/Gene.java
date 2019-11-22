package search;

import myObjects.Lesson;
import myObjects.Teacher;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * A gene is a lesson-teacher combination.
 * Based on the hypothesis declaration that no teacher can be assigned
 * to lessons that he is not eligible to teach.
*/
public class Gene {

    private Lesson lesson;
    private Teacher teacher;

    private Gene(Lesson lesson, Teacher teacher) {
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

    /**
     The method below, creates all possible lesson-teacher combinations, based on each teacher's potential on teaching a specific (or more) subject.
     @param lessons LinkedList of lessons (assuming all)
     @param teachers LinkedList of teachers (assuming all)
     @param classGrade String grade of class (A, B or C)
     @return LinkedList of all possible combinations
     */

    public static LinkedList<Gene> combinationsPerClass (HashMap<Integer,Lesson> lessons, HashMap<Integer,Teacher> teachers, String classGrade, int nullGenes) {

        //For every lesson in " + classGrade + "' class, who can teach it?

        LinkedList<Gene> genes = new LinkedList<>();

        for (int lessonId: lessons.keySet()) {
            if (lessons.get(lessonId).getClassGrade().equals(classGrade)) { //if classGrade of lesson (A,B or C) equals the given classGrade
                for (int teacherId : teachers.keySet()) {
                    if (teachers.get(teacherId).getLessons().contains(lessonId)) { //find which professors teaches this lesson
                        Gene g = new Gene(lessons.get(lessonId),teachers.get(teacherId)); //create a new gene with lesson-teacher combination
                        genes.add(g);
                    }
                    lessons.get(lessonId).setAvailableTeachers(teachers.get(teacherId));
                }
            }
        }
        //combination of null lesson with null teacher
        Lesson l = new Lesson(-1, "NULL", null, 0);
        Teacher t = new Teacher(-1, "NULL", null, 0, 0);
        Gene g = new Gene(l,t);

        for (int i = 0; i < nullGenes; i++) {
            genes.add(g);
        }
        return genes;
    }
}