package ar.edu.unq.sasa.model.assignments;

import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.requests.Request;

/**
 * {@link Assignment} utilizada para representar las asignaciones hechas en un
 * {@link MobileResource}.
 */
public class ResourceAssignment extends AssignmentByRequest {

    private MobileResource mobileResource;

    public ResourceAssignment(Request request, MobileResource aMobileResource) {
        super(request);
        mobileResource = aMobileResource;
    }

    @Override
    public Request getRequest() {
        return super.getRequest();
    }

    public MobileResource getAssignableItem() {
        return mobileResource;
    }

    @Override
    public boolean isBookedAssignment() {
        return false;
    }

    public boolean isClassroomAssignment() {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((mobileResource == null) ? 0 : mobileResource.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResourceAssignment other = (ResourceAssignment) obj;
        if (mobileResource == null) {
            if (other.mobileResource != null)
                return false;
        } else if (!mobileResource.equals(other.mobileResource))
            return false;
        return true;
    }

    @Override
    public boolean isResourceAssignment() {
        return true;
    }
}
