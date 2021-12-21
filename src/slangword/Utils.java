package slangword;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class Utils {
    private static String __dirname = System.getProperty("user.dir");
    public static String ORIGINAL_SW_FILE = __dirname + "\\data\\slang.txt";
    public static String MODIFIED_SW_FILE = __dirname + "\\data\\modified_slang.txt";
    private static HashMap<String, List<String>> wordList = new HashMap<String, List<String>>();
    private static TreeMap<String, String> historyLog = new TreeMap<String, String>();
    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static Scanner CONSOL_SCANNER = new Scanner(System.in);

    public static void loadDataFromFile(String inputFile) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(inputFile)));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains("`")) {
                    String[] content = line.split("`");
                    String word = content[0];
                    String[] values;
                    if (content[1].contains("|")) {
                        values = content[1].split("\\|");
                    } else {
                        values = new String[] { content[1] };
                    }
                    List<String> defList = Arrays.asList(values);
                    for (int i = 0; i < defList.size(); i++) {
                        String trimmedStr = defList.get(i);
                        defList.set(i, trimmedStr.trim());
                    }
                    wordList.put(word, defList);
                }
            }
        } catch (IOException ex) {
            System.out.println("\n\t\t\t*ERROR Caught while reading file*");
            ex.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                System.out.println("\n\t\t\t*ERROR Caught while closing file*");
                ex.printStackTrace();
            }
        }
    }

    private static void updateDataToFile() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(new File(MODIFIED_SW_FILE)));
            for (String item : wordList.keySet()) {
                bw.write(item + "`");
                List<String> defList = wordList.get(item);
                for (int i = 0; i < defList.size(); i++) {
                    bw.write(defList.get(i));
                    if (i + 1 < defList.size())
                        bw.write("| ");
                }
                bw.write("\n");
            }
        } catch (IOException ex) {
            System.out.println("\n\t\t\t*ERROR Caught while writing file*");
            ex.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ex) {
                System.out.println("\n\t\t\t*ERROR Caught while closing file*");
                ex.printStackTrace();
            }
        }
    }

    // Util 1
    public static void searchBySlangWord() {
        System.out.print("\n\t\t\t> Type word to find: ");
        String word = (CONSOL_SCANNER.nextLine()).toUpperCase();
        if (!wordList.containsKey(word)) {
            System.out.print("\n\t\t\t*No matching word was found*\n");
        } else {
            List<String> defList = wordList.get(word);
            System.out.print("\n\t\t\t> Definition: ");
            for (String item : defList) {
                System.out.print("\n\t\t\t\t+ " + item);
            }
            System.out.println();
        }
        String date = FORMATTER.format(new Date());
        historyLog.put(date, word);
    }

    // Util 2
    public static void searchByDefinition() {
        System.out.print("\n\t\t\t> Type to find Slang word: ");
        String def = CONSOL_SCANNER.nextLine();
        List<String> slangWordFoundList = new ArrayList<String>();
        for (String item : wordList.keySet()) {
            List<String> defList = wordList.get(item);
            for (String each : defList) {
                if (each.contains(def)) {
                    slangWordFoundList.add(item + "\t->\t" + defList);
                    break;
                }
            }
        }
        if (slangWordFoundList.isEmpty())
            System.out.println("\n\t\t\t*No Slang Word found*");
        else {
            System.out.println("\n\t\t\tList of Slang Word containing \'" + def + "\'");
            System.out.println("\n\t\t\tFound " + slangWordFoundList.size() + " results");
            for (String word : slangWordFoundList)
                System.out.print("\n\t\t\t+ " + word);
        }
    }

    // Util 3
    public static void showHistoryLog() {
        System.out.println("\n\t\tTime\t\t\tWord");
        for (String item : historyLog.keySet()) {
            System.out.println("\n\t\t" + item + "\t" + historyLog.get(item));
        }
    }

    // Util 4
    public static void addSlangWord() {
        System.out.print("\n\t\t\t> Type new slang word: ");
        String newWord = (CONSOL_SCANNER.nextLine()).toUpperCase();
        System.out.println("\n\t\t\t> Type new definitions");
        System.out.println("\n\t\t\t(Press \'q\' or \'Q\' to stop writing)");
        List<String> newDefList = new ArrayList<String>();
        do {
            System.out.print("\n\t\t\t");
            String input = CONSOL_SCANNER.nextLine();
            if (input.equals("q") || input.equals("Q"))
                break;
            if (input.length() != 0)
                newDefList.add(input);
        } while (true);
        if (wordList.containsKey(newWord)) {
            System.out.println("\n\t\t\t*Slang Word has already existed*");
            System.out.print("\n\t\t\tDo you want to overwrite(Y/N): ");
            String confirmOverwrite = CONSOL_SCANNER.nextLine();
            if (confirmOverwrite.equals("y") || confirmOverwrite.equals("Y")) {
                wordList.put(newWord, newDefList);
            } else {
                List<String> currentDefList = wordList.get(newWord);
                newDefList.addAll(newDefList.size(), currentDefList);
                newDefList = newDefList.stream().distinct().collect(Collectors.toList());
                wordList.put(newWord, newDefList);
            }
        } else {
            wordList.put(newWord, newDefList);
            System.out.println("\n\t\t\tSuccessfully added a new Slang Word");
        }
        updateDataToFile();
    }

    // Util 5
    public static void editSlangWord() {
        System.out.print("\n\t\t\t> Type Slang Word to edit: ");
        String word = (CONSOL_SCANNER.nextLine()).toUpperCase();
        if (!wordList.containsKey(word)) {
            System.out.println("\n\t\t\t*No matching word was found*");
            return;
        }

        System.out.println("\n\t\t      > LIST DEFINITIONS <");
        List<String> defList = wordList.get(word);
        List<String> newDefList = new ArrayList<String>();

        for (int i = 0; i < defList.size(); i++) {
            System.out.println("\n\t\t\t" + (i + 1) + ". " + defList.get(i));
        }

        System.out.println();
        System.out.println("\t\t--------------------------------------");
        System.out.println("\t\t|     > EDIT DEFINITION OPTIONS<     |");
        System.out.println("\t\t--------------------------------------");
        System.out.println("\t\t|         1. Add definition.         |");
        System.out.println("\t\t|         2. Update definition.      |");
        System.out.println("\t\t|         3. Delete definition.      |");
        System.out.println("\t\t--------------------------------------");
        System.out.print("\n\t\t\t> @Choose: ");
        String choice = CONSOL_SCANNER.nextLine();
        switch (choice) {
            case "1": {
                System.out.print("\n\t\t\t> Type new definition: ");
                String newDef = CONSOL_SCANNER.nextLine();
                boolean duplicateDef = false;
                for (String def : defList) {
                    if (def.equals(newDef)) {
                        duplicateDef = true;
                        break;
                    }
                }
                if (duplicateDef)
                    System.out.println("\n\t\t*New definition must be different from old definition*");
                else {
                    newDefList.addAll(defList);
                    newDefList.add(newDef);
                    wordList.put(word, newDefList);
                    System.out.println("\n\t\tSuccessfully added a new definition");
                }
                break;
            }
            case "2": {
                System.out.print("\n\t\tWhat number of definition you want to update: ");
                String number = CONSOL_SCANNER.nextLine();
                int order = Integer.parseInt(number);
                if (order > defList.size() || order <= 0) {
                    System.out.println("\n\t\t\t*Invalid index*");
                } else {
                    System.out.print("\n\t\t\t> Type new definition: ");
                    String newDef = CONSOL_SCANNER.nextLine();

                    boolean duplicateDef = false;
                    for (int i = 0; i < defList.size(); i++) {
                        if ((defList.get(i)).equals(newDef) && i != (order - 1)) {
                            duplicateDef = true;
                            break;
                        }
                    }

                    if ((defList.get(order - 1)).equals(newDef))
                        duplicateDef = true;

                    if (duplicateDef)
                        System.out.println("\n\t\t*New definition must be different from the existing definitions*");
                    else {
                        newDefList.addAll(defList);
                        newDefList.set(order - 1, newDef);
                        wordList.put(word, newDefList);
                        System.out.println("\n\t\t\tSuccessfully updated definition");
                    }
                }
                break;
            }
            case "3": {
                if (defList.size() == 1) {
                    System.out.println("\n\t\t\t*Can-not delete this definition!*");
                    System.out.println("\n\t\t\t*Slang Word must have at least one definition*");
                    break;
                }
                System.out.print("\n\t\tWhat number of definition you want to update: ");
                String number = CONSOL_SCANNER.nextLine();
                int order = Integer.parseInt(number);
                if (order > defList.size() || order <= 0) {
                    System.out.println("\n\t\t\t*Invalid index*");
                } else {
                    newDefList.addAll(defList);
                    newDefList.remove(order - 1);
                    wordList.put(word, newDefList);
                    System.out.println("\n\t\t\tSuccessfully deleted definition");
                }
                break;
            }
            default:
                System.out.println("\n\t\t\t*Invalid Input*");
                break;
        }
        updateDataToFile();
    }

    // Util 6
    public static void deleteSlangWord() {
        System.out.print("\n\t\t\t> Type new slang word: ");
        String word = (CONSOL_SCANNER.nextLine()).toUpperCase();
        if (word.length() != 0) {
            if (wordList.containsKey(word)) {
                System.out.print("\n\t\t\tDo you want to delete(Y/N): ");
                String confirm = CONSOL_SCANNER.nextLine();
                if (confirm.equals("y") || confirm.equals("Y")) {
                    wordList.remove(word);
                    System.out.print("\n\t\t\tSuccessfully deleted word " + word);
                    updateDataToFile();
                } else {
                    System.out.print("\n\t\t\t*Canceled process*");
                }
            } else {
                System.out.print("\n\t\t\t*No matching word was found*");
            }
        } else {
            System.out.print("\n\t\t\t*Please enter a word*");
        }
    }

    // Util 7
    public static void resetDictionary() {
        wordList.clear();
        loadDataFromFile(ORIGINAL_SW_FILE);
        System.out.print("\n\t\t\tSuccessfully reset to default dictionary");
    }

    // Util 8
    private static String randomSlangWord() {
        Set<String> keySet = wordList.keySet();
        List<String> keyList = new ArrayList<>(keySet);

        int size = keyList.size();
        int randomIdx = new Random().nextInt(size);

        String randomKey = keyList.get(randomIdx);
        return randomKey;
    }

    public static void printRandomSlangWord() {
        String keyWord = randomSlangWord();
        List<String> defList = wordList.get(keyWord);
        System.out.println("\n\t\t\t> A Random Slang Word: " + keyWord);
        System.out.print("\n\t\t\t> Definition:");
        for (String item : defList) {
            System.out.println("\n\t\t\t\t+ " + item);
        }
    }
}
