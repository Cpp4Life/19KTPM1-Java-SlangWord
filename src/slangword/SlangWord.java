package slangword;

import java.io.IOException;
import java.util.Scanner;

public class SlangWord {
    private static Scanner CONSOL_SCANNER = new Scanner(System.in);

    private static void showMenu() {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("|               > MENU OF SLANG WORD DICTIONARY <               |");
        System.out.println("-----------------------------------------------------------------");
        System.out.println("|                  1. Search by Slang Word                      |");
        System.out.println("|                  2. Search by definition                      |");
        System.out.println("|                  3. Show history logs                         |");
        System.out.println("|                  4. Add a new Slang Word                      |");
        System.out.println("|                  5. Edit a Slang Word                         |");
        System.out.println("|                  6. Delete a Slang Word                       |");
        System.out.println("|                  7. Reset to default dictionary               |");
        System.out.println("|                  8. Random a Slang Word                       |");
        System.out.println("|                  9. Quiz game: Find definition of Slang Word  |");
        System.out.println("|                  10. Quiz game: Find Slang Word by definition |");
        System.out.println("|                  11. Clear history                            |");
        System.out.println("|                  12. Quit                                     |");
        System.out.println("-----------------------------------------------------------------");
    }

    public static void cls() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void Read() {
        System.out.print("\n\n\t\t\tPress Enter to continue . . . ");
        CONSOL_SCANNER.nextLine();
    }

    public static void main(String[] args) {
        String choice = null;
        Utils.loadDataFromFile(Utils.MODIFIED_SW_FILE);
        while (true) {
            cls();
            showMenu();
            System.out.print("\n\t\t\t> @Choose: ");
            choice = CONSOL_SCANNER.nextLine();
            switch (choice) {
                case "1":
                    Utils.searchBySlangWord();
                    break;
                case "2":
                    Utils.searchByDefinition();
                    break;
                case "3":
                    Utils.showHistoryLog();
                    break;
                case "4":
                    Utils.addSlangWord();
                    break;
                case "5":
                    Utils.editSlangWord();
                    break;
                case "6":
                    Utils.deleteSlangWord();
                    break;
                case "7":
                    Utils.resetDictionary();
                    break;
                case "8":
                    Utils.printRandomSlangWord();
                    break;
                case "9":
                    Utils.quizFindDefinition();
                    break;
                case "10":
                    Utils.quizFindSlangWord();
                    break;
                case "11":
                    Utils.clearHistory();
                    break;
                case "12":
                    Utils.quit();
                default:
                    System.out.println("\n\t\t\t*Invalid Input*");
                    break;
            }
            Read();
        }
    }
}
