
package scheduler;

import io.vavr.collection.Stream;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import scheduler.model.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.toList;

public class App {

    public static void main(String[] args) {
        Schedule problem = createSchedule();

        SolverFactory<Schedule> solverFactory = SolverFactory.createFromXmlResource("scheduler/config.xml");

        System.out.println("#######" + problem);

        System.out.println("####### SOLVING #######");

        Solver<Schedule> solver = solverFactory.buildSolver();
        Schedule solution = solver.solve(problem);

        System.out.println("####### FINISHED SOLVING #######");

        System.out.println("####### best   =" + solution);
    }

    private static Schedule createSchedule() {
        Schedule problem = new Schedule();
        problem.setStartOfTime(LocalDateTime.now());
        Pool pool = new Pool();
        pool
                .addVirtualMachine(createMachine(1L, true, problem.getStartOfTime()))
                .addVirtualMachine(createMachine(2L, true, problem.getStartOfTime()))
                .addVirtualMachine(createMachine(3L, true, problem.getStartOfTime()))
                .addVirtualMachine(createMachine(4L, false, problem.getStartOfTime())); // TODO: weight = prefer started machines
        problem.setPools(List.of(pool));
        Tenant tenant1 = new Tenant("t-1", 10);
        Random random = new SecureRandom();
        Set<Long> ids = new TreeSet<>();

        List<MarkerNesting> nestings = Stream.range(0, 10).map(i -> createList("list" + i, random.nextInt(10) + 1, tenant1, 1)).flatMap(MarkerList::getMarkers).map(m -> {
            Long id = nextId(random, ids);
            return new MarkerNesting(id, m);
        }).collect(toList());

        problem.setMarkerNestings(nestings);

        return initializeSolution(problem);
    }

    private static MarkerList createList(String name, int nbMarkers, Tenant tenant, int delay) {
        Random timeRandom = new SecureRandom();
        MarkerList list = new MarkerList(name, tenant);
        Stream.range(0, nbMarkers).map(i -> new Marker("m" + i, (1 + timeRandom.nextInt(9)) * 60, LocalDateTime.now().plusHours(delay))).forEach(list::addMarker);
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
}
