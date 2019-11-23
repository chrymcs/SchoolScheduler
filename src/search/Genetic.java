package search;

import myObjects.Lesson;
import myObjects.Teacher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Collections;


public class Genetic {

    //ArrayList that contains the current population of chromosomes
    private ArrayList<Chromosome> population;

    /*
     * ArrayList that contains indexes of the chromosomes in the population ArrayList
     * Each chromosome index exists in the ArrayList as many times as its fitness score
     * By creating this ArrayList so, and choosing a random index from it,
     * the greater the fitness score of a chromosome the greater chance it will be chosen.
     */
    private ArrayList<Integer> fitnessBounds;

    private HashMap<Integer,Lesson> allLessons;
    private HashMap<Integer,Teacher> allTeachers;
    private LinkedList<Gene> genes;

    public Genetic (HashMap<Integer,Lesson> allLessons, HashMap<Integer,Teacher> allTeachers, LinkedList<Gene> genesList) {
        this.population = null;
        this.fitnessBounds = null;
        this.allLessons = allLessons;
        this.allTeachers = allTeachers;
        this.genes = genesList;
    }

    /**
     * @param populationSize: The size of the population in every step
     * @param mutationProbability: The propability a mutation might occur in a chromosome
     * @param maximumFitness: The maximum fitness value of the solution we wish to find
     * @param maximumSteps: The maximum number of steps we will search for a solution
     */

    public Chromosome start (int populationSize, double mutationProbability, int maximumFitness,
                       int maximumSteps) {

        //TODO. O parakatw kwdikas einai tou lab3

        //We initialize the population
        this.initializePopulation(populationSize);

        Random r = new Random();

        for(int step=0; step < maximumSteps; step++)
        {
            //Initialize the new generated population
            ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();

            for(int i=0; i < populationSize; i++)
            {
                //We choose two chromosomes from the population
                //Due to how fitnessBounds ArrayList is generated, the propability of
                //selecting a specific chromosome depends on its fitness score
                int xIndex = this.fitnessBounds.get(r.nextInt(this.fitnessBounds.size()));
                Chromosome x = this.population.get(xIndex);
                int yIndex = this.fitnessBounds.get(r.nextInt(this.fitnessBounds.size()));
                while(yIndex == xIndex)
                {
                    yIndex = this.fitnessBounds.get(r.nextInt(this.fitnessBounds.size()));
                }
                Chromosome y = this.population.get(yIndex);

                //We generate the children of the two chromosomes.
                Offspring children = this.reproduce(x,y);
                Chromosome child1 = children.child1;
                Chromosome child2 = children.child2;

                //We might then mutate one of the children or both of them (or none).
                if(r.nextDouble() < mutationProbability)
                {
                    //TODO code mutate() method in Chromosome class
                    child1.mutate();
                    child2.mutate();
                }
                //...and finally add it to the new population
                newPopulation.add(child1);
                newPopulation.add(child2);

            }
            this.population = new ArrayList<Chromosome>(newPopulation);

            //TODO apo edw kai katw prepei na doume ligo ta fitness mas. Se emas oso megalutero einai to fitness, toso xeirotera!

            //We sort the population so the one with the minimum fitness is first
            this.population.sort(Collections.reverseOrder());
            //If the chromosome with the best fitness is acceptable we return it
            if(this.population.get(0).getFitness() <= maximumFitness)
            {
                System.out.println("Finished after " + step + " steps...");
                return this.population.get(0);
            }
            //We update the fitnessBounds arrayList
            this.updateFitnessBounds();
        }

        System.out.println("Finished after " + maximumSteps + " steps...");
        return this.population.get(0);

        //return new Chromosome(population);
    }

    //initialization for the population
    public void initializePopulation (int populationSize) {
        this.population = new ArrayList <Chromosome>();
        for (int i=0 ; i < populationSize ; i++) {
            this.population.add(new Chromosome(genes, allTeachers, allLessons));
        }
        this.updateFitnessBounds();
    }

    //Updates the arraylist that contains indexes of the chromosomes in the population ArrayList
    private void updateFitnessBounds () {
        this.fitnessBounds = new ArrayList<Integer>();
        for (int i=0; i<this.population.size(); i++)
        {
            for(int j=0; j<this.population.get(i).getFitness(); j++)
            {
                //TODO nai alla se emas oso megalwnai to fitness, toso xeirotera einai! de theloume na dialeksei to xeirotero.
                //Each chromosome index exists in the ArrayList as many times as its fitness score
                //By creating this ArrayList so, and choosing a random index from it,
                //the MINIMUM the fitness score of a chromosome the greater chance it will be chosen.
                fitnessBounds.add(i);
            }
        }
    }

    public Offspring reproduce (Chromosome x, Chromosome y) {

        Random r = new Random();

        int intersectionPointClass = r.nextInt(2);
        int intersectionPointSubClass = r.nextInt(2);
        int intersectionPointDay= r.nextInt(4);
        int intersectionPointHour = r.nextInt(6);

        Gene [][][][] childChromosomeA = new Gene [3][3][7][5];
        Gene [][][][] childChromosomeB = new Gene [3][3][7][5];

        //The 1st child has the left side of the x chromosome up to the intersection point...
        //The 2nd child has the left side of the y chromosome up to the intersection point...
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
        //...and the right side of the x chromosome after the intersection point
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

        return new Offspring(new Chromosome(childChromosomeA), new Chromosome(childChromosomeB));
    }

    class Offspring { //inner class to help return 2 objects in reproduce() method
        private Chromosome child1;
        private Chromosome child2;

        Offspring(Chromosome chromosomeA, Chromosome chromosomeB) {
            this.child1 = chromosomeA;
            this.child2 = chromosomeB;
        }
    }


    public ArrayList<Chromosome> getPopulation() {
        return population;
    }

    public void setPopulation(ArrayList<Chromosome> population) {
        this.population = population;
    }

}