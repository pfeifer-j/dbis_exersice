package hamburg.dbis.utils;
import java.util.Objects;

public class LogEntry {
    private int transactionId;
    private int pageId;
    private String data;
    private int lsn;

    public LogEntry() {
    }

    public LogEntry(int transactionId, int pageId, String data, int lsn) {
        this.transactionId = transactionId;
        this.pageId = pageId;
        this.data = data;
        this.lsn = lsn;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getPageId() {
        return pageId;
    }

    public String getData() {
        return data;
    }

    public int getLsn() {
        return lsn;
    }
}
