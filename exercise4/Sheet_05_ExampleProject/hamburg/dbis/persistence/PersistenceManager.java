package hamburg.dbis.persistence;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PersistenceManager {

    static final private PersistenceManager _manager;

    // TODO Add class variables if necessary
    public static final String LOG_FILE = "exercise4\\Sheet_05_ExampleProject\\log.txt";
    public static final String USER_DATA_DIR = "exercise4\\Sheet_05_ExampleProject\\user_data\\";

    private final AtomicInteger transactionCounter = new AtomicInteger(0);
    private final AtomicInteger logSequenceCounter = new AtomicInteger(0);
    private final Map<Integer, Map<Integer, LogData>> buffer = new ConcurrentHashMap<>();
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
        if (!buffer.containsKey(taid)) {
            throw new IllegalArgumentException("Transaction ID not found: " + taid);
        }

        // Write log entry for commit
        writeLogEntry(taid, 0, "EOT");

        // Write data to persistent storage
        Map<Integer, LogData> transactionData = buffer.get(taid);
        for (Map.Entry<Integer, LogData> entry : transactionData.entrySet()) {
            int pageId = entry.getKey();
            LogData logData = entry.getValue();
            writeUserData(pageId, logData);
        }

        // Clean up
        committedTransactions.add(taid);
        buffer.remove(taid);
        transactionPages.remove(taid);
    }

    public void write(int taid, int pageid, String data) {
        if (!buffer.containsKey(taid)) {
            throw new IllegalArgumentException("Transaction ID not found: " + taid);
        }

        // Write log entry for write and get the lsn
        int lsn = writeLogEntry(taid, pageid, data);

        // Update buffer
        buffer.get(taid).put(pageid, new LogData(lsn, data));
        transactionPages.get(taid).add(pageid);

        // Check if buffer needs to be flushed
        if (buffer.size() > 5) {
            flushBuffer();
        }
    }

    private synchronized int writeLogEntry(int taid, int pageid, String data) {
        int lsn = logSequenceCounter.incrementAndGet();
        String logEntry = pageid == 0 ? lsn + ";" + taid + ";" + data : lsn + ";" + taid + ";" + pageid + ";" + data;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write log entry", e);
        }
        return lsn;
    }

    private void writeUserData(int pageid, LogData logData) {
        String filename = USER_DATA_DIR + "Page_" + pageid + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(logData.lsn() + ";" + logData.data());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write user data", e);
        }
    }

    private synchronized void flushBuffer() {
        for (Integer taid : committedTransactions) {
            Map<Integer, LogData> transactionData = buffer.get(taid);
            if (transactionData != null) {
                for (Map.Entry<Integer, LogData> entry : transactionData.entrySet()) {
                    writeUserData(entry.getKey(), entry.getValue());
                }
                buffer.remove(taid);
                transactionPages.remove(taid);
            }
        }
    }
}
