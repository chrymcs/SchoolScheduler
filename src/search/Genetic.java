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
    //periexei akeraious. px to 0 antistoixei sto chromosome 0. an auto to chromosome exei px totalfitness = 10, tha emfanistei ston pinaka fitnessbounds 10 fores. ara einai pio
    //pithano na epilegei apo ena xrwmoswma pou exei totalfitness = 4
    private ArrayList<Integer> fitnessBounds;

    private HashMap<Integer,Lesson> allLessons;
    private HashMap<Integer,Teacher> allTeachers;
    private LinkedList<Gene> genes;

    public Genetic (HashMap<Integer,Lesson> allLessons, HashMap<Integer,Teacher> allTeachers, LinkedList<Gene> genesList) {
        this.population = new ArrayList<>();
        this.fitnessBounds = null;
        this.allLessons = allLessons;
        this.allTeachers = allTeachers;
        this.genes = genesList;
    }

    /**
     * @param populationSize: The size of the population in every step
     * @param mutationProbability: The propability a mutation might occur in a chromosome
     * @param minimumAllowedFitness: The minimum fitness value of the solution we wish to find
     * @param maximumSteps: The maximum number of steps we will search for a solution
     */

    public Chromosome start (int populationSize, double mutationProbability, int minimumAllowedFitness,
                       int maximumSteps) {

        System.out.println("\nStart...\n");

        //We initialize the population
        this.initializePopulation(populationSize);

        Random r = new Random();

        for(int step=0; step < maximumSteps; step++)
        {
            //Initialize the new generated population
            ArrayList<Chromosome> newPopulation = new ArrayList<>();

            for(int i=0; i < populationSize / 2; i++) {


                System.out.println("Step: " + step + " " + i);

                //We choose two chromosomes from the population
                //Due to how fitnessBounds ArrayList is generated, the propability of
                //selecting a specific chromosome depends on its fitness score
                int xIndex = this.fitnessBounds.get(r.nextInt(this.fitnessBounds.size()));
                Chromosome x = this.population.get(xIndex);
                int yIndex = this.fitnessBounds.get(r.nextInt(this.fitnessBounds.size()));
                while(yIndex == xIndex) //we don't want reproduction with the same parent. We need different
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
            for (int i = 0; i < populationSize/2; i++) {
                newPopulation.add(population.remove(0));
            }
            this.population = newPopulation;

            //TODO theloume na kratame kai tous kaluterous palious goneis.

            //We sort the population so the one with the minimum fitness is first
            this.population.sort(Collections.reverseOrder());
            System.out.println("Fitness: " + this.population.get(0).getFitness());
            //If the chromosome with the best fitness is acceptable we return it
            if(this.population.get(0).getFitness() >= minimumAllowedFitness)
            {
                System.out.println("Total calculated fitness of final state: " + this.population.get(0).getFitness());
                System.out.println("Finished after " + step + " steps...");
                this.population.get(0).printDetailedFitness();
                return this.population.get(0);
            }
            //We update the fitnessBounds arrayList
            this.updateFitnessBounds();
        }

        System.out.println("Finished after " + maximumSteps + " steps...");
        this.population.get(0).printDetailedFitness();
        return this.population.get(0); //first is the best, because we sorted the population list above.
    }

    //initialization for the population
    private void initializePopulation(int populationSize) {
        for (int i=0 ; i < populationSize ; i++) {
            this.population.add(new Chromosome(genes, this.allTeachers, this.allLessons));
        }
        this.updateFitnessBounds();
        this.population.sort(Collections.reverseOrder());
    }

    //Updates the arraylist that contains indexes of the chromosomes in the population ArrayList
    private void updateFitnessBounds () {
        this.fitnessBounds = new ArrayList<Integer>();
        for (int i=0; i<this.population.size(); i++)
        {
            for(int j=0; j<this.population.get(i).getFitness(); j++)
            {
                //Each chromosome index exists in the ArrayList as many times as its fitness score
                //By creating this ArrayList so, and choosing a random index from it,
                //the maximum the fitness score of a chromosome the greater chance it will be chosen.
                fitnessBounds.add(i);
            }
        }
    }


    private Offspring reproduce(Chromosome x, Chromosome y) {

        Random r = new Random();

        //for each reproduction we will split randomly on one and only dimension.

        int dimension = r.nextInt(4) + 1;

        Gene [][][][] childChromosomeA = new Gene [3][3][5][7];
        Gene [][][][] childChromosomeB = new Gene [3][3][5][7];

        //split on class
        if (dimension == 1) {
            int splitPoint = r.nextInt(3);

            //The 1st child has the left side of the x chromosome up to the intersection point...
            //The 2nd child has the left side of the y chromosome up to the intersection point...
            for (int c = 0; c < splitPoint; c++) {
                for (int s = 0; s < 3; s++) {
                    for (int d = 0; d < 5; d++) {
                        for (int h = 0; h < 7; h++) {
                            childChromosomeA[c][s][d][h] = x.getGenes()[c][s][d][h];
                            childChromosomeB[c][s][d][h] = y.getGenes()[c][s][d][h];
                        }
                    }
                }
            }

            //...and the right side of the y chromosome after the intersection point
            //...and the right side of the x chromosome after the intersection point
            for (int c = splitPoint; c < 3; c++) {
                for (int s = 0; s < 3; s++) {
                    for (int d = 0; d < 5; d++) {
                        for (int h = 0; h < 7; h++) {
                            childChromosomeA[c][s][d][h] = y.getGenes()[c][s][d][h];
                            childChromosomeB[c][s][d][h] = x.getGenes()[c][s][d][h];
                        }
                    }
                }
            }

        }

        //split on subclass
        else if (dimension == 2) {
            int splitPoint = r.nextInt(3);

            //The 1st child has the left side of the x chromosome up to the intersection point...
            //The 2nd child has the left side of the y chromosome up to the intersection point...
            for (int c = 0; c < 3; c++) {
                for (int s = 0; s < splitPoint; s++) {
                    for (int d = 0; d < 5; d++) {
                        for (int h = 0; h < 7; h++) {
                            childChromosomeA[c][s][d][h] = x.getGenes()[c][s][d][h];
                            childChromosomeB[c][s][d][h] = y.getGenes()[c][s][d][h];
                        }
                    }
                }
            }

            //...and the right side of the y chromosome after the intersection point
            //...and the right side of the x chromosome after the intersection point
            for (int c = 0; c < 3; c++) {
                for (int s = splitPoint; s < 3; s++) {
                    for (int d = 0; d < 5; d++) {
                        for (int h = 0; h < 7; h++) {
                            childChromosomeA[c][s][d][h] = y.getGenes()[c][s][d][h];
                            childChromosomeB[c][s][d][h] = x.getGenes()[c][s][d][h];
                        }
                    }
                }
            }
        }

        //split on day
        else if (dimension == 3) {
            int splitPoint = r.nextInt(5);

            //The 1st child has the left side of the x chromosome up to the intersection point...
            //The 2nd child has the left side of the y chromosome up to the intersection point...
            for (int c = 0; c < 3; c++) {
                for (int s = 0; s < 3; s++) {
                    for (int d = 0; d < splitPoint; d++) {
                        for (int h = 0; h < 7; h++) {
                            childChromosomeA[c][s][d][h] = x.getGenes()[c][s][d][h];
                            childChromosomeB[c][s][d][h] = y.getGenes()[c][s][d][h];
                        }
                    }
                }
            }

            //...and the right side of the y chromosome after the intersection point
            //...and the right side of the x chromosome after the intersection point
            for (int c = 0; c < 3; c++) {
                for (int s = 0; s < 3; s++) {
                    for (int d = splitPoint; d < 5; d++) {
                        for (int h = 0; h < 7; h++) {
                            childChromosomeA[c][s][d][h] = y.getGenes()[c][s][d][h];
                            childChromosomeB[c][s][d][h] = x.getGenes()[c][s][d][h];
                        }
                    }
                }
            }
        }

        //split on hour
        else {
            int splitPoint = r.nextInt(7);

            //The 1st child has the left side of the x chromosome up to the intersection point...
            //The 2nd child has the left side of the y chromosome up to the intersection point...
            for (int c = 0; c < 3; c++) {
                for (int s = 0; s < 3; s++) {
                    for (int d = 0; d < 5; d++) {
                        for (int h = 0; h < splitPoint; h++) {
                            childChromosomeA[c][s][d][h] = x.getGenes()[c][s][d][h];
                            childChromosomeB[c][s][d][h] = y.getGenes()[c][s][d][h];
                        }
                    }
                }
            }

            //...and the right side of the y chromosome after the intersection point
            //...and the right side of the x chromosome after the intersection point
            for (int c = 0; c < 3; c++) {
                for (int s = 0; s < 3; s++) {
                    for (int d = 0; d < 5; d++) {
                        for (int h = splitPoint; h < 7; h++) {
                            childChromosomeA[c][s][d][h] = y.getGenes()[c][s][d][h];
                            childChromosomeB[c][s][d][h] = x.getGenes()[c][s][d][h];
                        }
                    }
                }
            }
        }

        return new Offspring(
                new Chromosome(childChromosomeA, genes,
                                allTeachers, allLessons),
                new Chromosome(childChromosomeB, genes,
                                allTeachers, allLessons));
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