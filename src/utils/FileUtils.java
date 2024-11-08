// utils/FileUtils.java
package utils;

import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FileUtils {
    private static final String FILE_NAME = "numbers.txt";
    private static final Random random = new Random();

    public static Set<Integer> loadGeneratedNumbers() {
        Set<Integer> generatedNumbers = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                generatedNumbers.add(Integer.parseInt(line.trim()));
            }
        } catch (IOException e) {
            System.out.println("No existing numbers found. Starting fresh.");
        }
        return generatedNumbers;
    }

    public static int generateUniqueRandomNumber(int min, int max, Set<Integer> generatedNumbers) {
        int randomNumber;
        do {
            randomNumber = random.nextInt((max - min) + 1) + min;
        } while (generatedNumbers.contains(randomNumber));
        generatedNumbers.add(randomNumber);
        return randomNumber;
    }

    public static void storeGeneratedNumber(int number) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(number + "\n");
        } catch (IOException e) {
            System.out.println("Error storing the generated number: " + e.getMessage());
        }
    }

    public static boolean clearGeneratedNumbers() {
        File file = new File(FILE_NAME);
        return file.exists() && file.delete();
    }
}
