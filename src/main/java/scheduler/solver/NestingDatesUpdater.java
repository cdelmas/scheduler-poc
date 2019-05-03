package scheduler.solver;

import org.optaplanner.core.impl.domain.variable.listener.VariableListenerAdapter;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import scheduler.model.MarkerNesting;
import scheduler.model.TaskChainLink;

public class NestingDatesUpdater extends VariableListenerAdapter<TaskChainLink> {

    @Override
    public void afterEntityAdded(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {
        System.out.println("##### afterEntityAdded -> " + taskChainLink);
        updateTime(scoreDirector, taskChainLink);
        System.out.println("##### afterEntityAdded (time updated) -> " + taskChainLink);
    }

    @Override
    public void afterEntityRemoved(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {
        System.out.println("##### afterEntityRemoved -> " + taskChainLink);
        updateTime(scoreDirector, taskChainLink);
        System.out.println("##### afterEntityRemoved");
    }

    @Override
    public void afterVariableChanged(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {
        System.out.println("##### afterVariableChanged -> " + taskChainLink);
        updateTime(scoreDirector, taskChainLink);
        System.out.println("##### afterVariableChanged (time updated) -> " + taskChainLink);
    }

    private void updateTime(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {
        if (taskChainLink instanceof MarkerNesting) {
            TaskChainLink previous = taskChainLink.getPreviousTaskChainLink();
            MarkerNesting current = (MarkerNesting) taskChainLink;
            while (current != null) {
                scoreDirector.beforeVariableChanged(current, "startTime");
                scoreDirector.beforeVariableChanged(current, "endTime");
                current.setTime(previous.getEndTime());
                scoreDirector.afterVariableChanged(current, "startTime");
                scoreDirector.afterVariableChanged(current, "endTime");
                current = current.getNextMarkerNesting();
            }
        }
    }

}
