package search;

import myObjects.Lesson;
import myObjects.Teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * Class that represents a Schedule - Chromosome
 * Each gene in the Chromosome is a Combination of
 * lesson and teacher who is eligible to teach it
 */
public class Chromosome {

    private Gene[][][][] genes; // hoursPerDay - daysPerWeek - subClassesPerSchedule
    private int maxClasses, classA, classB, classC;
    private int fitness, maxSubClasses, maxDay, maxHour;

    /**
     * Constructor for the preparation of the initial Chromosomes.
     * In order for the Chromosome to be prepared, we need to provide,
     * all the valid combinations that were already created, by the
     * given arguments (txt files).
     * */
    public Chromosome(LinkedList<LinkedList<Gene>> genesList, int maxD, int maxH,
                      int classA, int classB, int classC) {
        this.classA = classA;
        this.classB = classB;
        this.classC = classC;
        //get the max number of subClass of classes A,B,C
        maxSubClasses = Math.max(Math.max(classA,classB), classC);
        this.maxDay = maxD;
        this.maxHour = maxH;
        this.genes = new Gene[3][maxSubClasses][maxDay][maxHour];
        Random r = new Random();
        int upperRandomLimit;
        for (int c = 0; c < 2; c++) {
            upperRandomLimit = genesList.get(c).size();
            for (int s = 0; s < maxSubClasses; s++) { //foreach subClass
                for (int d = 0; d < maxDay; d++) { //foreach day
                    for (int h = 0; h < maxHour  ; h++) { //foreach hour
                        this.genes[c][s][d][h] = genesList.get(c).get(r.nextInt(upperRandomLimit));
                    }
                }
            }
        calculateFitness();
        }
    }

    private void calculateFitness() {
        int subClassesScore = calculateSubClassesScore();
        int teachersScore = calculateTeachersScore();
        int lessonsScore = calculateLessonsScore();
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
    private int calculateSubClassesScore () {
        int gapScore = 0; //changes only towards negative values
        int evenHoursScore = 0;
        int [][][] subClassesHours = new int [maxClasses][maxSubClasses][maxDay];
        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay; d++) {
                    int hoursCounter = 0;
                    boolean endOfDayFound = false;
                    for (int h = maxHour-1; h > 0; h--) {
                        if (genes[c][s][d][h] != null) {
                            hoursCounter++;
                            if (!endOfDayFound) endOfDayFound = true;
                        } else if (genes[c][s][d][h] == null && endOfDayFound)
                            gapScore--;
                    }
                    subClassesHours[c][s][d] = hoursCounter;
                }
            }
        }
        evenHoursScore = calcSubClassesEvenHours(subClassesHours);
        return gapScore + evenHoursScore;
    }

    private int calculateTeachersScore() {
        int consecutiveHoursScore = 0;
        int evenHoursScore = 0;
        int teacherId, lessonId;
        LinkedList<Lesson> assignedLessons = new LinkedList<>();
        HashMap<Integer,Integer> assignedTeachers = new HashMap<>();
        //TODO: γέμισμα assignedTeachers (hashmap) με τη λογική
        // (teacherId - πόσες φορές τον βρήκα στη loopα)
        int firstTeacher, middleTeacher, lastTeacher;
        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay; d++) {
                    if (genes[c][s][d][0] !=null
                            && !assignedLessons.contains(genes[c][s][d][0].getLesson()))
                        assignedLessons.add(genes[c][s][d][0].getLesson());
                    if (genes[c][s][d][1] != null
                            && !assignedLessons.contains(genes[c][s][d][1].getLesson()))
                        assignedLessons.add(genes[c][s][d][1].getLesson());
                    for (int h = 2; h < maxHour ; h++) { //starts from the 3rd hour on
                        if (genes[c][s][d][h] != null) {
                            lastTeacher = genes[c][s][d][h].getTeacher().getId();
                            if (!assignedLessons.contains(genes[c][s][d][h].getLesson()))
                                assignedLessons.add(genes[c][s][d][h].getLesson());
                        } else lastTeacher = -1;
                        if (genes[c][s][d][h - 1] != null) {
                            middleTeacher = genes[c][s][d][h - 1].getTeacher().getId();
                        } else middleTeacher = -1;
                        if (genes[c][s][d][h - 2] != null) {
                            firstTeacher = genes[c][s][d][h - 2].getTeacher().getId();
                        } else firstTeacher = -1;
                        consecutiveHoursScore = consecutiveHoursScore
                                + compareTeachersId (firstTeacher, middleTeacher, lastTeacher);
                    }
                }
            }
        }
        evenHoursScore = calcTeachersEvenHours(assignedLessons, assignedTeachers);
        return consecutiveHoursScore + evenHoursScore;
    }

    private int calculateLessonsScore() {
        return 0;
    }

    /**
     * Calculates the difference of the teaching hours between the days of a subClass
     * comparing each day with the others. If all days are even result is 0.
     * @return evenHoursScore represents the negative score for each difference
     *        that occurs between the maximum teaching hours of each day.
     */
    private int calcSubClassesEvenHours (int[][][] subClassesHours) {
        int evenHoursScore = 0;
        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay-1; d++) {
                    for (int nextDay = d+1; nextDay < maxDay; nextDay++) {
                        evenHoursScore = evenHoursScore +
                                Math.abs(subClassesHours[c][s][d] - subClassesHours[c][s][nextDay]);
                    }
                }
            }
        }
        return evenHoursScore;
    }

    /**
     *  Calculates the score for the teachers that could teach the same subject.
     * If a lesson can be taught only by 1 teacher it skips the calculation as it
     * did not have other option.
     * @param assignedLessons lessons that were actually assigned into the
     *                       schedule - chromosome
     */
    private int calcTeachersEvenHours (LinkedList<Lesson> assignedLessons,
                                       HashMap<Integer,Integer> assignedTeachers) {
        int teachersEvenHours = 0;
        int teacherAHours = -1;
        int teacherBHours = -1;
        for (Lesson al : assignedLessons) {
            if (al.getAvailableTeachers().size() > 1) {
                for (int teacherId: al.getAvailableTeachers()) {
                    teacherAHours = teacherBHours;
                    teacherBHours = assignedTeachers.get(teacherId);
                }
                teachersEvenHours =+ compareTeachersHours(teacherAHours,teacherBHours);
            }
        }
        return teachersEvenHours;
    }

    private int compareTeachersId (int teacher_A, int teacher_B, int teacher_C) {
        if (teacher_A == teacher_B &&
                teacher_B == teacher_C &&
                    teacher_C != -1) {
            return -1;
        }
        return 0;
    }

    private int compareTeachersHours (int teacher_A, int teacher_B) {
        if (teacher_A == teacher_B &&
                teacher_B != -1) {
            return -1;
        }
        return 0;
    }

    public double fitness() { return 0.0; }

    //public Chromosome mutate() { return new Chromosome(); }

    public Gene[][][][] getGenes() {
        return genes;
    }

    public void setGenes(Gene[][][][] genesList) {
        this.genes = genesList;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}
