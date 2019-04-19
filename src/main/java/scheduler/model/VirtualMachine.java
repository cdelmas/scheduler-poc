package scheduler.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VirtualMachine implements TaskChainLink {
    private Long id;
    private Pool belongsTo;
    private MarkerNesting nextMarkerNesting;
    private boolean started;

    public VirtualMachine() {
    }

    public VirtualMachine(Long id, boolean started) {
        this.id = id;
        this.started = started;
    }

    public List<MarkerNesting> getRunningTasks() {
        List<MarkerNesting> runningTasks = new ArrayList<>();
        MarkerNesting nesting = nextMarkerNesting;
        while(nesting != null) {
            runningTasks.add(nesting);
            nesting = nesting.getNextMarkerNesting();
        }
        return runningTasks;
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
        LocalDateTime now = LocalDateTime.now();
        return isStarted() ? now : now.plusMinutes(10);
    }

    @Override
    public VirtualMachine getVirtualMachine() {
        return this;
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
        return "[VM " + id + "]";
    }

}
