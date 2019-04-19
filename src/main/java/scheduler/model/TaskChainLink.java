package scheduler.model;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

import java.time.LocalDateTime;

@PlanningEntity
public interface TaskChainLink {

    // can be null
    VirtualMachine getVirtualMachine();

    @InverseRelationShadowVariable(sourceVariableName = "previousTaskChainLink")
    MarkerNesting getNextMarkerNesting();

    void setNextMarkerNesting(MarkerNesting nextMarkerNesting);


    LocalDateTime getEndTime();
}
