package bf.storage.finder.external;

import bf.storage.finder.ports.Ui;

import java.util.Scanner;

import static bf.storage.finder.external.TerminalUtil.updateLastLine;

public class TerminalUi implements Ui {

    private int percentLoading;
    private boolean isLoading;

    @Override
    public String getUserInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }

    @Override
    public void setBar(int percent) {
        percentLoading = percent;
        isLoading = percent < 100;
        if (percent == 0) {
            System.out.println();
        }
        updateLastLine( getLoadingString(percent) );
    }

    @Override
    public void reportError(String error) {
        updateLastLine(error);
        if (isLoading) {
            System.out.println( getLoadingString(percentLoading) );
        }
    }

    @Override
    public void display(String message) {
        System.out.println();
        System.out.println(message);
    }


    private String getLoadingString(int percent) {
        return "Scanning Directories: [" + "=".repeat(percent) + ".".repeat(100-percent) + "] " + percent + "%";
    }
}
