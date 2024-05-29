package hamburg.dbis.client;

import java.util.ArrayList;
import java.util.Iterator;

public class Schedule implements Iterable<Operation> {

    private final ArrayList<Operation> _schedule;

    @Override
    public Iterator iterator() {
        return _schedule.iterator();
    }

    private Schedule() {
        _schedule = new ArrayList<>();
    }

    public static Schedule createSchedule() {
        return new Schedule();
    }

    public Schedule addOperation(int page, String data) {
        _schedule.add(new Operation(page, data));
        return this;
    }
}
