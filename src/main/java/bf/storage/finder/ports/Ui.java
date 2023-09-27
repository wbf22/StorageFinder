package bf.storage.finder.ports;


/**
 * Interface used in StorageFinderService.java for the ui of the app.
 */
public interface Ui {

    String getUserInput(String prompt);

    void setBar(int percent);

    void reportError(String error);

    void display(String message);

}
