package scheduler.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VirtualMachine implements TaskChainLink {
    private Long id;
    private Pool belongsTo;
    private MarkerNesting nextMarkerNesting;
    private boolean started;
    private LocalDateTime endTime;

    public VirtualMachine() {
    }

    public VirtualMachine(Long id, boolean started) {
        this.id = id;
        this.started = started;
    }

    public List<MarkerNesting> getRunningTasks() {
        List<MarkerNesting> runningTasks = new ArrayList<>();
        MarkerNesting nesting = nextMarkerNesting;
        while (nesting != null) {
            runningTasks.add(nesting);
            nesting = nesting.getNextMarkerNesting();
        }
        return runningTasks;
    }

    public List<Slot> getScheduleSlots() {
        return io.vavr.collection.List.ofAll(getRunningTasks()).foldLeft(io.vavr.collection.List.of(Slot.oneHourSlot(getEndTime())), (acc, nesting) -> {
            // assign Nesting to a Slot
            Slot lastSlot = acc.last();
            if (lastSlot.getEndTime().isBefore(nesting.getEndTime())) { // isInSlot
                lastSlot.addTask(nesting);
                return acc;
            } else { // create a new slot, and assign nesting
                Slot newSlot = Slot.oneHourSlot(lastSlot.getEndTime());
                newSlot.addTask(nesting);
                return acc.append(newSlot);
            }
        }).asJava();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pool getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(Pool belongsTo) {
        this.belongsTo = belongsTo;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public VirtualMachine getVirtualMachine() {
        return this;
    }

    @Override
    public TaskChainLink getPreviousTaskChainLink() {
        return null;
    }

    @Override
    public MarkerNesting getNextMarkerNesting() {
        return nextMarkerNesting;
    }

    @Override
    public void setNextMarkerNesting(MarkerNesting nextMarkerNesting) {
        this.nextMarkerNesting = nextMarkerNesting;
    }

    @Override
    public String toString() {
        return "[VM " + id + "|" + endTime + "]";
    }

    public void setTime(LocalDateTime startOfTime) {
        endTime = startOfTime.plusMinutes(isStarted() ? 0 : 10);
    }
}
