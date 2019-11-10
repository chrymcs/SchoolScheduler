import java.io.*;
import java.nio.charset.Charset; //gia na mporei na diavasei greek letters apo ta txt arxeia

public class MainChr {

    // Function to remove the element (thn vrika online). Xrisimeuei stis readLessons & readTeachers ws eksis:

    /**
     * mesa sthn readLessons exw mia megalh while. ousiastika se kathe while, allazw grammh sto txt arxeio.
     * Pame sthn 1h grammh:     1 Νεοελληνική Γλώσσα Α3 Β2 Γ2
     * th diavazw, kanw split se kathe 'space' kai mpainei kathe leksi se ena keli tou proswrinou pinaka line[].
     * Ara twra exw ston pinaka line:
     * line[0] = 1
     * line[1] = Νεοελληνική
     * line[2] = Γλώσσα
     * line[3] = Α3
     * line[4] = Β2
     * line[5] = Γ2
     * <p>
     * thelw omws to "Νεοελληνική Γλώσσα" na einai se ena keli, oxi se dyo.
     * Ara prepei na valw to "Γλώσσα" sto line[1] kai na ferw ta epomena ena index prin.
     * diladi:
     * line[0] = 1
     * line[1] = Νεοελληνική Γλώσσα
     * line[2] = Α3
     * line[3] = Β2
     * line[4] = Γ2
     * <p>
     * Gia na ginei auto, xrisimopoiw th sunarthsh removeTheElement, h opoia ousiastika svinei ena element
     * kai "anevazei" ola ta epomena, mia thesh "pio panw".
     */
    public static String[] removeTheElement(String[] arr,
                                            int index) {

        // If the array is empty
        // or the index is not in array range
        // return the original array
        if (arr == null || index < 0 || index >= arr.length) {
            return arr;
        }

        // Create another array of size one less
        String[] anotherArray = new String[arr.length - 1];

        // Copy the elements except the index
        // from original array to the other array
        for (int i = 0, k = 0; i < arr.length; i++) {

            // if the index is
            // the removal element index
            if (i == index) {
                continue;
            }

            // if the index is not
            // the removal element index
            anotherArray[k++] = (arr[i]);
        }

        // return the resultant array
        return anotherArray;
    }

    // Function to read file of lessons
    public static String[][] readLessons() throws Exception {
        //TODO pathname won't be the same in class. It has to be ok in every environment.
        File fileName = new File("C:\\Users\\cmihe\\Desktop\\lessons.txt");
        // the file includes greek, so we need to set charset = ISO-8859-7. UTF8 doesn't work.
        Charset iso = Charset.forName("ISO-8859-7");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), iso));

        //parakatw vlepeis oti exw ftiaksei statiko pinaka, diladi me prokathorismena dimensions.
        //an kapoios allaksei to txt arxeio tha skasei. opote mallon prepei kapws na ginei dynamikos.
        //diladi na mporei na auksomeiwthei o arithmos rows k columns analogws to arxeio.
        int rows = 20;
        int columns = 5;
        String[][] classes = new String[rows][columns];

        String str;
        int x = 0;
        while ((str = br.readLine()) != null) { //loop for each line in text.

            String[] line = str.split("\\s+"); //kathe leksi ths protasis mpainei se ena keli tou pinaka

            //εχω βαλει την γραμμη του αρχειου μεσα στον πινακα line και σε καθε θεση εχει μια λεξη. θελω τους τιτλους των μαθηματων
            //να τους εχω σε μια θεση γιατι τωρα, π.χ. το μαθημα Νεοελληνικη Γλωσσα ειναι σε 2 κελιά.
            int l = line.length;

            // αν το καθε μαθημα ειναι σε 1 κελι, τοτε το μεγεθος καθε γραμμης πρεπει να ειναι 5 (0,1,2,3,4).
            // αν ειναι παραπανω απο 5, σημαινει οτι εχω τιτλο μαθηματος με παραπανω απο μια λεξη.
            // Θελω να βαλω ολον τον τιτλο στο ιδιο κελι.
            if (l >= 6) {
                do { //repeat mexri to length na ginei 5 !
                    line[1] = line[1].concat(" " + line[2]);
                    line = removeTheElement(line, 2);
                } while (line.length != 5);
            }

            //System.out.println("We are in row number "+ x + " in Classes array.");
            for (int j = 0; j < columns; j++) {
                classes[x][j] = line[j];
            }

            // kathe while einai gia ena line, diladi ena row ston pinaka classes.
            // giauto to i tou, (diladi to x) auksanetai ana while ki oxi mesa sth for.
            x++;
        }
        return classes;
    }

    // Function to read file of teachers
    public static String[][] readTeachers() throws Exception {
        File fileName = new File("C:\\Users\\cmihe\\Desktop\\teachers.txt");
        // the file includes greek, so we need to set charset = ISO-8859-7. UTF8 doesn't work.
        Charset iso = Charset.forName("ISO-8859-7");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), iso));

        //omoiws kai edw kalutera tha htan na exoume dunamiko pinaka kai oxi na orizoume emeis to megethos tou.
        //giati p.x. an xreiastei na prosthesoume sto txt mia grammh me kainourio kathigiti, tha vgei OutOfBounds
        int rows = 12;
        int columns = 6;
        String[][] teachers = new String[rows][columns];

        String str;
        int x = 0;
        while ((str = br.readLine()) != null) { //loop for each line in text.
            String[] line = str.split("\\s+"); //kathe leksi ths protasis mpainei se ena keli tou pinaka

            //εχω βαλει την γραμμη του αρχειου μεσα στον πινακα line και σε καθε θεση εχει μια λεξη. θελω το καθε ονομα
            //να το εχω σε μια θεση γιατι τωρα, π.χ. το Αντωνια Νικολαου ειναι σε 2 κελιά.
            int l = line.length;
/*            if (l >= 7) { // αν το καθε ονομα ειναι σε 1 κελι, τοτε το μεγεθος καθε γραμμης πρεπει να ειναι 6.
                //do {
                    line[1] = line[1].concat(" " + line[2]);
                    line = removeTheElement(line, 2);
                //} while (line.length != 6);
            }*/

            // Πρεπει να φτιαξουμε το ονομα να ειναι σε 1 κελι.
            // edw de xrisimopoiw epanalipsi giati ena onoma tha exei to polu mexri 2 lekseis (onoma-epwnumo)
            // ara mono mia fora tha kanw concat, gia na enwsw autes tis 2 lekseis.
            // sto lessons xreiazotan epanalipsi giati enas titlos mathimatos mporei na eixe perissoteres apo 2 lekseis
            if (l == 6) {
                line[1] = line[1].concat(" " + line[2]);
                line = removeTheElement(line, 2);
            }


            // an length=5 tote simainei oti o kathigitis didaskei mono ena mathima. Ara o pinakas mou tha einai kapws etsi :
            /**
             * code     name               class1    class2   hours/day   hours/week
             *  23   Γιώργος Καλογερίδης     5          6       1             1
             *  24   Ευγενία Βασιλείου      13          1       1
             */
            // eksigish: to parapanw einai lathos giati h eugenia vasileiou didaskei mono ena mathima, to 13.
            //          oi duo assoi antistixoun stis wres.
            //          to ftiaxnw sto parakatw if:

            if (l == 5) { //o kathigitis didaskei mono ena mathima. ara:
                for (int j = 0; j <= 2; j++) { //mexri to 2o stoixeio (diladi to 1o class) valta kanonika ston pinaka.
                    teachers[x][j] = line[j];
                }
                teachers[x][3] = "-";          //null sto class 2
                teachers[x][4] = line[3];
                teachers[x][5] = line[4];
            } else {
                for (int j = 0; j < columns; j++) {
                    teachers[x][j] = line[j];
                }
            }
            /**
             * Twra o pinakas exei ginei etsi:
             *
             * code     name               class1    class2   hours/day   hours/week
             *  23   Γιώργος Καλογερίδης     5          6       1             1
             *  24   Ευγενία Βασιλείου      13          -       1             1
             */

            //kathe while einai gia ena line, diladi ena row ston pinaka teachers. giauto to i tou(x) auksanetai ana while ki oxi mesa sth for.
            x++;
        }
        return teachers;
    }
}

//    public static void main(String[] args) throws Exception {
/*        System.out.println("Lessons:\n");
        String[][] lessons = readLessons();
        for (int i = 0; i < lessons.length; i++) {
            for (int j=0; j < lessons[i].length; j++) {
                System.out.println(lessons[i][j]);

            }
            System.out.println("\n");
        }

        String[][] teachers = readTeachers();
        for (int i = 0; i < teachers.length; i++) {
            for (int j=0; j < teachers[i].length; j++) {
                System.out.println(teachers[i][j]);
            }
            System.out.println("\n");
        }
    }*/
//}