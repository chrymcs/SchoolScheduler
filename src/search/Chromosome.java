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
        //LinkedList<HashMap<Teacher, Integer>> teachersDayHoursAllClasses =
                //new LinkedList<HashMap<Teacher, Integer>>();
        //for (int i = 0; i < 5; i++) {
           // teachersDayHoursAllClasses.add(new HashMap<>());
        //}

        for (int c = 0; c < maxClasses; c++) {
            upperRandomLimit = genesList.get(c).size();
            for (int s = 0; s < maxSubClasses; s++) { //foreach subClass
                for (int d = 0; d < maxDay; d++) { //foreach day
                //HashMap<Teacher, Integer> teachersDayHours1Class = new HashMap<>();
                    for (int h = 0; h < maxHour  ; h++) { //foreach hour
                        gene = genesList.get(c).get(r.nextInt(upperRandomLimit));
                        this.chromosome[c][s][d][h] = gene;

                        //decreasing week hours of each teacher when assigned
                        //Teacher teacher = gene.getTeacher();
                        //int teachersWeekHours = allTeachers.get(teacher.getId()).getWeekHours();
                        //allTeachers.get(teacher.getId()).setWeekHours(teachersWeekHours - 1);

                        //keeping for each teacher that occurs the number of hours that teached
                        // in that day in 1 subclass
                        //int currentHoursValue = teachersDayHours1Class.getOrDefault(teacher, 0);
                        //teachersDayHours1Class.put(teacher, currentHoursValue + 1);

//                        assignedTeachers = updateAssignedTeachers(assignedTeachers,
//                                gene.getTeacher());
//                        assignedLessons = updateAssignedLessons(assignedLessons,
//                                gene.getLesson());
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
        int subClassesScore = calculateSubClassesScore();
        //int teachersScore = calculateTeachersScore();
        //int lessonsScore = calculateLessonsScore();
        fitness = subClassesScore;
    }



    /**
     *
     * @return
     */
    private int calculateSubClassesScore () {
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


    private int calculateTeachersScore() {
        int consecutiveHoursScore = 0;
        int evenHoursScore = 0;
        int teacherId, lessonId;

        int firstTeacher, middleTeacher, lastTeacher;
        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay; d++) {
                    for (int h = 2; h < maxHour ; h++) { //starts from the 3rd hour on
                        if (chromosome[c][s][d][h].getLesson().getId() > 0) {
                            lastTeacher = chromosome[c][s][d][h].getTeacher().getId();
                        } else lastTeacher = -1;
                        if (chromosome[c][s][d][h-1].getLesson().getId() > 0) {
                            middleTeacher = chromosome[c][s][d][h - 1].getTeacher().getId();
                        } else middleTeacher = -1;
                        if (chromosome[c][s][d][h-2].getLesson().getId() > 0) {
                            firstTeacher = chromosome[c][s][d][h - 2].getTeacher().getId();
                        } else firstTeacher = -1;
                        //consecutiveHoursScore = consecutiveHoursScore
                                //+ compareTeachersId (firstTeacher, middleTeacher, lastTeacher);
                    }
                }
            }
        }
        //evenHoursScore = calcTeachersEvenHoursPerLesson(assignedLessons, assignedTeachers);
        return consecutiveHoursScore + evenHoursScore;
    }

//    private int calculateLessonsScore() {
//        return 0;
//    }
//
//    private HashMap<Lesson,Integer> updateAssignedLessons (HashMap<Lesson,Integer> assignedLessons,
//                                                           Lesson lesson) {
//        if (!assignedLessons.containsKey(lesson)) {
//            assignedLessons.put(lesson,1);
//        } else {
//            assignedLessons.put(lesson, assignedLessons.get(lesson) + 1);
//        }
//        return assignedLessons;
//    }
//
//    private HashMap<Teacher,Integer> updateAssignedTeachers (HashMap<Teacher,Integer> assignedTeachers,
//                                                           Teacher teacher) {
//        if (!assignedTeachers.containsKey(teacher)) {
//            assignedTeachers.put(teacher,1);
//        } else {
//            assignedTeachers.put(teacher, assignedTeachers.get(teacher) + 1);
//        }
//        return assignedTeachers;
//    }




//    private int calcSubClassesEvenHours (int[][][] subClassesHours, int maxEvenHoursScore) {
//        int temp = maxEvenHoursScore;
//        for (int c = 0; c < maxClasses; c++) {
//            for (int s = 0; s < maxSubClasses; s++) {
//                for (int d = 0; d < maxDay-1; d++) {
//                    for (int nextDay = d+1; nextDay < maxDay; nextDay++) {
//                        temp = temp -
//                                Math.abs(subClassesHours[c][s][d] - subClassesHours[c][s][nextDay]);
//                    }
//                }
//            }
//        }
//        return temp;
//    }


    /*
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
    */

    private int compareTeachersId (int teacher_A, int teacher_B, int teacher_C) {
        if (teacher_A == teacher_B &&
                teacher_B == teacher_C &&
                    teacher_C != -1) {
            return -1;
        }
        return 0;
    }

//    private int compareAssignedTeachers (HashMap<Integer,Integer> assignedTeachersStrict,
//                                         HashMap<Integer,Integer> assignedTeachersInMoreThanTwo) {
//        int teacherA, teacherB;
//        int counterStrict, counterAll;
//        int [] assignedTeachersToCheck = new int [assignedTeachersInMoreThanTwo.size()];
//        int index = 0;
//        for (int teacherId: assignedTeachersInMoreThanTwo.keySet()) {
//            if (assignedTeachersStrict.containsKey(teacherId)) {
//                counterStrict = assignedTeachersStrict.get(teacherId);
//                counterAll = assignedTeachersInMoreThanTwo.get(teacherId);
//                assignedTeachersInMoreThanTwo.put(teacherId, counterStrict+counterAll);
//            }
//            assignedTeachersToCheck [index++] =
//                    assignedTeachersInMoreThanTwo.get(teacherId);
//        }
//        for (int i = 1; i < index ; i++) {
//            compareTeachersHours(index[])
//        }
//    }
//
//    private int compareTeachersHours (int teacher_A, int teacher_B) {
//        if (teacher_A == teacher_B &&
//                teacher_B != -1) {
//            return -1;
//        }
//        return 0;
//    }
//    */

    public double fitness() { return 0.0; }

    //public Chromosome mutate() { return new Chromosome(); }

    public Gene[][][][] getGenes () {
        return this.chromosome;
    }

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

