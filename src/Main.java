//import search.Gene;
import io.Exporter;
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

        LinkedList<Gene> genesA = new LinkedList<>(); //gonidia A gymnasiou
        LinkedList<Gene> genesB = new LinkedList<>(); //gonidia B gymnasiou
        LinkedList<Gene> genesC = new LinkedList<>(); //gonidia C gymnasiou

        LinkedList<LinkedList<Gene>> genes = new LinkedList<>();

        try {
            lessons = importer.createLessonsMap(args[0]);
            teachers = importer.createTeachersMap(args[1]);
        } catch (Exception e) {
            System.err.println("No proper arguments given.");
            System.err.println("The arguments should be lessons.txt teachers.txt");
        }
        int nullGenes = 5;
        if (lessons!=null && teachers!=null) {
            genesA = Gene.combinationsPerClass(lessons, teachers, "A", nullGenes);
            genesB = Gene.combinationsPerClass(lessons, teachers, "B", nullGenes);
            genesC = Gene.combinationsPerClass(lessons, teachers, "C", nullGenes);
        }

        //vale ola ta gonidia se mia lista.
        genes.add(genesA);
        genes.add(genesB);
        genes.add(genesC);

        Genetic genetic = new Genetic(lessons, teachers, genes);
        //genetic.lessonsList();
        //genetic.teachersList();
        genetic.initializePopulation(100);

        Exporter.createExcelOutput(genetic.getPopulation().get(0).toString());


        //thelw na dw ti exei mesa h lista genes h opoia apoteleitai apo 3 listes (ta genes twn 3 taksewn)
        System.out.println("\nLINKED LIST 'genes' \n");

        //genes.get(0) -> genesA
        //genes.get(1) -> genesB
        //genes.get(2) -> genesC

        for (int i=0; i<genes.size(); i++) {
            if (i==0) { //genesA
                System.out.println("A' Gymnasiou");
                for (int a=0; a<genesA.size(); a++){
                    //me to genes.get(i) exw epileksei 1 apo tis 3 listes kai me to .get(a) paw mesa sthn lista a
                    System.out.println(genes.get(i).get(a).getLesson().getTitle() + " " + genes.get(i).get(a).getLesson().getClassGrade() + "' - " + genes.get(i).get(a).getTeacher().getName());
                }
            }
            else if (i==1) { //genesB
                System.out.println("\nB' Gymnasiou");
                for (int b=0; b<genesB.size(); b++){
                    System.out.println(genes.get(i).get(b).getLesson().getTitle() + " " + genes.get(i).get(b).getLesson().getClassGrade() + "' - " + genes.get(i).get(b).getTeacher().getName());
                }
            }
            else { //genesC
                System.out.println("\nC' Gymnasiou");
                for (int c=0; c<genesC.size(); c++){
                    System.out.println(genes.get(i).get(c).getLesson().getTitle() + " " + genes.get(i).get(c).getLesson().getClassGrade() + "' - " + genes.get(i).get(c).getTeacher().getName());
                }
            }
        }

        //random values for testing
        //Chromosome solution = genetic.start(100, 0.1, 1, 1000);
    }
}