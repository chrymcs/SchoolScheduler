package search;

import myObjects.Lesson;
import myObjects.Teacher;


import java.util.HashMap;
import java.util.LinkedList;


/**
 Class represents combinations of lessons-teachers.
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
     This is the main method of the Class. It creates all possible combinations
     of lessons-teachers based on each teacher's potential on teaching a specific
     (or more) subject.
     @param lessons LinkedList of lessons (assuming all)
     @param teachers LinkedList of teachers (assuming all)
     @param classGrade String grade of class (A, B or C)
     @return LinkedList of Combination
     */
    // an kaleseis ti methodo me classGrade=A , tha deis poioi kathigites didaskoun to kathe mathima tis taksis. Omoiws kai me classGrade=B or C
    public static LinkedList<Gene> combinationsPerClass (HashMap<Integer,Lesson> lessons,
                                                         HashMap<Integer,Teacher> teachers,
                                                         String classGrade, int nullGenes) {

        //System.out.println("\nFor every lesson in " + classGrade + "' class, who can teach it?\n");
        LinkedList<Gene> genes = new LinkedList<>();

        for (int lessonId: lessons.keySet()) {
            if (lessons.get(lessonId).getClassGrade().equals(classGrade)) { //an taksi pou didasketai to dwsmeno mathima = taksi pou dinetai
                for (int teacherId : teachers.keySet()) {
                    if (teachers.get(teacherId).getLessons().contains(lessonId)) { //vres poioi kathigites to didaskoun
                        Gene g = new Gene(lessons.get(lessonId),teachers.get(teacherId)); //ftiakse new gonidio
                        genes.add(g); //prosthese to sthn lista
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

    @Override
    public String toString() {
        return "";
    }
}