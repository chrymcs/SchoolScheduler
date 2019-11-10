//Θεωρήστε ότι κάθε μία από τις τρεις τάξεις έχει τρία τμήματα (Α1, Α2, Α3, Β1, Β2, Β3, Γ1, Γ2, Γ3).

//Το λογισμικό θα πρέπει να αναζητεί ωρολόγιο πρόγραμμα που να ικανοποιεί τους περιορισμούς
//των αρχείων lessons και teachers. Επιπλέον να ικανοποιούνται κατά το δυνατόν οι παρακάτω περιορισμοί:
/*
1. Να μην υπάρχουν κενά στο πρόγραμμα κανενός τμήματος.
2. Να μη διδάσκει κανένας καθηγητής περισσότερες από δύο συνεχόμενες ώρες.
3. Ο ημερήσιος αριθμός ωρών διδασκαλίας κάθε τμήματος να είναι κατά το δυνατόν ομοιόμορφος όλες τις ημέρες.
4. Οι ώρες διδασκαλίας κάθε μαθήματος σε ένα τμήμα να είναι ομοιόμορφα κατανεμημένες σε όλες τις ημέρες της εβδομάδας.
5. Ο αριθμός ωρών διδασκαλίας ανά εβδομάδα να είναι κατά το δυνατόν ομοιόμορφος για όλους τους καθηγητές.
*//*
Το λογισμικό να παράγει ένα αρχείο schedule που να παριστάνει το εβδομαδιαίο ωρολόγιο πρόγραμμα όλου του γυμνασίου.
Μπορείτε να διαλέξετε ελεύθερα τη μορφή αυτού του αρχείου, αρκεί να είναι εύκολα κατανοητά τα περιεχόμενά του.
*/
/*

* 5 days , 7hours/each
 * 1st class    8:15 - 9:00      5' break
 * 2nd class    9:05 - 9:50      10' break
 * 3rd class    10:00 - 10:45    10' break
 * 4th class    10:55 - 11:40    10' break
 * 5th class    11:50 - 12:30    5' break
 * 6th class    12:35 - 13:15    5' break
 * 7th class    13:20 - 14:00
 *
 *
 * Α1, Α2, Α3, Β1, Β2, Β3, Γ1, Γ2, Γ3
 * h istoria didasketai 2 wres/vdomada se kathe taksi. Ara 2 wres se kathe tmima twn taksewn. 2 sto A1, 2 sto A2.. klp..
 *
*/

import io.Lesson;
import io.Importer;
import io.Teacher;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        Importer importer = new Importer();
        LinkedList<Lesson> lessons = null;
        LinkedList<Teacher> teachers = null;

        try {
            lessons = importer.createLessonsList(args[0]);
            teachers = importer.createTeachersList(args[1]);
        } catch (Exception e) {
            System.err.println("No proper arguments given.");
            System.err.println("The arguments should be lessons.txt teachers.txt");
        }

        if (lessons != null) {
            System.out.println("Lessons:");
            for (Lesson lesson: lessons)
                System.out.println(lesson);
        }

        if (teachers != null) {
            System.out.println("Teachers:");
            for (Teacher teacher: teachers)
                System.out.println(teacher);
        }
    }
}