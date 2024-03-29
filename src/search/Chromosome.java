package search;

import myObjects.Lesson;
import myObjects.Teacher;

import java.util.*;

/**
 * Class that represents a Schedule - Chromosome
 * Each Chromosome has 7*45 genes.
 */
public class Chromosome implements Comparable<Chromosome> {

    private Gene[][][][] chromosome; // hoursPerDay - daysPerWeek - subClassesPerSchedule
    private int maxClasses = 3, maxDay = 5, maxHour = 7, maxSubClasses = 3;
    private int fitness;
    private int subClassesGapsScore, consecutiveTeachersScore, unevenHoursScore,
                teachersEvenHours, acceptableTeachersHours, acceptableLessonsHours, unevenDistributedHoursPerLesson;


    private HashMap<Integer,Teacher> allTeachers;
    private HashMap<Integer,Lesson> allLessons;
    private HashMap<Teacher,Integer> assignedTeachers = new HashMap<>();
    private HashMap<Lesson,Integer> assignedLessons = new HashMap<>();


    /** CONSTRUCTORS */
    //Copy constructor for the children chromosomes
    public Chromosome (Gene[][][][] childChromosome,
                       HashMap<Integer,Teacher> teacherHashMap,
                       HashMap<Integer,Lesson> lessonHashMap) {
        allTeachers = teacherHashMap;
        allLessons = lessonHashMap;
        Gene gene;
        chromosome = new Gene[maxClasses][maxSubClasses][maxDay][maxHour];
        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay; d++) {
                    for (int h = 0; h < maxHour; h++) {
                        gene = childChromosome[c][s][d][h];
                        chromosome[c][s][d][h] = gene;
                        assignedLessons = updateAssignedLessons(assignedLessons,
                                gene.getLesson());
                        assignedTeachers = updateAssignedTeachers(assignedTeachers,
                                gene.getTeacher());
                    }
                }
            }
        }
        calculateFitness();
    }

    //Constructs a randomly created chromosome
    public Chromosome(HashMap<Integer,Teacher> teacherHashMap,
                      HashMap<Integer,Lesson> lessonHashMap,
                      ArrayList<Lesson> lA,
                      ArrayList<Lesson> lB,
                      ArrayList<Lesson> lC) {

        allTeachers = teacherHashMap;
        allLessons = lessonHashMap;

        Gene gene;
        Lesson lesson;
        Teacher teacher;
        Teacher nullTeacher = new Teacher(-1, "NULL", null, 0, 0);
        this.chromosome = new Gene[3][3][5][7];
        Random r = new Random();

        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) { //foreach subClass
                for (int d = 0; d < maxDay; d++) { //foreach day
                    for (int h = 0; h < maxHour  ; h++) { //foreach hour

                        if (c == 0)
                            lesson = lA.get(r.nextInt(lA.size()));
                        else if (c == 1)
                            lesson = lB.get(r.nextInt(lB.size()));
                        else
                            lesson = lC.get(r.nextInt(lC.size()));

                        if (lesson.getId() == -1) {
                            teacher = nullTeacher;
                        }
                        else {
                            teacher = lesson.getAvailableTeachers().get(r.nextInt(lesson.getAvailableTeachers().size()));
                        }
                        gene = new Gene(lesson, teacher);
                        this.chromosome[c][s][d][h] = gene;

                        //In each assignment og lesson-teacher combination the program keeps a
                        //record of the assigned teachers and lessons in order to be handled
                        //later on and during constraint #5
                        assignedLessons = updateAssignedLessons(assignedLessons,
                                gene.getLesson());
                        assignedTeachers = updateAssignedTeachers(assignedTeachers,
                                gene.getTeacher());
                    }
                }
            }
        }
        calculateFitness();
    }


/** ------------------------------------------------------------------------------------------------------------------------------------------------ */

    /** SETTERS - GETTERS */

    public Gene[][][][] getGenes () {
        return this.chromosome;
    }

    public int getFitness() {
        return fitness;
    }

    public int getAcceptableLessonsHours () {
        return acceptableLessonsHours;
    }

    public int getAcceptableTeachersHours () {
        return acceptableTeachersHours;
    }

/** ------------------------------------------------------------------------------------------------------------------------------------------------ */
    /** CALCULATE FITNESS METHODS */

    //calculate total fitness for every constraint and the must constraints of all lessons to be
    //taught the needed hours + all teachers taught hours that they could
    private void calculateFitness() {

        //#1
        subClassesGapsScore = calculateGapsScore();
        //#2
        consecutiveTeachersScore = calculateConsecutiveTeachersScore();
        //#3
        unevenHoursScore = calculateUnevenHoursScore();
        //#4
        unevenDistributedHoursPerLesson = calcUnevenDistributedHoursPerLesson();
        //#5
        teachersEvenHours = calculateTeachersEvenHours();

        //Must
        acceptableTeachersHours = calculateAcceptableTeachersHours();
        acceptableLessonsHours = calculateAcceptableLessonsHours();

        fitness = subClassesGapsScore
                + consecutiveTeachersScore
                + unevenHoursScore
                + unevenDistributedHoursPerLesson
                + teachersEvenHours
                + acceptableTeachersHours
                + acceptableLessonsHours;

        fitness = fitness / 7; //(M.O.)
    }

    /**
     * Constraint #1 �� ��� �������� ���� ��� ��������� ������� ��������.
     * ��� ���� ���� �������� ��� ��������� ���� ����������� ��� ���� ��� ������
     * ����������� ���� ��� ����� ��� ��� ������ �������� ��� ���� �������� (id<0)
     * ������������ �� �������� score �� ��� ���� (��������� ���� ����������� - ����) / �.�.�.
     */
    private int calculateGapsScore() {
        int gapsCounter = 0;
        float gapsScore; //total gap hours
        int teachingHoursCounter = 0; //total teaching hours
        boolean endOfDayFound;
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
        }
        //the greater the gapsScore is, the better the program
        gapsScore = (float) (teachingHoursCounter - gapsCounter)/teachingHoursCounter;
        gapsScore = Math.round(gapsScore * 100);
        return (int) gapsScore;
    }

    /**
     * Constraint #2 �� �� �������� ������� ��������� ������������ ��� ��� ����������� ����
     * (�.�. �� ������� ��������� ���� ������ ����� ���� ������ ����, �� ���� �����������
     * ��� ���� ��� ������ ��� ������ ��� � ������ ��� ��� ���������� ����).
     */
    public int calculateConsecutiveTeachersScore() {
        int consecutiveHours = 0;
        int consecutiveScore = 100;

        int firstTeacher, middleTeacher, lastTeacher;
        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) {
                for (int d = 0; d < maxDay; d++) {
                    for (int h = 2; h < maxHour ; h++) { //starts from the 3rd hour on

                        //we take teachers in triplets and check if they are the same
                        lastTeacher = chromosome[c][s][d][h].getTeacher().getId();
                        middleTeacher = chromosome[c][s][d][h - 1].getTeacher().getId();
                        firstTeacher = chromosome[c][s][d][h - 2].getTeacher().getId();

                        consecutiveHours = consecutiveHours +
                                compareTeachersId (firstTeacher, middleTeacher, lastTeacher);
                    }
                }
            }
        }
        //In case too many teachers have 3 consecutive hours of teaching then the program maxes
        //the consecutive hours to 100, so this category's fitness is 0 (bad).
        if (consecutiveHours > 100) consecutiveHours = 100;
        consecutiveScore = consecutiveScore - consecutiveHours;

        return consecutiveScore;
    }

    /**
     * Secondary method for Constraint #2 to compare triplets of teachers
     */
    private int compareTeachersId (int teacher_A, int teacher_B, int teacher_C) {
        if (teacher_A == teacher_B && teacher_B == teacher_C && teacher_C != -1)  {
            return 1;
        }
        return 0;
    }

    /**
     * Constraint #3 � ��������� ������� ���� ����������� ���� �������� �� ����� ���� �� �������
     * ����������� ���� ��� ������ (�.�. ��� 4��� �� �������, 7��� ��� �����, 3��� ��� �������,
     * 7��� ������, 4��� ���������).
     */
    private int calculateUnevenHoursScore() {
        //total teaching hours
        int teachingHoursPerDay;

        //Worst case scenario (where teaching hours in the 5 days is something like
        // 7 - 0 - 7 - 0 - 7
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
                    //we keep a matrix of the teaching hours for each day for each subClass and
                    // class
                    subClassesHours[c][s][d] = teachingHoursPerDay;
                }
            }
        }

        evenHoursScore = calcSubClassesEvenHours(subClassesHours,maxEvenHoursScore);
        return evenHoursScore;
    }

    /**
     * Secondary method for Constraint #3
     * It compares alla days with the rest and calculates the absolute difference.
     * If this difference for all the days is 0 then the program returns 100 score.
     */
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
        temp = (float) (maxScore - evenHoursScore) / maxScore;
        temp = Math.round(temp * 100 * 10 / 9);
        if (temp > 100) temp =100;
        return (int) temp;
    }


    /** Constraint #4 : �� ���� ����������� ���� ��������� �� ��� ����� �� �����
     *  ���� �� ������� ���������� ������������� �� ���� ��� ������ ��� ���������
     *  (�.�. �� �� ����������� ���� �� ���� ��� ��������� ��� ���� �����).
     */
    private int calcUnevenDistributedHoursPerLesson() {

        //kathe fora pou vriskw to mathima mesa sth mera, auksanw kata 1 to sumPerLess
        int sumPerLess = 0;
        int totalSum = 0;

        int cc = 0;

        for (Map.Entry pair: allLessons.entrySet()) {
            Lesson l = (Lesson) pair.getValue();

            if (l.getClassGrade().equalsIgnoreCase("A")) cc = 0;
            else if (l.getClassGrade().equalsIgnoreCase("B")) cc = 1;
            else cc = 2;

            for (int s = 0; s < maxSubClasses; s++) {
                sumPerLess = 0;
                for (int d = 0; d < maxDay; d++) {
                    for (int h = 0; h < maxHour; h++) {
                        if (chromosome[cc][s][d][h].getLesson() == l) {
                            sumPerLess++;
                            break;
                        }
                    }
                }
                totalSum += (sumPerLess / 5.0) * 100;
            }
        }
        int temp = totalSum / allLessons.size();
        if (temp > 100) temp =100;
        return temp;
    }

    /**
     * Constraint #5 � ������� ���� ����������� ��� �������� �� ����� ���� �� ������� �����������
     * ��� ����� ���� ��������� (�.�. �� �� �������� ���� 25 ���� ��� �������� ��� ����� ���� 5).
     */
    private int calculateTeachersEvenHours () {
        Teacher teacherA;
        Teacher teacherB;
        int problematicComparisons = 0;
        int totalComparisons = 0;
        float score;
        LinkedList<Integer> checkedIds = new LinkedList<>();
        for (int teacherIdA : allTeachers.keySet()) {
            checkedIds.add(teacherIdA);
            for (int teacherIdB : allTeachers.keySet()) {
                if (teacherIdA != teacherIdB && !checkedIds.contains(teacherIdB)) {
                    teacherA = allTeachers.get(teacherIdA);
                    teacherB = allTeachers.get(teacherIdB);
                    totalComparisons++;
                    //We calculate the difference between the hours gof the teachers and if it is
                    //Greater than a prefix value we count that as a problematic case.
                    //Score is given by the formula (total comparisons made - probComparisons)/
                    // total comparisons made
                    if (Math.abs(assignedTeachers.getOrDefault(teacherA,0)
                            - assignedTeachers.getOrDefault(teacherB,0)) > 10) {
                        problematicComparisons++;
                    }
                }
            }
        }
        score = (float) (totalComparisons - problematicComparisons) / totalComparisons;
        score = Math.round(score * 100);
        return (int) score;
    }

    /**
     * Must Constraint of teachers.txt file. It calculates whether all teachers are teaching to
     * as many hours as the can per week and day.  We keep their hours in a matrix as for the day
     * and as for the week and in the end we compare it with the hours given by the txt file.
     */
    private int calculateAcceptableTeachersHours () {

        //A list that will hold, each teacher's week hours of teaching
        HashMap<Teacher, Integer> teachersMaxWeekHours = new HashMap<>();

        HashMap<Teacher, Integer> teachersMaxDayHours = new HashMap<>();
        HashMap<Teacher, Integer> teachersDayHours;
        int previousWeekHours;
        int previousDayHours;
        Teacher teacher;

        for (int d = 0; d < maxDay; d++) { //foreach day
            teachersDayHours = new HashMap<>();
            for (int c = 0; c < maxClasses; c++) {
                for (int s = 0; s < maxSubClasses; s++) { //foreach subClass
                    for (int h = 0; h < maxHour; h++) { //foreach hour
                        teacher = chromosome[c][s][d][h].getTeacher();

                        if (teacher.getId()>0) {
                            previousDayHours = teachersDayHours.getOrDefault(teacher, 0);
                            teachersDayHours.put(teacher, previousDayHours + 1);

                            previousWeekHours = teachersMaxWeekHours.getOrDefault(teacher, 0);
                            teachersMaxWeekHours.put(teacher, previousWeekHours + 1);
                        }
                    }
                }
            }

            //for every teacher that we found in that day
            for (Teacher t: teachersDayHours.keySet()) {

                //if (1) our list that keeps all maximum hours of all teachers has that teacher
                //and has lower value that the hours of the day we are checking
                // or (2) our list that keeps all maximum hours of all teachers does not have that
                // teacher
                // then put today's' hours in the teacher's maxHour value
                if (!teachersMaxDayHours.containsKey(t) || teachersMaxDayHours.get(t) < teachersDayHours.get(t)) {

                    teachersMaxDayHours.put(t, teachersDayHours.get(t));
                }
            }
        }

        //Comparing of the hours of the teachers that were assigned to the hours that could teach
        //Per week
        int negativeScoreWeek = 0;
        int negativeScoreDay = 0;
        int totalTeachers = 0;
        for (int teacherId : allTeachers.keySet()) {
            if (teacherId>0) {
                totalTeachers++;

                teacher = allTeachers.get(teacherId);
                if (teachersMaxWeekHours.containsKey(teacher) &&
                    teacher.getWeekHours() < teachersMaxWeekHours.get(teacher))
                    negativeScoreWeek++;
                if (teachersMaxDayHours.containsKey(teacher) &&
                    teacher.getDayHours() < teachersMaxDayHours.get(teacher))
                    negativeScoreDay++;
            }
        }

        float overallScore;
        overallScore = (float) ((totalTeachers * 2)  - (negativeScoreDay + negativeScoreWeek))
                / (totalTeachers *2);
        overallScore = Math.abs(overallScore *100);

        return (int) overallScore;
    }

    /**
     * Must Constraint of lessons.txt file
     * Same logic as above. We keep track of all the lessons that were taught in the schedule as
     * well as how many hours they were taught and we compare it with the hours in the txt file
     */
    private int calculateAcceptableLessonsHours () {

        /**
         * all lessons have to be teached exactly as many hours (per class) as the txt file says.
         * hours * 3, because a class have 3 subclasses.
        */

        Lesson lesson;
        int previousWeekHours;
        HashMap<Lesson,Integer> lessonsWeekHours; //krataei to mathima pou vrike kai poses wres auto exei didaxtei
        float overallScore = 0;

        for (int c = 0; c < maxClasses; c++) {
            for (int s = 0; s < maxSubClasses; s++) { //foreach subClass
                lessonsWeekHours = new HashMap<>();
                for (int d = 0; d < maxDay; d++) { //foreach day
                    for (int h = 0; h < maxHour; h++) { //foreach hour
                        lesson = chromosome[c][s][d][h].getLesson();

                        if (lesson.getId()>0) {
                            previousWeekHours = lessonsWeekHours.getOrDefault(lesson, 0);
                            lessonsWeekHours.put(lesson, previousWeekHours + 1);
                        }
                    }
                }
                //gia kathe tmima, afou exoume vrei k exoume kratisei tis wres kathe mathimatos se mia lista,
                //pame k vriskoume apo tis sunolikes wres pou eprepe na didaxtoun se auto to mathima tis taksis an tairiazoun oi wres.

                //ola ta mathimata ths taksis prepei na didaxtoun

                int notProperlyTaughtLessons = 0;
                int totalLessons = 0;
                for (int lessonId : allLessons.keySet()) {
                    if (lessonId > 0) {
                        lesson = allLessons.get(lessonId);
                        if ( (lesson.getClassGrade().equals("A") && c==0) ||
                             (lesson.getClassGrade().equals("B") && c==1) ||
                             (lesson.getClassGrade().equals("C") && c==2))  {
                            totalLessons++;
                            if (lessonsWeekHours.containsKey(lesson) &&
                                lessonsWeekHours.get(lesson) != lesson.getWeekHours()) {
                                notProperlyTaughtLessons++;
                            } else if (!lessonsWeekHours.containsKey(lesson)) {
                                notProperlyTaughtLessons++;
                            }
                        }
                    }
                }
                overallScore = overallScore +
                        (float) (totalLessons - notProperlyTaughtLessons) / totalLessons;
            }
        }
        //Division with 9 subClasses
        overallScore = Math.round((overallScore/9) *100);
        return (int) overallScore;
    }


    private HashMap<Lesson,Integer> updateAssignedLessons (HashMap<Lesson,Integer> assignedLessons,
                                                           Lesson lesson) {
        if (lesson.getId()>0) {
            if (!assignedLessons.containsKey(lesson)) {
                assignedLessons.put(lesson, 1);
            } else {
                assignedLessons.put(lesson, assignedLessons.get(lesson) + 1);
            }
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

    public void mutate( ArrayList<Lesson> lA,
                        ArrayList<Lesson> lB,
                        ArrayList<Lesson> lC) {

        Random r = new Random();

        //Choose a random slot from the program and change it

        int c = r.nextInt(3); //0 - 1 - 2
        int s = r.nextInt(3);
        int d = r.nextInt(5);
        int h = r.nextInt(7);

        Lesson lesson;

        Teacher nullTeacher = new Teacher(-1, "NULL", null, 0, 0);

        if (c == 0)
            lesson = lA.get(r.nextInt(lA.size()));
        else if (c == 1)
            lesson = lB.get(r.nextInt(lB.size()));
        else
            lesson = lC.get(r.nextInt(lC.size()));

        Teacher teacher;

        if (lesson.getId() <0) { //if the lesson is Null, match it with a Null teacher
            teacher = nullTeacher;
        } else { //choose a random teacher from availableTeachers who can teach the lesson
            teacher =
                lesson.getAvailableTeachers().get(r.nextInt(lesson.getAvailableTeachers().size()));
        }

        Gene gene = new Gene(lesson, teacher);

        chromosome[c][s][d][h] = gene;

        this.calculateFitness();
}

    public void printDetailedFitness () {
        System.out.println("Constraint #1: " + subClassesGapsScore);
        System.out.println("Constraint #2: " + consecutiveTeachersScore);
        System.out.println("Constraint #3: " + unevenHoursScore);
        System.out.println("Constraint #4: " + unevenDistributedHoursPerLesson);
        System.out.println("Constraint #5: " + teachersEvenHours);
        System.out.println();
        System.out.println("Constraint of teachers.txt: " + acceptableTeachersHours);
        System.out.println("Constraint of lessons.txt: " + acceptableLessonsHours);
    }

    @Override
    public String toString() { //the way the excel file (Schedule.xls) shows the final program
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

    @Override
    //The compareTo function has been overriden so sorting can be done according to fitness scores
    public int compareTo(Chromosome x)
    {
        return this.fitness - x.fitness;
    }

}