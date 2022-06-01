package blockchain;

import java.util.Scanner;

public class ConsoleHandler {
    public static int getInputNumber() {
        try(Scanner scanner = new Scanner(System.in)) {
            return scanner.nextInt();
        }
    }
}
