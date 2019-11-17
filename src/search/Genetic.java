
package search;

import myObjects.Lesson;
import myObjects.Teacher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Genetic {

    private Gene[][] population;
    private HashMap<Integer,Lesson> allLessons;
    private HashMap<Integer,Teacher> allTeachers;

    public Genetic(HashMap<Integer,Lesson> allLessons, HashMap<Integer,Teacher> allTeachers) {
        this.allLessons = allLessons;
        this.allTeachers = allTeachers;
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

    public Chromosome start(int populationSize, double mutationProbability, int minimumFitness, int maximumSteps) {
        return new Chromosome(population);
    }

    //initialization for the population
    private void initializePopulation(int populationSize) {

    }

    private void reproduce(Chromosome x, Chromosome y) {
        // TODO It needs to return two children.
    }
}