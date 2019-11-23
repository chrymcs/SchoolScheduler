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
     @return LinkedList of all possible combinations
     */

    //to allazw. de tha dinw classGrade. tha exw apla mia lista me ola ola ola ta pithana gonidia aneksartitws. oi sindiasmoi mesa tha einai lesson-teacher simfwna
    //me ti mporei na didaksei o kathe kathigitis alla de tha exw 3 listes, mia gia kathe taksi.
    //arxika tha trexei pio grigora to programma
    //deuteron tha exei kai nohma to constraint : kamia taksi na mhn periexei mathimata allis taksis.
    public static LinkedList<Gene> allPossibleGenes (HashMap<Integer,Lesson> lessons, HashMap<Integer,Teacher> teachers, int nullGenes) {

        LinkedList<Gene> genes = new LinkedList<>();

        for (int lessonId: lessons.keySet()) {
            for (int teacherId : teachers.keySet()) {
                if (teachers.get(teacherId).getLessons().contains(lessonId)) { //find which professors teaches this lesson
                    Gene g = new Gene(lessons.get(lessonId),teachers.get(teacherId)); //create a new gene with lesson-teacher combination
                    genes.add(g);
                }
                lessons.get(lessonId).setAvailableTeachers(teachers.get(teacherId));
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