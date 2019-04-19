package scheduler.model;

import java.time.LocalDateTime;

public class Marker {
    private String name;
    private int duration;
    private LocalDateTime maxReturnTime;
    private MarkerList belongsTo;

    public Marker(String name, int duration, LocalDateTime maxReturnTime) {
        this.name = name;
        this.duration = duration;
        this.maxReturnTime = maxReturnTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getMaxReturnTime() {
        return maxReturnTime;
    }

    public void setMaxReturnTime(LocalDateTime maxReturnTime) {
        this.maxReturnTime = maxReturnTime;
    }

    public MarkerList getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(MarkerList belongsTo) {
        this.belongsTo = belongsTo;
    }

    @Override
    public String toString() {
        return "[Marker " + name + "/" + duration + "s]";
    }
}
