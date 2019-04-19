package scheduler.solver;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;
import scheduler.model.Schedule;
import scheduler.model.VirtualMachine;

import java.util.List;

public class ScheduleScoreCalculator implements EasyScoreCalculator<Schedule> {

    @Override
    public Score calculateScore(Schedule schedule) {
        System.out.println("####### computing score on " + schedule.hashCode());
        long hardScore = 0L;
        long softScore = 0L;

        List<VirtualMachine> machines = schedule.getVirtualMachines();
        // test: make the score decrease if all machines are not used
        long unusedMachines = machines.stream().map(VirtualMachine::getRunningTasks).map(List::size).filter(x -> x == 0).count();
        hardScore = unusedMachines * -10;

        // TODO implement, following the defined constraints

        return HardSoftLongScore.of(hardScore, softScore);
    }
}
