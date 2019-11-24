import io.Exporter;
import myObjects.Lesson;
import io.Importer;
import myObjects.Teacher;
import search.Chromosome;
import search.Gene;
import search.Genetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Importer importer = new Importer(); //object to call readLessonsFile() and readTeachersFile()

        HashMap<Integer, Lesson> lessons = null; //hashmap to save all lessons
        HashMap<Integer, Teacher> teachers = null; //hashmap to save all teachers

        ArrayList<Lesson> lA = new ArrayList<>(); //arraylist to save lessons of class A
        ArrayList<Lesson> lB = new ArrayList<>(); //arraylist to save lessons of class B
        ArrayList<Lesson> lC = new ArrayList<>(); //arraylist to save lessons of class C

        LinkedList<Gene> genes = new LinkedList<>(); //TODO delete

        try {
            lessons = importer.readLessonsFile(args[0]);
            teachers = importer.readTeachersFile(args[1]);
        } catch (Exception e) {
            System.err.println("No proper arguments given.");
            System.err.println("The arguments should be: \" ./data/lessons.txt ./data/teachers.txt \"");
        }

       /* assert lessons != null;
        for (Map.Entry pair: lessons.entrySet())
            System.out.println(pair.getKey() + ": " + pair.getValue());

        assert teachers != null;
        for (Map.Entry pair: teachers.entrySet())
            System.out.println(pair.getKey() + ": " + pair.getValue());*/

        int nullGenes = 5;

        //Fill lA,lB and lC arraylists
        if (lessons != null && teachers != null) {
            for (Map.Entry pair: lessons.entrySet()) {
                Lesson l = (Lesson) pair.getValue();
                if (l.getClassGrade().equalsIgnoreCase("A")) lA.add(l);
                else if(l.getClassGrade().equalsIgnoreCase("B")) lB.add(l);
                else lC.add(l);
            }
            //genes = Gene.allPossibleGenes(lessons,teachers,nullGenes);
        }

        //fill available teachers
        if (lessons != null && teachers != null) {
            for (int lessonId : lessons.keySet()) {
                for (int teacherId : teachers.keySet()) {
                    if (teachers.get(teacherId).getLessons().contains(lessonId)) { //find which professors teach this lesson
                        lessons.get(lessonId).setAvailableTeachers(teachers.get(teacherId));
                    }
                }
            }
        }


        Genetic genetic = new Genetic(lessons, teachers, lA, lB, lC);
        Chromosome solution = genetic.start(1000, 0.5, 99, 1000);
        Exporter.createExcelOutput(solution.toString());

    }
}