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
        int gapScore = calculateSubClassesScore();

    }

    /**
     * Calculates the score for each Chromosome, regarding the constrains bound to
     * subClasses.
     * <b>gapScore</b> keeps the negative value for every null 'empty' hour between teaching hours.
     * When an empty hour is present at the beginning of a day (for each subClass)
     * we also consider it to be a gap.
     * For every subclass, every day, the method searches for the last teaching hour.
     * Moving back towards the first hour it detects the occuring gaps.
     * <b>evenHoursScore</b> keeps the negative value for every day that has different
     * number of teaching hours from the others
     * (example for Monday to Friday hours: "5 - 5 - 5 - 4 - 4" will result in minus 6)
     * @return represents the negative score for gapScore and evenHoursScore.
     */
    private int calculateSubClassesScore() {
        int gapScore = 0; //changes only towards negative values
        int evenHoursScore = 0;
        int [] subClassesHours = null;
        for (int subClass = 0; subClass < maxSubClass; subClass++) {
            subClassesHours = new int [maxDay];
            for (int day = 0; day < maxDay; day++) {
                int hoursCounter = 0;
                boolean endOfDayFound = false;
                for (int hour = maxHour-1; hour > 0; hour--) {
                    if (genes[subClass][day][hour] != null) {
                        hoursCounter++;
                        if (!endOfDayFound) endOfDayFound = true;
                    } else if (genes[subClass][day][hour] == null && endOfDayFound)
                        gapScore--;
                }
                subClassesHours[day] = hoursCounter;
            }
        }
        evenHoursScore = calculateSubClassesEvenHours(subClassesHours);
        return gapScore + evenHoursScore;
    }

    /**
     * Calculates the difference of the teaching hours between the days of a subClass
     * comparing each day with the others. If all days are even result is 0.
     * @return evenHoursScore represents the negative score for each difference
     * that occurs between the maximum teaching hours of each day.
     */
    private int calculateSubClassesEvenHours(int[] subClassesHours) {
        int evenHoursScore = 0;
        for (int day = 0; day < subClassesHours.length-1; day++) {
            for (int nextDay = day+1; nextDay < subClassesHours.length; nextDay++) {
                evenHoursScore = evenHoursScore + Math.abs(day - nextDay);
            }
        }
        return evenHoursScore;
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
