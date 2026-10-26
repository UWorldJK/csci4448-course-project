package ticketsystem.files;

public class FileStorageManager {
    private FileStorageManager instance;
    private final String filePath;
    private FileStorageManager(String filePath) {
        this.filePath = filePath;
    }

    public synchronized FileStorageManager getInstance(String filePath) {
        if (instance != null) {
            instance = new FileStorageManager(filePath);
        }
        return instance;
    }
}

