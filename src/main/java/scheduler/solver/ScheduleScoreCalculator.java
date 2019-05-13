package scheduler.solver;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;
import scheduler.model.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;

public class ScheduleScoreCalculator implements EasyScoreCalculator<Schedule> {

    @Override
    public Score calculateScore(Schedule schedule) {
       /* List<VirtualMachine> machines = schedule.getVirtualMachines();

        long notStartedMachinesUsedPenalty = machines.stream().mapToLong(m -> m.isStarted() ? 0 : -100).sum();

        long missedDeadlinePenalty = -50 * machines.stream().flatMap(m -> m.getRunningTasks().stream()).filter(n -> n.getEndTime().isAfter(n.getMarker().getMaxReturnTime())).count();

        List<Tenant> tenants = schedule.getMarkerNestings().stream().map(MarkerNesting::getMarker).map(Marker::getBelongsTo).map(MarkerList::getOwner).collect(toList());

        LocalDateTime endOfFirstSlot = schedule.getStartOfTime().plusHours(1);

        List<Slot> nextHourSlots = machines.stream().flatMap(m -> m.getScheduleSlots().stream()).filter(s -> s.getEndTime().isBefore(endOfFirstSlot)).collect(toList());

        long slaPenalty = tenants.stream()
                .flatMap(t -> nextHourSlots.stream().map(s -> Tuple.of(t, s.countForTenant(t))))
                .collect(groupingBy((Tuple2<Tenant, Long> t) -> t._1, summingLong((Tuple2<Tenant, Long> t) -> t._2)))
                .entrySet().stream()
                .mapToLong(e -> {
                    Tenant t = e.getKey();
                    long actualNumberOfMarkersNextHour = e.getValue();
                    long overRequirement = t.totalNestingOwned() - t.getNumberOfMarkersGuarantedPerHour();
                    return overRequirement > 0 ? t.getNumberOfMarkersGuarantedPerHour() - actualNumberOfMarkersNextHour : 0;
                }).sum() * -1;

        return HardSoftLongScore.of(slaPenalty, notStartedMachinesUsedPenalty + missedDeadlinePenalty);*/
       return computeUselessScore(schedule.getVirtualMachines());
    }

    private Score computeUselessScore(List<VirtualMachine> machines) {
        long softScore = 0L;
        long unusedMachines = machines.stream().map(VirtualMachine::getRunningTasks).map(List::size).filter(x -> x == 0).count();
        long hardScore = unusedMachines * -10;
        return HardSoftLongScore.of(hardScore, softScore);
    }
}
