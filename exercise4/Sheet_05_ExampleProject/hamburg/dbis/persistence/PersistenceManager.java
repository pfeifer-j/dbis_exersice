package hamburg.dbis.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PersistenceManager {

    static final private PersistenceManager _manager;

    // TODO Add class variables if necessary
    public static final String LOG_FILE = "dbis\\exercise4\\Sheet_05_ExampleProject\\log.txt";
    public static final String USER_DATA_DIR = "dbis\\exercise4\\Sheet_05_ExampleProject\\user_data\\";

    private final AtomicInteger transactionCounter = new AtomicInteger(0);
    private final AtomicInteger logSequenceCounter = new AtomicInteger(0);
    private final Map<Integer, Map<Integer, String>> buffer = new ConcurrentHashMap<>();
    private final Map<Integer, List<Integer>> transactionPages = new ConcurrentHashMap<>();
    private final Set<Integer> committedTransactions = Collections.synchronizedSet(new HashSet<>());


    static {
        try {
            _manager = new PersistenceManager();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private PersistenceManager() {
        // TODO Get the last used transaction id from the log (if present) at startup
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int lsn = Integer.parseInt(parts[0]);
                int taid = Integer.parseInt(parts[1]);
                logSequenceCounter.set(Math.max(logSequenceCounter.get(), lsn));
                transactionCounter.set(Math.max(transactionCounter.get(), taid));
            }
        } catch (IOException e) {
            // Log file might not exist at the start
        }

        // TODO Initialize class variables if necessary

    }

    static public PersistenceManager getInstance() {
        return _manager;
    }

    public synchronized int beginTransaction() {
        // TODO return a valid transaction id to the client
        int newTransactionId = transactionCounter.incrementAndGet();
        buffer.put(newTransactionId, new ConcurrentHashMap<>());
        transactionPages.put(newTransactionId, Collections.synchronizedList(new ArrayList<>()));
        return newTransactionId;    
    }

    public void commit(int taid) {
        // TODO handle commits
        if (!buffer.containsKey(taid)) {
            throw new IllegalArgumentException("Transaction ID not found: " + taid);
        }

        // Write log entry for commit
        writeLogEntry(taid, 0, "EOT");

        // Write data to persistent storage
        Map<Integer, String> transactionData = buffer.get(taid);
        for (Map.Entry<Integer, String> entry : transactionData.entrySet()) {
            int pageId = entry.getKey();
            String data = entry.getValue();
            writeUserData(pageId, taid, data);
        }

        // Clean up
        committedTransactions.add(taid);
        buffer.remove(taid);
        transactionPages.remove(taid);
    }

    public void write(int taid, int pageid, String data) {
        // TODO handle writes of Transaction taid on page pageid with data
        if (!buffer.containsKey(taid)) {
            throw new IllegalArgumentException("Transaction ID not found: " + taid);
        }

        // Write log entry for write
        writeLogEntry(taid, pageid, data);

        // Update buffer
        buffer.get(taid).put(pageid, data);
        transactionPages.get(taid).add(pageid);

        // Check if buffer needs to be flushed
        if (buffer.size() > 5) {
            flushBuffer();
        }
    }

    private synchronized void writeLogEntry(int taid, int pageid, String data) {
        int lsn = logSequenceCounter.incrementAndGet();
        String logEntry = pageid == 0 ? lsn + ";" + taid + ";" + data : lsn + ";" + taid + ";" + pageid + ";" + data;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write log entry", e);
        }
    }

    private void writeUserData(int pageid, int taid, String data) {
        String filename = USER_DATA_DIR + "Page_" + pageid + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            int lsn = logSequenceCounter.get();
            writer.write(lsn + ";" + data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write user data", e);
        }
    }

    private synchronized void flushBuffer() {
        for (Integer taid : committedTransactions) {
            Map<Integer, String> transactionData = buffer.get(taid);
            if (transactionData != null) {
                for (Map.Entry<Integer, String> entry : transactionData.entrySet()) {
                    writeUserData(entry.getKey(), taid, entry.getValue());
                }
                buffer.remove(taid);
                transactionPages.remove(taid);
            }
        }
    }
}
