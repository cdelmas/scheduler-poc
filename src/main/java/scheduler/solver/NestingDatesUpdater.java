package scheduler.solver;

import org.optaplanner.core.impl.domain.variable.listener.VariableListenerAdapter;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import scheduler.model.MarkerNesting;
import scheduler.model.TaskChainLink;
import scheduler.model.VirtualMachine;

import java.time.LocalDateTime;

public class NestingDatesUpdater extends VariableListenerAdapter<TaskChainLink> {

    @Override
    public void afterEntityAdded(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {
        System.out.println("##### afterEntityAdded -> " + taskChainLink);
        updateTime(scoreDirector, taskChainLink);
        System.out.println("##### afterEntityAdded (time updated) -> " + taskChainLink);
    }

    @Override
    public void afterVariableChanged(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {
        System.out.println("##### afterVariableChanged -> " + taskChainLink);
        updateTime(scoreDirector, taskChainLink);
        System.out.println("##### afterVariableChanged (time updated) -> " + taskChainLink);
    }

    private void updateTime(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {
        updateTimeAlgo(scoreDirector, taskChainLink);
    }

    private void updateTimeAlgo2(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {
        VirtualMachine machine = taskChainLink.getVirtualMachine();
        MarkerNesting current = machine.getNextMarkerNesting();
        while(current != null) {
            scoreDirector.beforeVariableChanged(current, "startTime");
            current.setStartTime(current.getPreviousTaskChainLink().getEndTime());
            scoreDirector.afterVariableChanged(current, "startTime");
            current = current.getNextMarkerNesting();
        }
    }

    private void updateTimeAlgo(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {
        if (taskChainLink instanceof MarkerNesting) {
            MarkerNesting current = (MarkerNesting) taskChainLink;
            while (current != null) {
                scoreDirector.beforeVariableChanged(current, "startTime");
                current.setStartTime(taskChainLink.getPreviousTaskChainLink().getEndTime());
                scoreDirector.afterVariableChanged(current, "startTime");
                current = current.getNextMarkerNesting();
            }
        } else { // anchor: VM
            updateTimeAlgo(scoreDirector, taskChainLink.getNextMarkerNesting());
        }
    }

}
