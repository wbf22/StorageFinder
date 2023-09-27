package bf.storage.finder.core.objects;

import java.nio.file.Path;
import java.util.List;

/**
 * Object to store data about a directory or file
 *
 * @author brand
 */
public class FileData {

    private long size;
    private Path path;
    private int depth;
    private final List<FileData> immediateChildren;

    public FileData(long size, Path path, int depth, List<FileData> immediateChildren) {
        this.size = size;
        this.path = path;
        this.depth = depth;
        this.immediateChildren = immediateChildren;
    }

    public long getSize() {
        return size;
    }

    public Path getPath() {
        return path;
    }

    public int getDepth() {
        return depth;
    }

    public List<FileData> getImmediateChildren() {
        return immediateChildren;
    }



}