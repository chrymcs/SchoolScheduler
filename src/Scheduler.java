import java.util.HashMap;
import java.util.LinkedList;

public class Scheduler {
    public HashMap <String, Integer> createAllSubClasses(LinkedList<String[]> lessons, int numA, int numB, int numC) {
        HashMap <String, Integer> courseTable = new HashMap<>();
        int temp;
        for (String [] lesson:lessons){
            if (lesson[2].equalsIgnoreCase("Α")) temp = numA;
            else if (lesson[2].equalsIgnoreCase("Β")) temp = numB;
            else temp = numC;

            for (int i = 1; i <= temp; i++) {
                courseTable.put(lesson[0] + "_" + lesson[2] + i, Integer.parseInt(lesson[3]));
            }
        }
        return courseTable;
    }
}
