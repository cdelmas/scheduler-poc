package scheduler.solver;

import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import scheduler.model.TaskChainLink;

public class StartEndTimeTaskUpdate implements VariableListener<TaskChainLink> {
    // TODO: update start and endTime for the MarkerNesting chain (from the Machine to the end of the chain)

    @Override
    public void beforeEntityAdded(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {

    }

    @Override
    public void afterEntityAdded(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {

    }

    @Override
    public void beforeVariableChanged(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {

    }

    @Override
    public void afterVariableChanged(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {

    }

    @Override
    public void beforeEntityRemoved(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {

    }

    @Override
    public void afterEntityRemoved(ScoreDirector scoreDirector, TaskChainLink taskChainLink) {

    }

}
