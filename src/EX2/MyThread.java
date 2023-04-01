package EX2;
import java.io.*;
import java.util.Scanner;
//Sub Class for the third questions in Part1 of the Assignment.
public class MyThread extends Thread {

    private String fileName;
    private int totalLines;
    //calculating lines same way as in Ex2_1.java but for an individual file
    public static int getNumOfLines(String fileName) throws FileNotFoundException {
        if (fileName == null) {
            return 0;
        }
        int totalLines = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while (reader.readLine() != null) {
                totalLines++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return totalLines;
    }

    public MyThread(String fileName) {
        this.fileName = fileName;

    }
    //returns the total num of lines in file (should be run *after* running run() method below)
    public int returnLines() {
        return this.totalLines;
    }

//    @Override
    public void run() {
        if (this.fileName == null) {
            this.totalLines = 0;
        } else {
            try {
                this.totalLines = getNumOfLines(this.fileName);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
