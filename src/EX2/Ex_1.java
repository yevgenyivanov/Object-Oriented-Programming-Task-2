package EX2;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static EX2.Ex_1.*;
import static java.lang.Thread.sleep;
/**
 * @author Dor Yanay, Yevgeny Ivanov
 * this Program was written for Object Orienting Programming Course as an Assignment.
 * In this Class we are checking the differences in times when using 3 different algorithms.
 * non-thread , threads, threadpool.
 * You can look at the readme page to see the results and diagrams.
 * Sub Classes:
 * MyThread.java - for the third function.
 * MyThreadPoolCallable - for the forth function.
 */
public class Ex_1 {
    //FILES CREATIONS

    /**
     *
     * @param n - number of files
     * @param seed - used for creating a new random number generator.
     * @param bound - maximum number of lines for each file the program create.
     * @return String array that contains the names of the files.
     * @throws IOException
     */
    public static String[] createTextFiles(int n, int seed, int bound) throws IOException {
        String[] filenames = new String[n];
        Random rand = new Random(seed);
        for (int i = 0; i < n; i++) {
            int randomInt = rand.nextInt(bound);
            String filename = "file_" + (i + 1) + ".txt";
            filenames[i] = filename;
            File file = new File(filename);
            FileWriter writer = new FileWriter(file);
            for (int j = 0; j < randomInt; j++) {
                writer.write(+j + "\n");
            }
            writer.close();
        }
        return filenames;
    }
//NumOfLines function Non-threads.

    /**
     * read the lines using the main thread only.
     * @param fileNames - the given String array with the names of the files the program will read.
     * @return sumoflines - Integer with the sum of all the lines of all the files.
     * @throws IOException
     */
    public static int getNumOfLines(String[] fileNames) throws IOException {

        if (fileNames == null) {
            return 0;
        }
        int sumoflines = 0;
        for (int i = 0; i < fileNames.length; i++) {
            String filename = fileNames[i];
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while (reader.readLine() != null) {
                sumoflines++;
            }
            reader.close();
        }

        return sumoflines;
    }
    //NumOfLines function with threads.

    /**
     * read lines using multiple threads.
     * @param fileNames - the given String array with the names of the files the program will read.
     * @return totalLines - Integer with the sum of all the lines of all the files.
     * @throws InterruptedException
     */
    public static int getNumOfLinesThreads(String[] fileNames) throws InterruptedException{

        if (fileNames == null) {
            return 0;
        }
        int totalLines = 0;
        int n = fileNames.length;
        MyThread[] currentFileThread = new MyThread[fileNames.length];
        for (int i = 0; i < n; i++) {
            currentFileThread[i] = new MyThread(fileNames[i]);
            currentFileThread[i].start();
        }
        for (int i = 0;i<n;i++) {
            currentFileThread[i].join();
            totalLines = totalLines+ currentFileThread[i].returnLines();
        }
        return totalLines;
    }

    //NumOfLines function using ThreadPool.

    /**
     * this Function using threadpool to manage the threads.
     * @param fileNames - the given String array with the names of the files the program will read.
     * @return totalLines - Integer with the sum of all the lines of all the files.
     */
    public static int getNumOfLinesThreadPool(String[] fileNames){

        if (fileNames == null) {
            return 0;
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(fileNames.length);
        int totalLines=0;
        Integer numOfLinesInFile;
        Future<Integer>[] f = new Future[fileNames.length];
        for(int i=0; i< fileNames.length; i++) {
            f[i] = threadPool.submit(new MyThreadPoolCallable(fileNames[i]));//future for current fileThread
        }
        for(int i = 0;i<fileNames.length;i++){
            try {
                numOfLinesInFile= f[i].get();
                totalLines = totalLines + numOfLinesInFile;

            } catch (Exception e) {
                System.err.println(e);
            }
        }
        threadPool.shutdown();
        return totalLines;
    }


//main with time measurement.
    public static void main(String[] args) throws IOException, InterruptedException {
        String[] output = createTextFiles(1000, 1, 200000);
        Instant start = Instant.now();
        System.out.println(getNumOfLines(output));
        Instant end = Instant.now();
        System.out.println("Time taken numoflines: "+ Duration.between(start, end).toMillis() +" milliseconds");
        start = Instant.now();
        System.out.println(getNumOfLinesThreads(output));
        end = Instant.now();
        System.out.println("Time taken numoflinesthreads: "+ Duration.between(start, end).toMillis() +" milliseconds");
        start = Instant.now();
        System.out.println(getNumOfLinesThreadPool(output));
        end = Instant.now();
        System.out.println("Time taken numoflinesthreadpool: "+ Duration.between(start, end).toMillis() +" milliseconds");

    }
}