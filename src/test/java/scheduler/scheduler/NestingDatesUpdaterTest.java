package scheduler.scheduler;

import io.vavr.collection.Stream;
import org.junit.Test;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.score.constraint.Indictment;
import org.optaplanner.core.impl.domain.variable.descriptor.VariableDescriptor;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import scheduler.model.*;
import scheduler.solver.NestingDatesUpdater;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class NestingDatesUpdaterTest {

    @Test
    public void updateVm() {
        VirtualMachine machine = createFixture();
        assertThat(machine).satisfies(isValidTimeChain()); // initial problem is valid
        ScoreDirector scoreDirector = new NoOpScoreDirector();
        NestingDatesUpdater nestingDatesUpdater = new NestingDatesUpdater();

        nestingDatesUpdater.afterVariableChanged(scoreDirector, machine);
        assertThat(machine).satisfies(isValidTimeChain());
        nestingDatesUpdater.afterVariableChanged(scoreDirector, machine.getRunningTasks().get(3));
        assertThat(machine).satisfies(isValidTimeChain());
    }

    private Consumer<VirtualMachine> isValidTimeChain() {
        return vm -> {
            TaskChainLink prev = vm;
            MarkerNesting current = vm.getNextMarkerNesting();
            while(current != null) {
                assertThat(prev.getEndTime()).isEqualToIgnoringNanos(current.getStartTime());
                prev = current;
                current = current.getNextMarkerNesting();
            }
        };
    }

    private VirtualMachine createFixture() {
        Schedule problem = new Schedule();
        problem.setStartOfTime(LocalDateTime.now());
        Pool pool = new Pool();
        pool
                .addVirtualMachine(createMachine(1L, true, problem.getStartOfTime())); // TODO: weight = prefer started machines
        problem.setPools(List.of(pool));
        Tenant tenant1 = new Tenant("t-1", 10);
        Random random = new SecureRandom();
        Set<Long> ids = new TreeSet<>();

        List<MarkerNesting> nestings = Stream.range(0, 10).map(i -> createList("list" + i, 10, tenant1, 1)).flatMap(MarkerList::getMarkers).map(m -> {
            Long id = nextId(random, ids);
            return new MarkerNesting(id, m);
        }).collect(toList());

        problem.setMarkerNestings(nestings);

        Schedule schedule = initializeSolution(problem);
        return schedule.getVirtualMachines().get(0);
    }

    private static MarkerList createList(String name, int nbMarkers, Tenant tenant, int delay) {
        Random timeRandom = new SecureRandom();
        MarkerList list = new MarkerList(name, tenant);
        Stream.range(0, nbMarkers).map(i -> new Marker("m" + i, 10, LocalDateTime.now().plusHours(delay))).forEach(list::addMarker);
        return list;
    }

    private static VirtualMachine createMachine(long l, boolean b, LocalDateTime startOfTime) {
        VirtualMachine virtualMachine = new VirtualMachine(l, b);
        virtualMachine.setTime(startOfTime);
        return virtualMachine;
    }

    private static Long nextId(Random rng, Set<Long> alreadyUsed) {
        Long id;
        do {
            id = Math.abs(rng.nextLong()) % 1000;
        } while (alreadyUsed.contains(id));
        alreadyUsed.add(id);
        return id;
    }

    private static Schedule initializeSolution(Schedule problem) {
        VirtualMachine machine = problem.getPools().get(0).getMachines().get(0);
        List<MarkerNesting> markerNestings = problem.getMarkerNestings();
        markerNestings.get(0).setPreviousTaskChainLink(machine);
        markerNestings.get(0).setStartTime(machine.getEndTime());
        markerNestings.get(0).setVirtualMachine(machine);
        machine.setNextMarkerNesting(markerNestings.get(0));
        for (int i = 1; i < markerNestings.size(); i++) {
            MarkerNesting prev = markerNestings.get(i - 1);
            MarkerNesting current = markerNestings.get(i);
            current.setStartTime(prev.getEndTime());
            current.setVirtualMachine(machine);
            prev.setNextMarkerNesting(current);
            current.setPreviousTaskChainLink(prev);
        }
        return problem;
    }

    static class NoOpScoreDirector implements ScoreDirector {

        @Override
        public Object getWorkingSolution() {
            return null;
        }

        @Override
        public void setWorkingSolution(Object workingSolution) {
        }

        @Override
        public Score calculateScore() {
            return null;
        }

        @Override
        public boolean isConstraintMatchEnabled() {
            return false;
        }

        @Override
        public Collection<ConstraintMatchTotal> getConstraintMatchTotals() {
            return null;
        }

        @Override
        public Map<Object, Indictment> getIndictmentMap() {
            return null;
        }

        @Override
        public String explainScore() {
            return null;
        }

        @Override
        public void beforeEntityAdded(Object entity) {

        }

        @Override
        public void afterEntityAdded(Object entity) {

        }

        @Override
        public void beforeVariableChanged(Object entity, String variableName) {

        }

        @Override
        public void afterVariableChanged(Object entity, String variableName) {

        }

        @Override
        public void beforeVariableChanged(VariableDescriptor variableDescriptor, Object entity) {

        }

        @Override
        public void afterVariableChanged(VariableDescriptor variableDescriptor, Object entity) {

        }

        @Override
        public void changeVariableFacade(VariableDescriptor variableDescriptor, Object entity, Object newValue) {

        }

        @Override
        public void triggerVariableListeners() {

        }

        @Override
        public void beforeEntityRemoved(Object entity) {

        }

        @Override
        public void afterEntityRemoved(Object entity) {

        }

        @Override
        public void beforeProblemFactAdded(Object problemFact) {

        }

        @Override
        public void afterProblemFactAdded(Object problemFact) {

        }

        @Override
        public void beforeProblemPropertyChanged(Object problemFactOrEntity) {

        }

        @Override
        public void afterProblemPropertyChanged(Object problemFactOrEntity) {

        }

        @Override
        public void beforeProblemFactRemoved(Object problemFact) {

        }

        @Override
        public void afterProblemFactRemoved(Object problemFact) {

        }

        @Override
        public void close() {

        }

        @Override
        public Object lookUpWorkingObjectOrReturnNull(Object externalObject) {
            return null;
        }

        @Override
        public Object lookUpWorkingObject(Object externalObject) {
            return null;
        }
    }
}
