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

}
