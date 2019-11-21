package search;

//import com.sun.source.tree.ArrayAccessTree;
import myObjects.Lesson;
import myObjects.Teacher;

import java.util.*;

/**
 * Class that represents a Schedule - Chromosome
 * Each Chromosome has 7*45 genes.
 */
public class Chromosome {


    private Gene[][][][] chromosome; // hoursPerDay - daysPerWeek - subClassesPerSchedule
    private int maxClasses = 3, maxDay = 5, maxHour = 7, maxSubClasses = 3;
    private int fitness, eligibleFitness;
    private int totalLessonHoursNeeded;
    private int [] hoursNeededPerClass = new int[3];
    private HashMap<Integer,Teacher> allTeachers;
    private HashMap<Integer,Lesson> allLessons;
    private HashMap<Teacher,Integer> assignedTeachers = new HashMap<>();
    private HashMap<Lesson,Integer> assignedLessons = new HashMap<>();

    public Chromosome (Gene[][][][] childChromosome) {
        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay; d++) {
                    for (int h = 0; h < maxHour; h++) {
                       this.chromosome[c][s][d][h] = childChromosome[c][s][d][h];
                    }
                }
            }
        }
    }

    public Chromosome(LinkedList<LinkedList<Gene>> genesList,
                      HashMap<Integer,Teacher> teacherHashMap,
                      HashMap<Integer,Lesson> lessonHashMap
                      //int totalLessonHoursNeeded, int[] hoursNeededPerClass
                        ) {
        allTeachers = teacherHashMap;
        allLessons = lessonHashMap;
        //this.totalLessonHoursNeeded = totalLessonHoursNeeded;
        //this.hoursNeededPerClass = hoursNeededPerClass;
        Gene gene;
        this.chromosome = new Gene[3][3][5][7];
        Random r = new Random();
        int upperRandomLimit;

        //A list that will hold 5 hashmaps of teachers and their daily hours
        LinkedList<HashMap<Teacher, Integer>> teachersDayHours = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            teachersDayHours.add(new HashMap<>());
        }

        int teacher1DayHours;

        for (int c = 0; c < maxClasses; c++) {
            upperRandomLimit = genesList.get(c).size();
            for (int s = 0; s < maxSubClasses; s++) { //foreach subClass
                for (int d = 0; d < maxDay; d++) { //foreach day
                //HashMap<Teacher, Integer> teachersDayHours1Class = new HashMap<>();
                    for (int h = 0; h < maxHour  ; h++) { //foreach hour
                        gene = genesList.get(c).get(r.nextInt(upperRandomLimit));
                        this.chromosome[c][s][d][h] = gene;

                        //decreasing week hours of each teacher when assigned
                        Teacher teacher = gene.getTeacher();
                        int teachersWeekHours = teacherHashMap.get(teacher.getId()).getWeekHours();
                        teacherHashMap.get(teacher.getId()).setWeekHours(teachersWeekHours - 1);

                        //keeping for each teacher that occurs the number of hours that taught
                        // in that day in 1 subclass
                        //int temp = teachersDayHours.get(d).getOrDefault(teacher, 0);
                        //teachersDayHours1Class.put(teacher, teacherDayHours + 1);

                        //In each assignment og lesson-teacher combination the program keeps a
                        //record of the assigned teachers and lessons in order to be handled
                        //later on and during constraint #5
                        // TODO: may change comment
                        assignedLessons = updateAssignedLessons(assignedLessons,
                                gene.getLesson());
                        assignedTeachers = updateAssignedTeachers(assignedTeachers,
                                gene.getTeacher());
                    }

                    //at the end of that day we sum up all hours for each teacher
                    //for (Teacher teacher : teachersDayHours1Class.keySet()) {
                     //   teachersDayHoursAllClasses.get(d).put(teacher,
                     //           teachersDayHours1Class.get(teacher));
                    }
                }
            }


        //After the assignment of Genes all teachers (from hypothesis) are updated, regarding the
        // day hours that they taught. Negative values indicate insufficiency in the schedule.
//        for (int teacherId : allTeachers.keySet()) {
//            int maxDayHours = 0;
//            Teacher teacher = allTeachers.get(teacherId);
//            for (int d = 0; d < 5; d++) {
//                if (maxDayHours
//                        < teachersDayHoursAllClasses.get(d).getOrDefault(teacher,0)) {
//                    maxDayHours = teachersDayHoursAllClasses.get(d).get(teacher);
//                }
//            }
//            int dayHoursBeforeGenes = allTeachers.get(teacherId).getDayHours();
//            allTeachers.get(teacherId).setDayHours(dayHoursBeforeGenes - maxDayHours);
//        }
        calculateFitness();
    }


    private void calculateFitness() {
        int subClassesScore = calculateGapsScore();
        int unevenHoursScore = calculateUnevenHoursScore();
        int consecutiveTeachersScore = calculateConsecutiveTeachersScore();
        int teachersEvenHours = calculateTeachersEvenHours(assignedTeachers, allTeachers);


        //int teachersScore = calculateTeachersScore();
        //int lessonsScore = calculateLessonsScore();
        fitness = subClassesScore;
    }

    //Constraint #1
    private int calculateGapsScore() {
        int gapsCounter = 0;
        float gapsScore; //total gap hours
        int teachingHoursCounter = 0; //total teaching hours
        int totalDaysWithLessons = 0;
        int totalTeachingHoursCounter = 0;
        boolean endOfDayFound;
        int maxEvenHoursScore = 42 * (3);
        int evenHoursScore;
        int [][][] subClassesHours = new int [maxClasses][maxSubClasses][maxDay];
        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay; d++) {
                    //int hoursCounter = 0;
                    endOfDayFound = false;
                    for (int h = maxHour-1; h >= 0; h--) {
                        if (chromosome[c][s][d][h].getLesson().getId() > 0) { //px kostas math
                            //hoursCounter++;
                            teachingHoursCounter++;
                            if (!endOfDayFound) endOfDayFound = true;
                        } else if (chromosome[c][s][d][h].getLesson().getId() < 0 && endOfDayFound) {
                            gapsCounter++;
                            teachingHoursCounter++;
                        }
                    }
                }
            }
                    //subClassesHours[c][s][d] = hoursCounter;
        }
        gapsScore = (float) (teachingHoursCounter - gapsCounter)/teachingHoursCounter;
        gapsScore = Math.round(gapsScore * 100);
        //evenHoursScore = Math.round ((float) (calcSubClassesEvenHours(subClassesHours,
                                                     //                           maxEvenHoursScore)
                                                   //     / maxEvenHoursScore) * 100);
        return (int) gapsScore; //+ evenHoursScore;
    }

    //Constraint #2
    public int calculateConsecutiveTeachersScore() {
        int consecutiveHours = 0;
        int consecutiveScore = 100;

        int evenHoursScore = 0;
        int firstTeacher, middleTeacher, lastTeacher;
        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay; d++) {
                    for (int h = 2; h < maxHour ; h++) { //starts from the 3rd hour on
                        lastTeacher = chromosome[c][s][d][h].getTeacher().getId();
                        middleTeacher = chromosome[c][s][d][h - 1].getTeacher().getId();
                        firstTeacher = chromosome[c][s][d][h - 2].getTeacher().getId();

                        System.out.println("same teacher more than 2 hours");
                        System.out.println("Found in class: " + c + " , Subclass: " + s +
                                " and Day: " + d + ". Teacher Name: " +
                                chromosome[c][s][d][h - 2].getTeacher().getName());

                        consecutiveHours = consecutiveHours +
                                compareTeachersId (firstTeacher, middleTeacher, lastTeacher);
                    }
                }
            }
        }
        //In case too many teachers have 3 consecutive hours of teaching then the program maxes
        // the consecutive hours to 100, so this category's fitness is 0.
        if (consecutiveHours > 100) consecutiveHours = 100;
        consecutiveScore = consecutiveScore - consecutiveHours;

        //evenHoursScore = calcTeachersEvenHoursPerLesson(assignedLessons, assignedTeachers);
        return consecutiveScore;
    }

    //Secondary method for Constraint #2
    private int compareTeachersId (int teacher_A, int teacher_B, int teacher_C) {
        if (teacher_A == teacher_B && teacher_B == teacher_C && teacher_C != -1)  {
            return 1;
        }
        return 0;
    }

    //Constraint #3
    private int calculateUnevenHoursScore() {
        //total teaching hours
        int teachingHoursPerDay;

        //Worst case scenario
        int maxEvenHoursScore = 42 * 9;
        int evenHoursScore;
        int [][][] subClassesHours = new int [maxClasses][maxSubClasses][maxDay];
        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay; d++) {
                    teachingHoursPerDay = 0;
                    for (int h = 0; h < maxHour; h++) {
                        if (chromosome[c][s][d][h].getLesson().getId() > 0) {
                            teachingHoursPerDay++;
                        }
                    }
                subClassesHours[c][s][d] = teachingHoursPerDay;
                }
            }
        }

        evenHoursScore = calcSubClassesEvenHours(subClassesHours,maxEvenHoursScore);
        return evenHoursScore;
    }

    //Secondary method for Constraint #2
    private int calcSubClassesEvenHours (int[][][] subClassesHours, int maxScore) {
        float temp;
        int evenHoursScore = 0;

        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay - 1; d++) {
                    for (int nextDay = d+1; nextDay < maxDay; nextDay++) {
                        evenHoursScore = evenHoursScore +
                                Math.abs(subClassesHours[c][s][d] - subClassesHours[c][s][nextDay]);
                    }
                }
            }
        }
        //TODO: ksereis
        temp = (float) (maxScore - evenHoursScore) / maxScore;
        temp = Math.round(temp * 100);
        return (int) temp;
    }


    //Constraint #5
    private int calculateTeachersEvenHours (HashMap<Teacher,Integer> assignedTeachers,
                                           HashMap<Integer,Teacher> allTeachers) {
        Teacher teacherA;
        Teacher teacherB;
        float difference = 0;
        LinkedList<Integer> checkedIds = new LinkedList<>();
        for (int teacherIdA : allTeachers.keySet()) {
            checkedIds.add(teacherIdA);
            for (int teacherIdB : allTeachers.keySet()) {
                if (teacherIdA != teacherIdB && !checkedIds.contains(teacherIdB)) {
                    teacherA = allTeachers.get(teacherIdA);
                    teacherB = allTeachers.get(teacherIdB);
                    difference = difference +
                            Math.abs(assignedTeachers.getOrDefault(teacherA,0)
                                    - assignedTeachers.getOrDefault(teacherB,0));
                }
            }
        }
        //TODO: check formula
        difference = difference / 100;
        return Math.round(100-difference);
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
        if (teacher.getId()>0) {
            if (!assignedTeachers.containsKey(teacher)) {
                assignedTeachers.put(teacher,1);
            } else {
                assignedTeachers.put(teacher, assignedTeachers.get(teacher) + 1);
            }
        }
        return assignedTeachers;
    }


    //public Chromosome mutate() { return new Chromosome(); }

    public Gene[][][][] getGenes () {
        return this.chromosome;
    }

    //TODO: is needed?
    public void setChromosome (Gene[][][][] genesList) {
        this.chromosome = genesList;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String str = "";
        for (int c = 0; c < maxClasses; c++) {
            stringBuilder.append("Class: ");
            stringBuilder.append(c+1);
            stringBuilder.append("\n");
            for (int s = 0; s < maxSubClasses; s++) {
                stringBuilder.append("Subclass: ");
                stringBuilder.append(s+1);
                stringBuilder.append("\n");
                for (int h = 0; h < maxHour ; h++) {
                    for (int d = 0; d < maxDay; d++) {
                        stringBuilder.append(chromosome[c][s][d][h].getLesson().getTitle());
                        stringBuilder.append(" @ ");
                        stringBuilder.append(chromosome[c][s][d][h].getTeacher().getName());
                        stringBuilder.append(",");
                    }
                    stringBuilder.append("\n");
                }
                stringBuilder.append("\n");
            }
            stringBuilder.append("\n");
        }
        str = stringBuilder.toString();
        return str;
    }
}

