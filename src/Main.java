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

        LinkedList<Gene> genesA = new LinkedList<>(); //all genes of A' class
        LinkedList<Gene> genesB = new LinkedList<>(); //all genes of B' class
        LinkedList<Gene> genesC = new LinkedList<>(); //all genes of C' class

        LinkedList<LinkedList<Gene>> genes = new LinkedList<>();

        try {
            lessons = importer.readLessonsFile(args[0]);
            teachers = importer.readTeachersFile(args[1]);
        } catch (Exception e) {
            System.err.println("No proper arguments given.");
            System.err.println("The arguments should be: \" ./data/lessons.txt ./data/teachers.txt \"");
        }
        int nullGenes = 5;
        if (lessons!=null && teachers!=null) {
            genesA = Gene.combinationsPerClass(lessons, teachers, "A", nullGenes);
            genesB = Gene.combinationsPerClass(lessons, teachers, "B", nullGenes);
            genesC = Gene.combinationsPerClass(lessons, teachers, "C", nullGenes);
        }

        //put all genes in one list
        genes.add(genesA);
        genes.add(genesB);
        genes.add(genesC);


        Genetic genetic = new Genetic(lessons, teachers, genes);

        genetic.initializePopulation(5);

        Exporter.createExcelOutput(genetic.getPopulation().get(0).toString());
        genetic.getPopulation().get(0).calculateUnevenDistributedHoursPerLesson();


        //random values for testing
        //Chromosome solution = genetic.start(100, 0.1, 1, 1000);
    }
}