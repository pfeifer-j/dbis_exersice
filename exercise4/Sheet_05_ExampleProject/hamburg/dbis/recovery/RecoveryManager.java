package hamburg.dbis.recovery;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;  
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hamburg.dbis.persistence.PersistenceManager;

public class RecoveryManager {

    static final private RecoveryManager _manager;

    // TODO Add class variables if necessary
    private final Map<Integer, Integer> transactionLSNs;
    private final Set<Integer> winnerTransactions;

    static {
        try {
            _manager = new RecoveryManager();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private RecoveryManager() {
        transactionLSNs = new HashMap<>();
        winnerTransactions = new HashSet<>();
    }

    static public RecoveryManager getInstance() {
        return _manager;
    }

    public void startRecovery() {
        // TODO
        System.out.println("Starting Recovery...");
        clearUserData();

        // First, the so-called winner transactions have to b e determined from the log data. 
        determineWinnerTransactions();

        // After that, the pending write op erations have to be executed.
        // Remember updating the LSNs in the user data.
        if(winnerTransactions.size()>0){
            redoPendingWrites();
        }
        else {
            System.out.println("There was nothing to recover.");
        }
    }

    private void clearUserData() {
        System.out.println("Clearing user data...");
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(PersistenceManager.USER_DATA_DIR))) {
            for (Path path : directoryStream) {
                Files.delete(path);
                System.out.println("Removed: " + path.getFileName());
            }
        } catch (IOException e) {
            System.err.println("Failed to clear user data directory: " + e.getMessage());
        }
    }

    private void determineWinnerTransactions() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PersistenceManager.LOG_FILE))) {
            String line;
            int taid = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int lsn = Integer.parseInt(parts[0]);
                taid = Integer.parseInt(parts[1]);
                String data = parts[parts.length - 1];
                transactionLSNs.put(taid, Math.max(transactionLSNs.getOrDefault(taid, 0), lsn));

                if ("EOT".equals(data)) {
                    winnerTransactions.add(taid);
                }
            }
            System.out.println("Winning transaction: " + winnerTransactions.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redoPendingWrites() {
        System.out.println("Redoing pending writes...");

        try (BufferedReader reader = new BufferedReader(new FileReader(PersistenceManager.LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                // Read log
                int lsn = Integer.parseInt(parts[0]);
                int transactionId = Integer.parseInt(parts[1]);
                int pageid; // parts[2] = Integer.parseInt(parts[2]);
                String data; // parts[3];

                // Redo writes
                if (winnerTransactions.contains(transactionId)) {
                    if (parts.length > 3 && !"EOT".equals(parts[3])) {
                        data = parts[3];
                        pageid = Integer.parseInt(parts[2]);
                        String filename = PersistenceManager.USER_DATA_DIR + "Page_" + pageid + ".txt";
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                            writer.write(lsn + ";" + data);
                            System.out.println("Writing " + lsn + ";" + data + "in Page" + pageid + ".txt");
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to redo write operation", e);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}