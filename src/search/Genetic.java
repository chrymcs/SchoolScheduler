package search;

import myObjects.Lesson;
import myObjects.Teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Genetic {

    private ArrayList<Chromosome> population;
    private HashMap<Integer,Lesson> allLessons;
    private HashMap<Integer,Teacher> allTeachers;

    public Genetic(HashMap<Integer,Lesson> allLessons, HashMap<Integer,Teacher> allTeachers) {
        this.allLessons = allLessons;
        this.allTeachers = allTeachers;
    }

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
