package ticketsystem.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ticketsystem.tickets.Ticket;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileStorageManager {
    private static FileStorageManager instance;
    private final ObjectMapper objectMapper;
    private final String filePath;
    private final String backupFileName = "ticket_backup.json";
    private List<Ticket> tickets;

    private FileStorageManager(String filePath, ObjectMapper objectMapper) {
        this.filePath = filePath;
        this.objectMapper = objectMapper;
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.tickets = new ArrayList<>();

        ensureTicketDirectoryAndFile();
        loadTickets();
    }

    public synchronized void saveTicket(Ticket ticket) throws IOException {
        tickets.removeIf(t -> t.equals(ticket));

        tickets.add(ticket);
        persistTickets();
    }

    private void persistTickets() throws IOException {
        File backupFile = new File(filePath + "/" + backupFileName);
        objectMapper.writeValue(backupFile, tickets);
    }

    public synchronized static FileStorageManager getInstance(String filePath, ObjectMapper objectMapper) {
        if (instance == null) {
            instance = new FileStorageManager(filePath, objectMapper);
        }
        return instance;
    }

    private void loadTickets() {
        try {
            File backupFile = new File(filePath + "/" + backupFileName);
            if (backupFile.exists() && backupFile.length() > 0) {
                tickets = objectMapper.readValue(
                        backupFile,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Ticket.class)
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tickets from backup", e);
        }
    }

    private void ensureTicketDirectoryAndFile() {
        try {
            File directory = new File(filePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File backupFile = new File(filePath + "/" + backupFileName);
            if (!backupFile.exists()) {
                backupFile.createNewFile();
                objectMapper.writeValue(backupFile, new ArrayList<>());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize FileStorageManager", e);
        }
    }
}

