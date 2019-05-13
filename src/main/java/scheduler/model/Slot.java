package scheduler.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Slot {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<MarkerNesting> tasks = new ArrayList<>();

    public static Slot oneHourSlot(LocalDateTime startTime) {
        return new Slot(startTime, 1);
    }

    public Slot(LocalDateTime startTime, int hours) {
        this(startTime, startTime.plusHours(hours));
    }

    private Slot(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void addTask(MarkerNesting task) {
        tasks.add(task);
    }

    public long countForTenant(Tenant tenant) {
        return tasks.stream().filter(tenant::owns).count();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
