import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;

public class Importer {

    public Importer () {
    }

    //general purpose reader function (it can recognise file content by reading the file name)
    public LinkedList < String [] > read(String fileName) throws Exception {
        File file = new File(fileName);
        // the file includes greek, so we need to set charset = ISO-8859-7. UTF8 doesn't work.
        Charset iso = Charset.forName("ISO-8859-7");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), iso));
        LinkedList <String []> linkedList = new LinkedList<>();
        String str;
        //Lessons
        if (fileName.startsWith("L") || fileName.startsWith("l")) {
            while ((str = br.readLine()) != null) { //loop for each line in text.

                String [] course = new String[4]; // courseCode - title - class - hours
                String [] line = str.split("\\s+");

                course [0] = line [0];
                String temp = "";
                boolean isFirstWord = true;

                for (int i=1; i<= line.length-3; i++) {
                    if (isFirstWord) {
                        temp = line[i];
                        isFirstWord = false;
                    }
                    else temp = temp + " " + line[i];
                }
                course [1] = temp;
                course [2] = line [line.length-2];
                course [3] = line [line.length-1];

                linkedList.add(course);
            }
        } else {
            while ((str = br.readLine()) != null) { //loop for each line in text.

                String [] teacher = new String[5]; // teacherCode - Name - courseCodes - hours/day - hours/week
                String [] line = str.split("\\s+");

                teacher [0] = line [0];
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
                teacher [1] = temp;

                temp = line[i++];
                for (int j = i; i <= line.length-3; i++) {
                        temp = temp + ", " + line[i];
                }
                teacher [2] = temp;
                teacher [3] = line [line.length-2]; //day
                teacher [4] = line [line.length-1]; //week

                linkedList.add(teacher);
            }
        }
        return linkedList;
    }
}
