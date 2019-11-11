package search;

import java.util.LinkedList;
import java.util.Random;

/**
 * Class that represents a Schedule - Chromosome
 * Each gene in the Chromosome is a Combination of
 * lesson and teacher who is eligible to teach it
 */
public class Chromosome {

    private Gene[][][] genes; // hoursPerDay - daysPerWeek - subClassesPerSchedule
    int maxSubClass, maxDay, maxHour;
    private int fitness;

    /**
     * Constructor for the preparation of the initial Chromosomes.
     * In order for the Chromosome to be prepared, we need to provide,
     * all the valid combinations that were already created, by the
     * given arguments (txt files).
     * */
    public Chromosome(LinkedList<Gene> genes, int maxSubClass,
                      int maxDay, int maxHour) {
        this.maxSubClass = maxSubClass;
        this.maxDay = maxDay;
        this.maxHour = maxHour;
        this.genes = new Gene[maxSubClass][maxDay][maxHour];
        Random r = new Random();
        int upperRandomLimit = genes.size();
        for (int subClass = 0; subClass < maxSubClass; subClass++) { //foreach subClass
            for (int day = 0; day < maxDay; day++) { //foreach day
                for (int hour = 0; hour < maxHour  ; hour++) { //foreach hour
                    this.genes[subClass][day][hour] = genes.get(r.nextInt(upperRandomLimit));
                }
            }
        }
        calculateFitness();
    }

    private void calculateFitness() {
        int gapScore = calculateSubClassesGaps();

    }

    /**
     * Calculates the score for each Chromosome, regarding the gaps between lessons.
     * As a 'gap' we define every null 'empty' hour between teaching hours.
     * When an empty hour is present at the beginning of a day (for each subClass)
     * we also consider it to be a gap.
     * For every subclass, every day, the method searches for the last teaching hour.
     * Moving back towards the first hour it detects the occuring gaps.
     * @return gapScore represents the negative score for each gap that occurs (counter).
     */
    private int calculateSubClassesGaps() {
        int gapScore = 0; //changes only towards negative values
        for (int subClass = 0; subClass < maxSubClass; subClass++) {
            for (int day = 0; day < maxDay; day++) {
                //int lastTeachingHour;
                boolean endOfDayFound = false;
                for (int hour = maxHour-1; hour > 0; hour--) {
                    if (genes[subClass][day][hour] != null && !endOfDayFound) {
                        //lastTeachingHour = hour;
                        endOfDayFound = true;
                    } else if (genes[subClass][day][hour] == null && endOfDayFound)
                        gapScore--;
                    }
                }
            }
        return gapScore;
    }

    public double fitness() { return 0.0; }

    //public Chromosome mutate() { return new Chromosome(); }

    public Gene[][][] getGenes() {
        return genes;
    }

    public void setGenes(Gene[][][] genes) {
        this.genes = genes;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}
