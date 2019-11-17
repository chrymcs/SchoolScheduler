//import search.Gene;
import myObjects.Lesson;
import io.Importer;
import myObjects.Teacher;
import search.Chromosome;
import search.Gene;
import search.Genetic;
//import search.Chromosome;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        Importer importer = new Importer();
        HashMap<Integer ,Lesson> lessons = null;
        HashMap<Integer ,Teacher> teachers = null;
        //LinkedList<LinkedList<Gene>> genesPerClass = new LinkedList<>();
        LinkedList<Gene> genesA = new LinkedList<>(); //gonidia A gymnasiou
        LinkedList<Gene> genesB = new LinkedList<>(); //gonidia B gymnasiou
        LinkedList<Gene> genesC = new LinkedList<>(); //gonidia C gymnasiou



        try {
            lessons = importer.createLessonsMap(args[0]);
            teachers = importer.createTeachersMap(args[1]);
        } catch (Exception e) {
            System.err.println("No proper arguments given.");
            System.err.println("The arguments should be lessons.txt teachers.txt");
        }

        if (lessons!=null && teachers!=null) {
            genesA = Gene.combinationsPerClass(lessons, teachers, "A");
            genesB = Gene.combinationsPerClass(lessons, teachers, "B");
            genesC = Gene.combinationsPerClass(lessons, teachers, "C");
        }


/*        if (lessons != null) {
            System.out.println("Lessons:");
            for (int lessonId: lessons.keySet())
                System.out.println(lessons.get(lessonId));
        }

        if (teachers != null) {
            System.out.println("Teachers:");
            for (int teacherId: teachers.keySet())
                System.out.println(teachers.get(teacherId));
        }*/

        Genetic genetic = new Genetic(lessons, teachers);

        //genetic.lessonsList();
        //genetic.teachersList();


        //thelw na dw ti exoun mesa oi listes genesA, genesB, genesC.
        System.out.println("\n LINKED LIST genes \n");

        for (int i=0; i<genesA.size(); i++) {
            System.out.println(genesA.get(i).getLesson().getTitle());
            System.out.println(genesA.get(i).getLesson().getClassGrade() + "' Gymnasiou");
            System.out.println(genesA.get(i).getTeacher().getName());
        }
        for (int i=0; i<genesB.size(); i++) {
            System.out.println(genesB.get(i).getLesson().getTitle());
            System.out.println(genesB.get(i).getLesson().getClassGrade() + "' Gymnasiou");
            System.out.println(genesB.get(i).getTeacher().getName());
        }
        for (int i=0; i<genesC.size(); i++) {
            System.out.println(genesC.get(i).getLesson().getTitle());
            System.out.println(genesC.get(i).getLesson().getClassGrade() + "' Gymnasiou");
            System.out.println(genesC.get(i).getTeacher().getName());
        }


        //random values for testing
        //Chromosome solution = genetic.start(100, 0.1, 1, 1000);
    }
}