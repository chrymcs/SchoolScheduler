import io.Exporter;
import myObjects.Lesson;
import io.Importer;
import myObjects.Teacher;
import search.Chromosome;
import search.Genetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Importer importer = new Importer(); //Importer object to call readLessonsFile() and readTeachersFile()

        HashMap<Integer, Lesson> lessons = null; //hashmap to save all lessons
        HashMap<Integer, Teacher> teachers = null; //hashmap to save all teachers

        ArrayList<Lesson> lA = new ArrayList<>(); //arraylist to save lessons of class A
        ArrayList<Lesson> lB = new ArrayList<>(); //arraylist to save lessons of class B
        ArrayList<Lesson> lC = new ArrayList<>(); //arraylist to save lessons of class C

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

        //Fill lA,lB and lC arraylists
        if (lessons != null && teachers != null) {
            for (Map.Entry pair: lessons.entrySet()) {
                Lesson l = (Lesson) pair.getValue();
                if (l.getClassGrade().equalsIgnoreCase("A")) lA.add(l);
                else if(l.getClassGrade().equalsIgnoreCase("B")) lB.add(l);
                else lC.add(l);
            }
        }

        //combination of null lesson with null teacher
        Lesson l = new Lesson(-1, "NULL", null, 0);
        lA.add(l);
        lB.add(l);
        lC.add(l);


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

        //let's go!
        Genetic genetic = new Genetic(lessons, teachers, lA, lB, lC);
        Chromosome solution = genetic.start(100, 0.7, 98, 1000);
        Exporter.createExcelOutput(solution.toString());

    }
}