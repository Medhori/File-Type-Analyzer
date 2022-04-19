package analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static void main(String[] args) {
        String directory = args[0];
        String pattern_db = args[1];
        String str = "";
        String database = "";
        File fileDb = new File(pattern_db);
        List<String[]> listDb = new ArrayList<>();

        try (Scanner scanner = new Scanner(fileDb)) {
            while (scanner.hasNext()) {
                listDb.add(scanner.nextLine().replaceAll("[\"]", "").split(";"));

            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + pattern_db);
        }
        Collections.reverse(listDb);
        File dir = new File(directory);
        File[] list = dir.listFiles();
        for (File file : list) {
            try {
                str = readFileAsString(file.getPath());
                String finalStr = str;
                Thread thread = new Thread(() -> {
                    boolean found = false;
                    for (String[] array : listDb) {
                        if (isContainsRabinKarpAlgorithm(array[1], finalStr, array[2], file.getName())) {
                            System.out.println(file.getName() + ": " + array[2]);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println(file.getName() + ": " + "Unknown file type");
                    }

                });

                thread.start();
                thread.join();
            } catch (IOException | InterruptedException e) {
                System.out.println("Cannot read file: " + e.getMessage());
            }
        }
    }
    private static boolean isContainsKMPAlgorithm(String patten, String str, String result, String filename) {
        boolean check = false;
        check = KMPSearch(str, patten);
        return check;
    }

    private static boolean isContainsRabinKarpAlgorithm(String patten, String str, String result, String filename) {
        boolean check = false;
        check = rabinKarpContains(str, patten);
        return check;
    }

    public static boolean rabinKarpContains(String text, String pattern) {
        int a = 117;
        long m = 173_961_102_589_771L;

        if (pattern.length() > text.length()) {
            return false;
        }

        long patternHash = 0;
        long currSubstrHash = 0;
        long pow = 1;

        for (int i = 0; i < pattern.length(); i++) {
            patternHash += (long) pattern.charAt(i) * pow;
            patternHash %= m;

            currSubstrHash += (long) text.charAt(text.length() - pattern.length() + i) * pow;
            currSubstrHash %= m;

            if (i != pattern.length() - 1) {
                pow = pow * a % m;
            }
        }

        for (int i = text.length(); i >= pattern.length(); i--) {
            if (patternHash == currSubstrHash) {
                for (int j = 0; j < pattern.length(); j++) {
                    if (text.charAt(i - pattern.length() + j) != pattern.charAt(j)) {
                        break;
                    }
                }
                return true;
            }

            if (i > pattern.length()) {
                currSubstrHash = (currSubstrHash - text.charAt(i - 1) * pow % m + m) * a % m;
                currSubstrHash = (currSubstrHash + text.charAt(i - pattern.length() - 1)) % m;
            }
        }
        return false;
    }


    private static void isContainsNaiveAlgorithm(String pattern, String str, String result) {
        boolean check = false;
        if (str.contains(pattern)) {
            check = true;
        }
        System.out.println(check ? result : "Unknown file type");
    }


    public static int[] prefixFunction(String str) {
        int[] prefixFunc = new int[str.length()];
        for (int i = 1; i < str.length(); i++) {
            int j = prefixFunc[i - 1];
            while (j > 0 && str.charAt(i) != str.charAt(j)) {
                j = prefixFunc[j - 1];
            }
            if (str.charAt(i) == str.charAt(j)) {
                j += 1;
            }
            prefixFunc[i] = j;
        }
        return prefixFunc;
    }

    public static boolean KMPSearch(String text, String pattern) {
        int[] prefixFunc = prefixFunction(pattern);
        ArrayList<Integer> occurrences = new ArrayList<Integer>();
        int j = 0;

        for (int i = 0; i < text.length(); i++) {

            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = prefixFunc[j - 1];
            }

            if (text.charAt(i) == pattern.charAt(j)) {
                j += 1;
            }

            if (j == pattern.length()) {
                return true;
               
            }
        }
        return false;
    }

}
