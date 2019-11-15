package search;

import myObjects.Lesson;
import myObjects.Teacher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Class that represents a Schedule - Chromosome
 * Each gene in the Chromosome is a Combination of
 * lesson and teacher who is eligible to teach it
 */
public class Chromosome {

    private Gene[][][][] genes; // hoursPerDay - daysPerWeek - subClassesPerSchedule
    private int maxClasses = 3, maxDay = 5, maxHour = 7;
    private int classA, classB, classC, maxSubClasses;
    private int fitness, eligibleFitness;
    private int totalLessonHoursNeeded;
    private int [] hoursNeededPerClass = new int[3];
    private HashMap<Integer,Teacher> allTeachers;
    private HashMap<Integer,Lesson> allLessons;
    private HashMap<Teacher,Integer> assignedTeachers = new HashMap<>();
    private HashMap<Lesson,Integer> assignedLessons = new HashMap<>();
    /**
     * Constructor for the preparation of the initial Chromosomes.
     * In order for the Chromosome to be prepared, we need to provide,
     * all the valid combinations that were already created, by the
     * given arguments (txt files).
     * */
    public Chromosome(LinkedList<LinkedList<Gene>> genesList,
                      HashMap<Integer,Teacher> teacherHashMap,
                      HashMap<Integer,Lesson> lessonHashMap,
                      int classA, int classB, int classC,
                      int totalLessonHoursNeeded, int[] hoursNeededPerClass) {
        allTeachers = teacherHashMap;
        allLessons = lessonHashMap;
        this.totalLessonHoursNeeded = totalLessonHoursNeeded;
        this.hoursNeededPerClass = hoursNeededPerClass;
        this.classA = classA;
        this.classB = classB;
        this.classC = classC;
        Gene gene;
        //get the max number of subClass of classes A,B,C
        maxSubClasses = Math.max(Math.max(classA,classB), classC);
        this.genes = new Gene[3][maxSubClasses][maxDay][maxHour];
        Random r = new Random();
        int upperRandomLimit;

        //A list that will hold 5 hashmaps of teachers and their daily hours
        LinkedList<HashMap<Teacher, Integer>> teachersDayHoursAllClasses =
                new LinkedList<HashMap<Teacher, Integer>>();
        for (int i = 0; i < 5; i++) {
            teachersDayHoursAllClasses.add(new HashMap<>());
        }

        for (int c = 0; c <= 2; c++) {
            upperRandomLimit = genesList.get(c).size();
            for (int s = 0; s < maxSubClasses; s++) { //foreach subClass
                for (int d = 0; d < maxDay; d++) { //foreach day
                HashMap<Teacher, Integer> teachersDayHours1Class = new HashMap<>();
                    for (int h = 0; h < maxHour  ; h++) { //foreach hour
                        gene = genesList.get(c).get(r.nextInt(upperRandomLimit));
                        this.genes[c][s][d][h] = gene;

                        //decreasing week hours of each teacher when assigned
                        Teacher teacher = gene.getTeacher();
                        int teachersWeekHours = allTeachers.get(teacher.getId()).getWeekHours();
                        allTeachers.get(teacher.getId()).setWeekHours(teachersWeekHours - 1);

                        //keeping for each teacher that occurs the number of hours that teached
                        // in that day in 1 subclass
                        int currentHoursValue = teachersDayHours1Class.getOrDefault(teacher, 0);
                        teachersDayHours1Class.put(teacher, currentHoursValue + 1);

//                        assignedTeachers = updateAssignedTeachers(assignedTeachers,
//                                gene.getTeacher());
//                        assignedLessons = updateAssignedLessons(assignedLessons,
//                                gene.getLesson());
                    }
                    //at the end of that day we sum up all hours for each teacher
                    for (Teacher teacher : teachersDayHours1Class.keySet()) {
                        teachersDayHoursAllClasses.get(d).put(teacher,
                                teachersDayHours1Class.get(teacher));
                    }
                }
            }
        }

        //After the assignment of Genes all teachers (from hypothesis) are updated, regarding the
        // day hours that they taught. Negative values indicate insufficiency in the schedule.
        for (int teacherId : allTeachers.keySet()) {
            int maxDayHours = 0;
            Teacher teacher = allTeachers.get(teacherId);
            for (int d = 0; d < 5; d++) {
                if (maxDayHours
                        < teachersDayHoursAllClasses.get(d).getOrDefault(teacher,0)) {
                    maxDayHours = teachersDayHoursAllClasses.get(d).get(teacher);
                }
            }
            int dayHoursBeforeGenes = allTeachers.get(teacherId).getDayHours();
            allTeachers.get(teacherId).setDayHours(dayHoursBeforeGenes - maxDayHours);
        }

        calculateFitness();
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
        int gapsCounter = 0;
        int gapsScore; //maximum = 100
        int maxEvenHoursScore = 42 * (classA + classB + classC);
        int evenHoursScore;
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
                        } else if (genes[c][s][d][h] == null && endOfDayFound) {
                            gapsCounter++;
                        }
                    }
                    subClassesHours[c][s][d] = hoursCounter;
                }
            }
        }
        //TODO: check formula
        gapsScore = Math.round ((float) ((totalLessonHoursNeeded - gapsCounter)
                                                        / totalLessonHoursNeeded) * 100);
        evenHoursScore = Math.round ((float) (calcSubClassesEvenHours(subClassesHours,
                                                                                maxEvenHoursScore)
                                                        / maxEvenHoursScore) * 100);
        return gapsScore + evenHoursScore;
    }

    private int calculateTeachersScore() {
        int consecutiveHoursScore = 0;
        int evenHoursScore = 0;
        int teacherId, lessonId;

        int firstTeacher, middleTeacher, lastTeacher;
        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay; d++) {
                    for (int h = 2; h < maxHour ; h++) { //starts from the 3rd hour on
                        if (genes[c][s][d][h] != null) {
                            lastTeacher = genes[c][s][d][h].getTeacher().getId();
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
        evenHoursScore = calcTeachersEvenHoursPerLesson(assignedLessons, assignedTeachers);
        return consecutiveHoursScore + evenHoursScore;
    }

    private int calculateLessonsScore() {
        return 0;
    }

    private HashMap<Lesson,Integer> updateAssignedLessons (HashMap<Lesson,Integer> assignedLessons,
                                                           Lesson lesson) {
        if (!assignedLessons.containsKey(lesson)) {
            assignedLessons.put(lesson,1);
        } else {
            assignedLessons.put(lesson, assignedLessons.get(lesson) + 1);
        }
        return assignedLessons;
    }

    private HashMap<Teacher,Integer> updateAssignedTeachers (HashMap<Teacher,Integer> assignedTeachers,
                                                           Teacher teacher) {
        if (!assignedTeachers.containsKey(teacher)) {
            assignedTeachers.put(teacher,1);
        } else {
            assignedTeachers.put(teacher, assignedTeachers.get(teacher) + 1);
        }
        return assignedTeachers;
    }



    /**
     * Calculates the difference of the teaching hours between the days of a subClass
     * comparing each day with the others. If all days are even result is 0.
     * @return evenHoursScore represents the negative score for each difference
     *        that occurs between the maximum teaching hours of each day.
     */
    private int calcSubClassesEvenHours (int[][][] subClassesHours, int maxEvenHoursScore) {
        int temp = maxEvenHoursScore;
        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay-1; d++) {
                    for (int nextDay = d+1; nextDay < maxDay; nextDay++) {
                        temp = temp -
                                Math.abs(subClassesHours[c][s][d] - subClassesHours[c][s][nextDay]);
                    }
                }
            }
        }
        return temp;
    }

    /**
     *  Calculates the score for the teachers that could teach the same subject.
     * If a lesson can be taught only by 1 teacher it skips the calculation as it
     * did not have other option.
     * @param assignedLessons lessons that were actually assigned into the
     *                       schedule - chromosome
     * @param assignedTeachers teachers that were actually assigned into the
     *                         scheduler -chromosome
     */
    private int calcTeachersEvenHoursPerLesson(HashMap<Lesson,Integer> assignedLessons,
                                               HashMap<Teacher,Integer> assignedTeachers) {
        int teachersEvenHours = 0;
        int teacherAHours = -1;
        int teacherBHours = -1;
        int teacherId;
        for (Lesson al : assignedLessons.keySet()) {
            if (al.getAvailableTeachers().size() == 1) {

            } else {
                for (teacherId:
                     al.getAvailableTeachers()) {
                    assignedTeacherMoreThatTwo = updateAssignedTeachers(assignedTeacherMoreThatTwo,
                        teacherId);
            }
            }
                teachersEvenHours =+ compareTeachersHours(teacherAHours,teacherBHours);
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

    private int compareAssignedTeachers (HashMap<Integer,Integer> assignedTeachersStrict,
                                         HashMap<Integer,Integer> assignedTeachersInMoreThanTwo) {
        int teacherA, teacherB;
        int counterStrict, counterAll;
        int [] assignedTeachersToCheck = new int [assignedTeachersInMoreThanTwo.size()];
        int index = 0;
        for (int teacherId: assignedTeachersInMoreThanTwo.keySet()) {
            if (assignedTeachersStrict.containsKey(teacherId)) {
                counterStrict = assignedTeachersStrict.get(teacherId);
                counterAll = assignedTeachersInMoreThanTwo.get(teacherId);
                assignedTeachersInMoreThanTwo.put(teacherId, counterStrict+counterAll);
            }
            assignedTeachersToCheck [index++] =
                    assignedTeachersInMoreThanTwo.get(teacherId);
        }
        for (int i = 1; i < index ; i++) {
            compareTeachersHours(index[])
        }
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
