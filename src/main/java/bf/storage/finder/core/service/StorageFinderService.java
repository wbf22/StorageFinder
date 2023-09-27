package bf.storage.finder.core.service;

import bf.storage.finder.core.objects.FileData;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for responding to user input and displaying results of scanning
 */
public class StorageFinderService {

    private List<FileData> files;


    public StorageFinderService(List<FileData> files) {
        this.files = files;
    }

    /**
     * Get the results of the scan. Iterates over a list of files and directories finding those that
     * are greater than the specified size.
     *
     * @param gbSize size to filter folders and files for
     * @return list of FileData objects that are greater than the specified size
     */
    public List<FileData> getResults(long gbSize) {
        List<FileData> listData = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            FileData f = files.get(i);
            if (f.getSize() >= gbSize) {
                boolean add = true;
                FileData toDelete = null;
                for (FileData o : listData) {
                    //check if 'f' is sub directory of other file but still over fileSizeTarget
                    if (f.getPath().toString().contains(o.getPath().toString())) {
                        toDelete = o;
                    }
                    //check if 'f' is parent directory of other file already in the list
                    if (o.getPath().toString().contains(f.getPath().toString())) {
                        add = false;
                    }
                }

                if (toDelete != null) {
                    listData.remove(toDelete);
                }

                if (add) {
                    listData.add(f);
                }

            }
        }
        return listData;
    }


}
