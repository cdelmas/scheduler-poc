package scheduler.model;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@PlanningSolution
public class Schedule {

    private HardSoftLongScore score;
    private List<Pool> pools;
    private List<MarkerNesting> markerNestings;

    public Schedule() {
        System.out.println("####### new Schedule="+System.identityHashCode(this)); // TODO: DELETE
    }

    public List<Pool> getPools() {
        return pools;
    }

    public void setPools(List<Pool> pools) {
        this.pools = pools;
    }

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "vms")
    public List<VirtualMachine> getVirtualMachines() {
        return pools.stream().map(Pool::getMachines).flatMap(List::stream).collect(toList());
    }

    @PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "nestings")
    public List<MarkerNesting> getMarkerNestings() {
        return markerNestings;
    }

    public void setMarkerNestings(List<MarkerNesting> markerNestings) {
        this.markerNestings = markerNestings;
    }

    @PlanningScore
    public HardSoftLongScore getScore() {
        return score;
    }

    public void setScore(HardSoftLongScore score) {
        this.score = score;
    }


    @Override
    public String toString() {
        return "Solution: " +
                score +
                '\n' +
                getVirtualMachines().stream().map(Schedule::machineChainAsString).collect(joining("\n"));
    }

    private static StringBuilder machineChainAsString(VirtualMachine machine) {
        StringBuilder toStr = new StringBuilder(machine.toString());
        MarkerNesting nesting = machine.getNextMarkerNesting();
        while(nesting != null) {
            toStr.append("~~~>").append(nesting);
            nesting = nesting.getNextMarkerNesting();
        }
        return toStr;
    }
}
