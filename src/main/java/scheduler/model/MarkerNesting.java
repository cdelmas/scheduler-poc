package scheduler.model;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

import java.time.LocalDateTime;
import java.util.Objects;

@PlanningEntity // TODO: check whether a difficulty comparator is useful? See 4.3.3.2
public class MarkerNesting implements TaskChainLink {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Marker marker;
    private VirtualMachine virtualMachine;

    private TaskChainLink previousTaskChainLink;
    private MarkerNesting nextMarkerNesting;

    public MarkerNesting() {
        System.out.println("####### new Nesting="+System.identityHashCode(this)); // TODO: DELETE
    }

    public MarkerNesting(Long id, Marker marker) {
        this.id = id;
        this.marker = marker;
    }

    @PlanningVariable(valueRangeProviderRefs = {"vms", "nestings"}, graphType = PlanningVariableGraphType.CHAINED)
    public TaskChainLink getPreviousTaskChainLink() {
        return previousTaskChainLink;
    }

    public void setPreviousTaskChainLink(TaskChainLink previousTaskChainLink) {
        System.out.println("####### "+this+".setPreviousTaskChainLink("+previousTaskChainLink+")");
        this.previousTaskChainLink = previousTaskChainLink;
    }

    @AnchorShadowVariable(sourceVariableName = "previousTaskChainLink")
    @Override
    public VirtualMachine getVirtualMachine() {
        return virtualMachine;
    }

    public void setVirtualMachine(VirtualMachine virtualMachine) {
        System.out.println("####### "+this+".setVirtualMachine("+virtualMachine+")");
        this.virtualMachine = virtualMachine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        System.out.println("####### "+this+".setId("+id+")");
        this.id = id;
    }

    @Override
    public MarkerNesting getNextMarkerNesting() {
        return nextMarkerNesting;
    }

    @Override
    public void setNextMarkerNesting(MarkerNesting nextMarkerNesting) {
        System.out.println("####### "+this+".setNextMarkerNesting("+nextMarkerNesting+")");
        this.nextMarkerNesting = nextMarkerNesting;
    }

    // TODO: see StartTimeUpdatingVariableListener in Task Assigning example
    //@CustomShadowVariable(variableListenerClass = StartEndTimeTaskUpdate.class, sources = { @PlanningVariableReference(variableName = "previousTaskChainLink" })
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public String toString() {
        return "[Nesting(" + id + "/" + System.identityHashCode(this) + ")/" + marker + "]";
    }
}
