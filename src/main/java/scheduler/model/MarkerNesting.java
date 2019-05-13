package scheduler.model;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.*;
import scheduler.solver.NestingDatesUpdater;

import java.time.LocalDateTime;

@PlanningEntity
public class MarkerNesting implements TaskChainLink {
    private Long id;
    private LocalDateTime startTime;
    private Marker marker;
    private VirtualMachine virtualMachine;

    private TaskChainLink previousTaskChainLink;
    private MarkerNesting nextMarkerNesting;

    public MarkerNesting() {
    }

    public MarkerNesting(Long id, Marker marker) {
        this.id = id;
        this.marker = marker;
    }

    @PlanningVariable(valueRangeProviderRefs = {"vms", "nestings"}, graphType = PlanningVariableGraphType.CHAINED)
    @Override
    public TaskChainLink getPreviousTaskChainLink() {
        return previousTaskChainLink;
    }

    public void setPreviousTaskChainLink(TaskChainLink previousTaskChainLink) {
        this.previousTaskChainLink = previousTaskChainLink;
    }

    @AnchorShadowVariable(sourceVariableName = "previousTaskChainLink")
    @Override
    public VirtualMachine getVirtualMachine() {
        return virtualMachine;
    }

    public void setVirtualMachine(VirtualMachine virtualMachine) {
        this.virtualMachine = virtualMachine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public MarkerNesting getNextMarkerNesting() {
        return nextMarkerNesting;
    }

    @Override
    public void setNextMarkerNesting(MarkerNesting nextMarkerNesting) {
        this.nextMarkerNesting = nextMarkerNesting;
    }

    @CustomShadowVariable(variableListenerClass = NestingDatesUpdater.class, sources = { @PlanningVariableReference(variableName = "previousTaskChainLink") })
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return getStartTime().plusSeconds(marker.getDuration());
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public String toString() {
        return "[Nesting(" + id + ")/" + marker + "||| time: " + getStartTime() + " -> " + getEndTime() + "]";
    }
}
