
package search;

import myObjects.Lesson;
import myObjects.Teacher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class Genetic {

    private ArrayList<Chromosome> population;
    private HashMap<Integer,Lesson> allLessons;
    private HashMap<Integer,Teacher> allTeachers;
    private ArrayList<Integer> fitnessBounds;
    private LinkedList<LinkedList<Gene>> genes;

    public Genetic (HashMap<Integer,Lesson> allLessons,
                    HashMap<Integer,Teacher> allTeachers,
                    LinkedList<LinkedList<Gene>> genesList) {
        this.population = null;
        this.allLessons = allLessons;
        this.allTeachers = allTeachers;
        this.fitnessBounds = null;
        this.genes = genesList;
    }

    //test methods to check what my lists allLessons & allTeachers have already saved
    public void lessonsList () {
        for (int i = 0; i <= allLessons.size(); i++) {
            System.out.println(allLessons.get(i));
        }
    }
    public void teachersList () {
        for (int i = 3140; i <= 3151; i++) {
            System.out.println(allTeachers.get(i));
        }
    }

    /**
     * @param populationSize: The size of the population in every step
     * @param mutationProbability: The propability a mutation might occur in a chromosome
     * @param minimumFitness: The minimum fitness value of the solution we wish to find
     * @param maximumSteps: The maximum number of steps we will search for a solution
     */

    public void start (int populationSize, double mutationProbability, int minimumFitness,
                       int maximumSteps) {
        this.initializePopulation(populationSize);
        //return new Chromosome(population);
    }

    //initialization for the population
    public void initializePopulation (int populationSize) {
        this.population = new ArrayList <>();
        for (int i=0 ; i < populationSize ; i++) {
            this.population.add(new Chromosome(genes, allTeachers, allLessons));
        }
        this.updateFitnessBounds();
    }

    private void updateFitnessBounds () {
        this.fitnessBounds = new ArrayList<Integer>();
        for (int i=0; i<this.population.size(); i++)
        {
            for(int j=0; j<this.population.get(i).getFitness(); j++)
            {
                //Each chromosome index exists in the ArrayList as many times as its fitness score
                //By creating this ArrayList so, and choosing a random index from it,
                //the greater the fitness score of a chromosome the greater chance it will be chosen.
                fitnessBounds.add(i);
            }
        }
    }

    public Chromosome reproduce (Chromosome x, Chromosome y) {
        Random r = new Random();
        int intersectionPointClass = r.nextInt(2);
        int intersectionPointSubClass = r.nextInt(2);
        int intersectionPointDay= r.nextInt(4);
        int intersectionPointHour = r.nextInt(6);
        Gene [][][][] childChromosomeA = new Gene [3][3][7][5];
        Gene [][][][] childChromosomeB = new Gene [3][3][7][5];
        //The child has the left side of the x chromosome up to the intersection point...
        for (int c=0; c < intersectionPointClass; c++) {
            for (int s = 0; s < intersectionPointSubClass; s++) {
                for (int d = 0; d < intersectionPointDay; d++) {
                    for (int h = 0; h < intersectionPointHour; h++) {
                        childChromosomeA[c][s][d][h] = x.getGenes()[c][s][d][h];
                        childChromosomeB[c][s][d][h] = y.getGenes()[c][s][d][h];
                    }
                }
            }
        }
        //...and the right side of the y chromosome after the intersection point
        for (int c = intersectionPointClass; c < 3; c++) {
            for (int s = intersectionPointSubClass; s < 3; s++) {
                for (int d = intersectionPointDay; d < 5; d++) {
                    for (int h = intersectionPointHour; h < 7; h++) {
                        childChromosomeA[c][s][d][h] = y.getGenes()[c][s][d][h];
                        childChromosomeB[c][s][d][h] = x.getGenes()[c][s][d][h];
                    }
                }
            }
        }
        return new Chromosome(childChromosomeA);
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }

    public void setPopulation(ArrayList<Chromosome> population) {
        this.population = population;
    }
}