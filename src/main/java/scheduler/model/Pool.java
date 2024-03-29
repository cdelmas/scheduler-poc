package scheduler.model;


import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

import java.util.ArrayList;
import java.util.List;

@DeepPlanningClone
public class Pool {
    private List<VirtualMachine> machines = new ArrayList<>();

    public Pool addVirtualMachine(VirtualMachine machine) {
        machine.setBelongsTo(this);
        machines.add(machine);
        return this;
    }

    public List<VirtualMachine> getMachines() {
        return machines;
    }

    public void setMachines(List<VirtualMachine> machines) {
        this.machines = machines;
    }
}
