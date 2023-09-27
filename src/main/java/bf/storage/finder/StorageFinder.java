package bf.storage.finder;

import bf.storage.finder.core.objects.FileData;
import bf.storage.finder.ports.Ui;
import bf.storage.finder.core.service.StorageFinderService;
import bf.storage.finder.core.service.FileExplorerService;
import bf.storage.finder.core.service.util.GbUtil;
import bf.storage.finder.external.TerminalUi;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class StorageFinder {

    public static void main(String[] args) {
        run();
    }


    /**
     * Method to control user flow through the terminal ui
     */
    private static void run() {
        Ui ui = new TerminalUi();

        String startDirectory = System.getProperty("user.home");

        String userInput = "n";
        if (startDirectory != null) {
            userInput = ui.getUserInput("Scan from home directory: '" + startDirectory + "'?(y/n) ");
        }

        if (userInput.equals("n")) {
            startDirectory = ui.getUserInput("Enter file path: ");
        }

        File file = new File(startDirectory);
        FileExplorerService explorer = new FileExplorerService(file, 10000, ui );

        ui.setBar(0);
        List<FileData> files = explorer.exploreFile(file, 0);

        ui.display("Done!");

        boolean done = false;

        while (!done) {
            String gbSize = ui.getUserInput("Enter Gb size: ");

            ui.display(
                getResultDisplay( GbUtil.getBytes(Float.parseFloat(gbSize)), files )
            );

            userInput = ui.getUserInput("\nRun again with different Gb size? (y/n)");
            if (userInput.equals("n")) {
                done = true;
            }
        }

    }


    /**
     * Creates a string to display the results of a scan.
     * @param gbSize size to filter folders and files for
     *
     * @return string to display
     */
    private static String getResultDisplay(long gbSize, List<FileData> files) {
        StorageFinderService storageFinderService = new StorageFinderService(files);

        String fileList = storageFinderService.getResults(gbSize).stream().map(
            f -> GbUtil.getGBAsString(f.getSize()) + f.getPath().toString()
        ).collect(Collectors.joining("\n"));

        return "Directories/Files over " + GbUtil.getGBAsString(gbSize) + ": \n" + fileList;
    }

}
