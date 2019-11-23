import io.Exporter;
import myObjects.Lesson;
import io.Importer;
import myObjects.Teacher;
import search.Gene;
import search.Genetic;
import java.util.HashMap;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        Importer importer = new Importer();
        HashMap<Integer ,Lesson> lessons = null;
        HashMap<Integer ,Teacher> teachers = null;

        LinkedList<Gene> genes = new LinkedList<>();

        try {
            lessons = importer.readLessonsFile(args[0]);
            teachers = importer.readTeachersFile(args[1]);
        } catch (Exception e) {
            System.err.println("No proper arguments given.");
            System.err.println("The arguments should be: \" ./data/lessons.txt ./data/teachers.txt \"");
        }

        int nullGenes = 5;

        if (lessons!=null && teachers!=null) {
            genes = Gene.allPossibleGenes(lessons,teachers,nullGenes);
        }

        Genetic genetic = new Genetic(lessons, teachers, genes);
        genetic.initializePopulation(5);
        Exporter.createExcelOutput(genetic.getPopulation().get(0).toString());


        //Kanonika tha prepei na kaloume ta parakatw kai na paragei apotelesma:
        //Genetic genetic = new Genetic(lessons, teachers, genes);
        //Chromosome solution = genetic.start(100, 0.1, 1, 1000);
        //Exporter.createExcelOutput(solution.toString());


        //random values for testing
        //Chromosome solution = genetic.start(100, 0.1, 1, 1000);
    }
}