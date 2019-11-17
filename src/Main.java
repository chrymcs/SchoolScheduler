//import search.Gene;
import myObjects.Lesson;
import io.Importer;
import myObjects.Teacher;
import search.Chromosome;
import search.Gene;
import search.Genetic;
//import search.Chromosome;
import java.util.HashMap;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        Importer importer = new Importer();
        HashMap<Integer ,Lesson> lessons = null;
        HashMap<Integer ,Teacher> teachers = null;
        //LinkedList<LinkedList<Gene>> genesPerClass = new LinkedList<>();


        try {
            lessons = importer.createLessonsMap(args[0]);
            teachers = importer.createTeachersMap(args[1]);
        } catch (Exception e) {
            System.err.println("No proper arguments given.");
            System.err.println("The arguments should be lessons.txt teachers.txt");
        }

/*        if (lessons!=null && teachers!=null) {
            genesPerClass.add(Gene.createAllCombinationsPerClass(lessons, teachers, "Α"));
            genesPerClass.add(Gene.createAllCombinationsPerClass(lessons, teachers, "Β"));
            genesPerClass.add(Gene.createAllCombinationsPerClass(lessons, teachers, "Γ"));
        }*/


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

        LinkedList<Gene> genes = new LinkedList<>();

        Gene.combinationsPerClass(lessons,teachers,"C");

        //random values for testing
        //Chromosome solution = genetic.start(100, 0.1, 1, 1000);
    }
}