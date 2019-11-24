import io.Exporter;
import myObjects.Lesson;
import io.Importer;
import myObjects.Teacher;
import search.Chromosome;
import search.Gene;
import search.Genetic;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Importer importer = new Importer();
        HashMap<Integer, Lesson> lessons = null;
        HashMap<Integer, Teacher> teachers = null;

        LinkedList<Gene> genes = new LinkedList<>();

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

        if (lessons!=null && teachers!=null) {
            genes = Gene.allPossibleGenes(lessons,teachers,nullGenes);
        }


        Genetic genetic = new Genetic(lessons, teachers, genes);
        Chromosome solution = genetic.start(1000, 0.1,
                                    99, 1000);
        Exporter.createExcelOutput(solution.toString());

    }
}