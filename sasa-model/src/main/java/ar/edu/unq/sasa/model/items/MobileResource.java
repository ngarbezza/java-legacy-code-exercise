package ar.edu.unq.sasa.model.items;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.ResourceAssignment;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa aquellos recursos denominados "móviles" porque pueden ser asignados
 * al igual que las aulas.
 */
public class MobileResource extends AssignableItem implements Resource {

    private final int id;

    public MobileResource(String name, int anId) {
        super(name);
        id = anId;
    }

    @Override
    public void setName(String newName) {
        super.setName(newName);
    }

    @Override
    public boolean isFixedResource() {
        return false;
    }

    public int getId() {
        return id;
    }

    public List<ResourceAssignment> getResourceAssignments() {
        return getAssignments().values().stream()
                .filter(Assignment::isResourceAssignment)
                .map(a -> (ResourceAssignment) a)
                .collect(Collectors.toList());
    }
}
