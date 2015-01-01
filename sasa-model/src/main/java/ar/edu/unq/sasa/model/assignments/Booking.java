package ar.edu.unq.sasa.model.assignments;

import ar.edu.unq.sasa.model.items.AssignableItem;
import ar.edu.unq.sasa.model.requests.Request;

/**
 * Reservas, es decir, asignaciones que no están asociadas a ningún pedido.
 */
public class Booking implements Assignment {

    private AssignableItem assignableItem;
    private String cause;

    public Booking(String aCause, AssignableItem anAssignableItem) {
        cause = aCause;
        assignableItem = anAssignableItem;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String aCause) {
        cause = aCause;
    }

    @Override
    public AssignableItem getAssignableItem() {
        return assignableItem;
    }

    @Override
    public Request getRequest() {
        // TODO is this required? is there a better solution?
        return null;
    }

    @Override
    public boolean isBookedAssignment() {
        return true;
    }

    @Override
    public boolean isClassroomAssignment() {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cause == null) ? 0 : cause.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Booking other = (Booking) obj;
        if (assignableItem == null) {
            if (other.assignableItem != null)
                return false;
        } else if (!assignableItem.equals(other.assignableItem))
            return false;
        if (cause == null) {
            if (other.cause != null)
                return false;
        } else if (!cause.equals(other.cause))
            return false;
        return true;
    }

    @Override
    public boolean isResourceAssignment() {
        return false;
    }
}
