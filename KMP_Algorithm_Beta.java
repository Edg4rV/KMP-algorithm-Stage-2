package analyzer;

import java.io.*;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class Main {
    private static byte[] pdfSignature = "%PDF-".getBytes();

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Please provide the input file");
            System.exit(0);
        } else if ("--naive".equals(args[0])) {
//            System.out.println("Entering in naive");
            Search check = new Search(new Naive());
            for(String x:args) {
                System.out.println(x);
            }
//            System.out.println(args[1].length() + " " + args[2].length() + " " + args[3].length());
//            System.out.println(args[1] + " " + args[2] + " " + args[3]);
            check.search(args);

        }
        else if ("--KMP".equals(args[0])) {
//            System.out.println("Entering in KMP");
//            System.out.println(args[1].length() + " " + args[2].length() + " " + args[3].length());
//            System.out.println(args[1] + " " + args[2] + " " + args[3]);
            Search check = new Search(new KMP());
            check.search(args);

        }
    }
}

class Search {

    private SearchingStrategy strategy;

    public Search(SearchingStrategy strategy) {
        // write your code here
        this.strategy = strategy;
    }

    /**
     * It performs the search algorithm according to the given strategy
     */
    public  void search(String...args) throws Exception {
        // write your code here
        strategy.patternChecker(args);

    }
}

interface SearchingStrategy {

    void patternChecker(String...args) throws Exception;

}




class Naive implements SearchingStrategy {
    @Override
    public void patternChecker(String... args) throws Exception {
        long starttime = System.nanoTime();
        InputStream inputFile = new FileInputStream(args[1]);
        boolean isPDF = false;
        boolean isDOC = false;
        String check = "";

        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            check = scanner.nextLine();
            if (check.contains("%PDF-")) {
                isPDF = true;
                break;
            } else {
                isPDF = false;
            }
            if (check.contains("%DOC-")) {
                isDOC = true;
                break;
            } else {
                isDOC = false;
            }

        }
        long endtime = System.nanoTime();
        long timeResult =  endtime - starttime;
        double seconds = (double)timeResult / 1_000_000_000.0;
        if ("%PDF-".equals(args[2]) && isPDF) {
            System.out.println("PDF document");
            System.out.println("It took" + " " + seconds + " " + "seconds");
        } else if ("%PDF-".equals(args[2]) && isPDF == false){
            System.out.println("Unknown file type");
            System.out.println("It took" + " " + seconds + " " + "seconds");
        } else if ("%DOC-".equals(args[2]) && isDOC) {
            System.out.println("DOC document");
            System.out.println("It took" + " " + seconds + " " + "seconds");
        } else if ("%DOC-".equals(args[2]) && isPDF == false) {
            System.out.println("Unknown file type");
            System.out.println("It took" + " " + seconds + " " + "seconds");
        }

        scanner.close();



        inputFile.close();

    }
}

class KMP implements SearchingStrategy {

    int[] generatePrefixTable(String... args) {
        int[] table = new int[args[0].length()];
        int j = 0;
        table[0] = 0;
        for (int i = 1; i < args[0].length(); ) {
            if (args[0].charAt(i) == args[0].charAt(j)) {
                j++;
                table[i] = j;
                i++;
            } else {
                if (j != 0) {
                    j = table[j - 1];
                } else {
                    table[i] = 0;
                    i++;
                }
            }
        }
        return table;
    }

    @Override
    public void patternChecker(String... args) throws Exception {
        long starttime = System.nanoTime();
        InputStream inputFile = new FileInputStream(args[1]);
        boolean isPDF = false;
        boolean isDOC = false;
        String check = "";
//------------------------------------------------------------------
        int[] table = generatePrefixTable(args[2]);

        boolean flag = false;
        boolean status = false;
        int txtPos = 0;
        int patternPos = 0;
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            if (flag) {
                break;
            }
            check = scanner.nextLine();
//            System.out.println(">>>" + check);
            while (txtPos < check.length()) {
                if (args[2].charAt(patternPos) == check.charAt(txtPos)) {
                    patternPos++;
                    txtPos++;
                    if (patternPos == args[2].length()) {
                        status = true;
                        txtPos = check.length();
                        flag = true;
                    }

                } else if (args[2].charAt(patternPos) != check.charAt(txtPos)) {
                    status = false;
                    txtPos = check.length();
                    flag = true;
                    if (patternPos != 0) {
                        patternPos = table[patternPos - 1];
                    } else {
                        txtPos++;
                    }
                }
//                if (patternPos == args[2].length()) {
//                    status = true;
//                    System.out.println("Pattern is found at index " + (txtPos - patternPos));
//                    return;
//                }
//            if (args[2].charAt(patternPos) != check.charAt(txtPos)) {
//                    status = false;
//                    if (patternPos != 0) {
//                        patternPos = table[patternPos - 1];
//                    } else {
//                        txtPos++;
//                    }
//                }
//                if (status == false) {
//                    System.out.println("\nPattern not found in text.\n");
//                }
            }
//            if (status == false) {
//                System.out.println("\nPattern not found in text.\n");
//            }
        }
//        System.out.println("Chekc out from while");
        long endtime = System.nanoTime();
        long timeResult = endtime - starttime;
        double seconds = (double) timeResult / 1_000_000_000.0;
        if ("%PDF-".equals(args[2]) && status) {
            System.out.println("PDF document");
            System.out.println("It took" + " " + seconds + " " + "seconds");
        } else if ("%PDF-".equals(args[2]) && status == false) {
            System.out.println("Unknown file type");
            System.out.println("It took" + " " + seconds + " " + "seconds");
        } else if ("%DOC-".equals(args[2]) && status) {
            System.out.println("DOC document");
            System.out.println("It took" + " " + seconds + " " + "seconds");
        } else if ("%DOC-".equals(args[2]) && status == false) {
            System.out.println("Unknown file type");
            System.out.println("It took" + " " + seconds + " " + "seconds");
        }

        scanner.close();


        inputFile.close();
    }
}
//------------------------------------------------------------------

//        Scanner scanner = new Scanner(inputFile);
//        while (scanner.hasNextLine()) {
//            check = scanner.nextLine();
//            if (check.contains("%PDF-")) {
//                isPDF = true;
//                break;
//            } else {
//                isPDF = false;
//            }
//            if (check.contains("%DOC-")) {
//                isDOC = true;
//                break;
//            } else {
//                isDOC = false;
//            }
//
//        }
//---------------------------------------------------------------------------------------

//        public class Main {
//            public static void main(String[] args) {
//                // write your code hereString
//                String text  = "abcadbabcdbbeeffdsdascbascbascbadcbac";
//                String pattern = "abc";
//                Scanner s = new Scanner(System.in);
//                while(true) {
//                    System.out.println("Enter Pattern");
//                    pattern = s.nextLine();
//                    if (pattern.equals("exit")) {
//                        break;
//                    }
//                    searchMethod(pattern, text);
//                }
//            }

//             void searchMethod(String pattern, String txt) {
//                int table[] = generatePrefixTable(pattern);
//                boolean status = false;
//                int txtPos = 0;
//                int patternPos = 0;
//                while (txtPos < txt.length()) {
//                    if (pattern.charAt(patternPos) == txt.charAt(txtPos)) {
//                        patternPos++;
//                        txtPos++;
//                    }
//                    if (patternPos == pattern.length()) {
//                        status = true;
//                        System.out.println("Pattern is found at index " + (txtPos - patternPos));
//                        return;
//                    } else if (pattern.charAt(patternPos) != txt.charAt(txtPos)) {
//                        status = false;
//                        if (patternPos != 0) {
//                            patternPos = table[patternPos - 1];
//                        } else {
//                            txtPos++;
//                        }
//                    }
//                    if (status == false) {
//                        System.out.println("\nPattern not found in text.\n");
//                    }
//                }
//            }
//        }

//---------------------------------------------------------------------------------------
//
//        long endtime = System.nanoTime();
//        long timeResult =  endtime - starttime;
//        double seconds = (double)timeResult / 1_000_000_000.0;
//        if ("%PDF-".equals(args[2]) && isPDF) {
//            System.out.println("PDF document");
//            System.out.println("It took" + " " + seconds + " " + "seconds");
//        } else if ("%PDF-".equals(args[2]) && isPDF == false){
//            System.out.println("Unknown file type");
//            System.out.println("It took" + " " + seconds + " " + "seconds");
//        } else if ("%DOC-".equals(args[2]) && isDOC) {
//            System.out.println("DOC document");
//            System.out.println("It took" + " " + seconds + " " + "seconds");
//        } else if ("%DOC-".equals(args[2]) && isPDF == false) {
//            System.out.println("Unknown file type");
//            System.out.println("It took" + " " + seconds + " " + "seconds");
//        }
//
//        scanner.close();
//
//
//
//        inputFile.close();
//
//    }
//}

//public class Main {
//    private static byte[] pdfSignature = "%PDF-".getBytes();
//
//        public static void main(String[] args) throws Exception {
//            if (args.length < 1) {
//            System.out.println("Please provide the input file");
//            System.exit(0);
//        } else {
//                if (args[0] == "--naive") {
//                    Search check = new Search(new Naive());
//                    check.search(args[1]);
//
//                }
//            }
//        InputStream inputFile = new FileInputStream(args[0]);
//        boolean isPDF = false;
//        boolean isDOC = false;
//        String check = "";
//
////        if ("%PDF-".equals(args[1]) && "PDF document".equals(args[2])) {
//
//            Scanner scanner = new Scanner(inputFile);
//            while (scanner.hasNextLine()) {
//                check = scanner.nextLine();
//                if (check.contains("%PDF-")) {
//                    isPDF = true;
//                    break;
//                } else {
//                    isPDF = false;
//                }
//                if (check.contains("%DOC-")) {
//                    isDOC = true;
//                    break;
//                } else {
//                    isDOC = false;
//                }
//
//            }
//            if ("%PDF-".equals(args[1]) && isPDF) {
//                System.out.println("PDF document");
//            } else if ("%PDF-".equals(args[1]) && isPDF == false){
//                System.out.println("Unknown file type");
//            } else if ("%DOC-".equals(args[1]) && isDOC) {
//                System.out.println("DOC document");
//            } else if ("%DOC-".equals(args[1]) && isPDF == false) {
//                System.out.println("Unknown file type");
//            }
//            scanner.close();
//
//
//
//            inputFile.close();

//
//    }
//}
//
