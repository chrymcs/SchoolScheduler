package search;

import java.util.ArrayList;

public class Genetic {

    private ArrayList<Chromosome> population;

    public Genetic() { }

    /**
     * @param populationSize: The size of the population in every step
     * @param mutationProbability: The propability a mutation might occur in a chromosome
     * @param minimumFitness: The minimum fitness value of the solution we wish to find
     * @param maximumSteps: The maximum number of steps we will search for a solution
     */
    public Chromosome start(int populationSize, double mutationProbability, int minimumFitness, int maximumSteps) {
        return new Chromosome();
    }

    //initialization for the population
    private void initializePopulation(int populationSize) {

    }

    private void reproduce(Chromosome x, Chromosome y) {
        // TODO It needs to return two children.
    }
}
