package hamburg.dbis.recovery;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import hamburg.dbis.persistence.PersistenceManager;

public class RecoveryManager {

    static final private RecoveryManager _manager;

    // TODO Add class variables if necessary
    private PersistenceManager _persistenceManager;
    private Map<Integer, Integer> transactionLSNs;


    static {
        try {
            _manager = new RecoveryManager();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private RecoveryManager() {
        // TODO Initialize class variables if necessary
        _persistenceManager = PersistenceManager.getInstance();
        transactionLSNs = new HashMap<>();
    }

    static public RecoveryManager getInstance() {
        return _manager;
    }

    public void startRecovery() {
        // TODO
        // Determine winner transactions
        determineWinnerTransactions();

        // Redo pending write operations and update LSNs in user data
        redoPendingWrites();
    }

    private void determineWinnerTransactions() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PersistenceManager.LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int lsn = Integer.parseInt(parts[0]);
                int taid = Integer.parseInt(parts[1]);
                transactionLSNs.put(taid, Math.max(transactionLSNs.getOrDefault(taid, 0), lsn));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Redo pending write operations and update LSNs in user data
    private void redoPendingWrites() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PersistenceManager.LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int lsn = Integer.parseInt(parts[0]);
                int transactionId = Integer.parseInt(parts[1]);
                if (lsn == transactionLSNs.getOrDefault(transactionId, 0)) {
                    if (parts.length > 3 && !parts[3].equals("EOT")) { // Check if parts has at least 4 elements
                        int pageid = Integer.parseInt(parts[2]);
                        String data = parts[3];
                        redoWriteOperation(transactionId, pageid, data);
                        updateLSNInUserData(pageid, lsn, data);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redoWriteOperation(int taid, int pageid, String data) {
        String filename = _persistenceManager.USER_DATA_DIR + "Page_" + pageid + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to redo write operation", e);
        }
    }

    private void updateLSNInUserData(int pageid, int lsn, String data) {
        String filename = _persistenceManager.USER_DATA_DIR + "Page_" + pageid + ".txt";
        try (RandomAccessFile raf = new RandomAccessFile(filename, "rw")) {
            long length = raf.length();
            if (length > 0) {
                raf.seek(length - data.length() - 1); // Go to the beginning of LSN
                raf.writeBytes(String.valueOf(lsn)); // Update LSN
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to update LSN in user data", e);
        }
    }
}