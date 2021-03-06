/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ugligovic.filegeneratorug;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.stream.LongStream;

/**
 *
 * @author UrosGligovic
 */
public class DataGenerator {

    static final String COMPLETED_BAR = "##########";
    static final String EMPTY_BAR = "          ";

    static class SizeHolder {

        public long size;

        public SizeHolder() {
            this.size = 0;
        }

    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("You have to pass the path to the file you want to generate");
            return;
        }

        long start = System.currentTimeMillis();

        SizeHolder fileSize = new SizeHolder();

        GeneratorParams userParams = new GeneratorParams(args);

        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(userParams.getPath()))) {
            File file = new File(userParams.getPath());
            LongStream.range(0, Long.MAX_VALUE)
                    .peek(x -> showCompletionInPercents(x, fileSize, userParams.getGoalSize(), userParams.getSizePrecision()))
                    .peek(x -> fileWriter.println(UUID.randomUUID().toString() + ";" + x))
                    .peek(x -> flush(x, fileWriter, userParams.getFlushFrequency()))
                    .peek(x -> updateSize(x, fileSize, file, userParams.getSizePrecision()))
                    .filter(x -> checkSize(x, fileSize, userParams.getGoalSize()))
                    .count();

        } catch (FileGeneratedException e) {
            System.out.println("Generated " + userParams.getPath());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR! " + e);

        }

        System.out.println("Finished in " + ((System.currentTimeMillis() - start)) + " miliseconds!");
    }

    static void flush(long x, PrintWriter fileWriter, long flushFrequency) {
        if (x % flushFrequency == 0) {
            fileWriter.flush();
        }
    }

    static void updateSize(long x, SizeHolder fileSize, File file, long sizePresizion) {
        if (x % sizePresizion == 0) {
            fileSize.size = file.length();
        }
    }

    static boolean checkSize(long x, SizeHolder fileSize, long goalSize) {

        if (fileSize.size >= goalSize) {
            throw new FileGeneratedException((x+1) + " lines were generated!"); //not the best practice but not to make people install Java9 because of takeWhile
        }
        return true;

    }

    static void showCompletionInPercents(long x, SizeHolder fileSize, long goalSize, long sizePresizion) {
        if (x % sizePresizion == 0) {
            int percentageOfCompletion = (int) (((fileSize.size) * 100) / goalSize);
            percentageOfCompletion = percentageOfCompletion > 100 ? 100 : percentageOfCompletion;
            System.out.print(showCompletionBar(percentageOfCompletion) + percentageOfCompletion + "%\r");
        }

    }

    static String showCompletionBar(int percentageOfCompletion) {

        if (percentageOfCompletion >= 100) {
            return "";
        }
        return "["
                + COMPLETED_BAR.substring(0, percentageOfCompletion / 10)
                + EMPTY_BAR.substring(percentageOfCompletion / 10, EMPTY_BAR.length() - 1)
                + "]";
    }

}
