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
        List<VirtualMachine> machines = schedule.getVirtualMachines();

        // test: make the score decrease if all machines are not used
        return computeUselessScore(machines);
    }

    private Score computeUselessScore(List<VirtualMachine> machines) {
        long softScore = 0L;
        long unusedMachines = machines.stream().map(VirtualMachine::getRunningTasks).map(List::size).filter(x -> x == 0).count();
        long hardScore = unusedMachines * -10;
        return HardSoftLongScore.of(hardScore, softScore);
    }
}
