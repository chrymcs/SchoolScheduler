package io;

import myObjects.Lesson;
import myObjects.Teacher;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;

public class Importer {

    // Function to read file of lessons
    public HashMap<Integer ,Lesson> readLessonsFile(String fileName) throws Exception {

        File file = new File(fileName);
        // the file includes greek, so we need to set charset = ISO-8859-7. UTF8 doesn't work.
        Charset iso = Charset.forName("ISO-8859-7");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), iso));

        HashMap<Integer, Lesson> hashMap = new HashMap<>();
        String str;

        // Lessons
        while ((str = br.readLine()) != null) { //loop for each line in text.

            // ignore blank lines
            if (str.matches("\\s*")) continue;

            Lesson lesson = new Lesson(); // id - title - classGrade - hoursPerWeek
            String[] line = str.split("\\s+");

            lesson.setId(Integer.parseInt(line[0]));
            String temp = "";
            boolean isFirstWord = true;

            for (int i = 1; i < line.length - 2; i++) {
                if (isFirstWord) {
                    temp = line[i];
                    isFirstWord = false;
                }
                else temp = temp + " " + line[i];
            }
            lesson.setTitle(temp);
            lesson.setClassGrade(line[line.length - 2]);
            lesson.setWeekHours(Integer.parseInt(line[line.length - 1]));

            hashMap.put(lesson.getId(), lesson);
        }
        return hashMap;
    }

    // Function to read file of teachers
    public HashMap<Integer ,Teacher> readTeachersFile(String fileName) throws Exception {

        File file = new File(fileName);
        // the file includes greek, so we need to set charset = ISO-8859-7. UTF8 doesn't work.
        Charset iso = Charset.forName("ISO-8859-7");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), iso));

        HashMap<Integer ,Teacher> hashMap = new HashMap<>();
        String str;

        while ((str = br.readLine()) != null) {

                // ignore blank lines
                if (str.matches("\\s*")) continue;

                Teacher teacher = new Teacher();
                String [] line = str.split("\\s+");

                teacher.setId(Integer.parseInt(line [0]));
                String temp = "";
                int i = 1;
                boolean isFirstWord = true;
                while (!Character.isDigit(line[i].charAt(0))) {
                    if (isFirstWord) {
                        temp = line[i++];
                        isFirstWord = false;
                    }
                    else temp = temp + " " + line[i++];
                }
                teacher.setName(temp);
                LinkedList<Integer> lessons = new LinkedList<>();

                for (int j = i; j < line.length - 2; j++) {
                    lessons.add(Integer.parseInt(line[j])); //at least 1 course
                }
                teacher.setLessons(lessons);
                teacher.setDayHours(Integer.parseInt(line[line.length - 2])); //day
                teacher.setWeekHours(Integer.parseInt(line[line.length - 1])); //week

            hashMap.put(teacher.getId(), teacher);
        }
        return hashMap;
    }
}
