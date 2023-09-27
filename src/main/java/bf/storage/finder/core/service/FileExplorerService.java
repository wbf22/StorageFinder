package bf.storage.finder.core.service;

import bf.storage.finder.core.objects.FileData;
import bf.storage.finder.ports.Ui;

import java.io.File;
import java.nio.file.Files;
import java.util.*;


/**
 * Class for exploring files and determining the size of each.
 * Updates a Ui progress bar during exploration
 * @author brand
 */
public class FileExplorerService {

    private final int indicatorDepth;
    private final int childrenAtIndicatorDepth;
    private int progress;
    private final Ui ui;


    /**
     * This service updates a progress bar through the Ui interface since the scan can take quite a long
     * time. During the 'exploreFile' method the service updates the progress bar whenever it scans a file
     * at a certain depth. The 'desiredIndicatorSize' is used to determine a depth for this with a certain number
     * of files. This helps the progress bar not be so choppy since different folder can have very different
     * depths.
     *
     * @param file file to start scan from
     * @param desiredIndicatorSize size explained above
     * @param ui ui interface to use for updating the progress bar
     */
    public FileExplorerService(File file, int desiredIndicatorSize, Ui ui) {
        int[] depthAndCount = getIndicatorDepth(file, desiredIndicatorSize);
        this.indicatorDepth = depthAndCount[0];
        this.childrenAtIndicatorDepth = depthAndCount[1];
        this.ui = ui;
    }


    /**
     * Recursive function that explores a directory collecting data on the size and depth of files and sub directories.
     * Updates the FileExplorer's UI progress bar during scan.
     * @return a list of FileData objects which contain children FileData objects (sub files and directories)
     */
    public List<FileData> exploreFile(File file, int depth) {
        List<FileData> allFiles = new ArrayList<>();

        // go through all the children and add to allFiles and immediateChildren
        File[] children = file.listFiles();
        children = (children == null)? new File[0] : children;
        List<FileData> immediateChildren = new ArrayList<>();
        for (File cf : children){
            try{
                if (Files.isDirectory(cf.toPath())) {
                    //recursive call
                    List<FileData> explored = exploreFile(cf, depth + 1);
                    allFiles.addAll(explored);
                    immediateChildren.add(explored.get(explored.size() - 1)); // the last element will be cf as added below
                } else {
                    //if it's not a directory add to allFiles and immediatechildren, and get the size
                    allFiles.add(
                        new FileData(Files.size(cf.toPath()), cf.toPath(), depth + 1, new ArrayList<>())
                    );
                    immediateChildren.add(
                        new FileData(Files.size(cf.toPath()), cf.toPath(), depth + 1, new ArrayList<>())
                    );
                }
            } catch(Exception e) {
                //TODO could keep track of the directories the scan could not access.
            }
        }

        //add a filedata for this file and to end of allFiles to be accessed after recursive call
        long size = immediateChildren.stream().mapToLong(FileData::getSize).sum();
        allFiles.add(
            new FileData(size, file.toPath(), depth, immediateChildren)
        );

        if (depth + 1 == indicatorDepth) {
            progress += immediateChildren.size();
            ui.setBar(
                (int) Math.ceil( (double)progress/childrenAtIndicatorDepth * 100 )
            );
        }

        return allFiles;
    }


    /**
     * Explores sub directories until it finds a depth where the number of sub directories and files is greater than
     * desiredIndicatorSize.
     * Meant to help with creating a progress bar.
     *
     * @param file directory to scan from
     * @param desiredIndicatorSize amount of files needed
     * @return int array with the first index being determined depth, and the second
     * the amount of files at the determined depth
     */
    private int[] getIndicatorDepth(File file, int desiredIndicatorSize) {
        int currentDepth = 0;
        File[] arr = file.listFiles();
        List<File> currentDepthChildren = (arr == null)? new ArrayList<>() : Arrays.stream(arr).sequential().toList();

        while(currentDepthChildren.size() < desiredIndicatorSize) {
            List<File> nextDepthChildren = new ArrayList<>();
            for (File f : currentDepthChildren) {
                if (Files.isDirectory(f.toPath())){
                    File[] files = f.listFiles();
                    if (files != null) {
                        nextDepthChildren.addAll(Arrays.asList(files));
                    }
                }

            }

            currentDepthChildren = nextDepthChildren;
            currentDepth++;
        }
//        System.out.println(currentDepth);
        return new int[]{currentDepth+1, currentDepthChildren.size()};
    }




}