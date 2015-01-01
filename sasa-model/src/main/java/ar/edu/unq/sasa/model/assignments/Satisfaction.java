package ar.edu.unq.sasa.model.assignments;

import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

import java.util.Map;

public class Satisfaction {

    // TODO get rid of maps an model the corresponding objects

    private Map<Resource, Integer> resources;

    private Map<Period, Float> timeDifference;

    private int capacityDifference;

    public Satisfaction(Map<Resource, Integer> resourcesMap, Map<Period, Float> aTimeDifference,
                        int aCapacityDifference) {
        resources = resourcesMap;
        timeDifference = aTimeDifference;
        capacityDifference = aCapacityDifference;
    }

    public Map<Resource, Integer> getResources() {
        return resources;
    }

    public Map<Period, Float> getTimeDifference() {
        return timeDifference;
    }

    public int getCapacityDifference() {
        return capacityDifference;
    }

    public boolean isSatisfied() {
        return resources.isEmpty() && capacityDifference >= 0 && timeDifference.isEmpty();
    }

    public void addPeriodSuperposition(Period period, float minutesShared) {
        timeDifference.put(period, minutesShared);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + capacityDifference;
        result = prime * result
                + ((resources == null) ? 0 : resources.hashCode());
        result = prime * result
                + ((timeDifference == null) ? 0 : timeDifference.hashCode());
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
        Satisfaction other = (Satisfaction) obj;
        if (capacityDifference != other.capacityDifference)
            return false;
        if (resources == null) {
            if (other.resources != null)
                return false;
        } else if (!resources.equals(other.resources))
            return false;
        if (timeDifference == null) {
            if (other.timeDifference != null)
                return false;
        } else if (!timeDifference.equals(other.timeDifference))
            return false;
        return true;
    }

}
