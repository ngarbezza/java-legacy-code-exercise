package ar.edu.unq.sasa.model.departments;

import ar.edu.unq.sasa.model.requests.ClassroomRequest;
import ar.edu.unq.sasa.model.requests.Request;
import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.Booking;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.assignments.ResourceAssignment;
import ar.edu.unq.sasa.model.items.AssignableItem;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;

import java.util.*;
import java.util.Map.Entry;

public class AssignmentsDepartment extends Department {

    public AssignmentsDepartment(University university) {
        super(university);
    }

    public ResourceAssignment assignResourceAssignment(Request request, MobileResource mobileResource, Period period) {
        ResourceAssignment resourceAssignment = new ResourceAssignment(request, mobileResource);
        mobileResource.addAssignment(period, resourceAssignment);
        addAssignment(resourceAssignment);
        return resourceAssignment;
    }

    public Booking assignBooking(Classroom classroom, String cause, Period period) {
        Booking booking = new Booking(cause, classroom);
        classroom.addAssignment(period, booking);
        addAssignment(booking);
        updateAssignmentsSatisfactionSuperpositions(period, booking);
        changedAssignmentsAndRequests();
        return booking;
    }

    private void addAssignment(Assignment anAssignment) {
        getUniversity().addAssignment(anAssignment);
    }

    public ClassroomAssignment assignClassroomAssignment(
            ClassroomRequest classroomRequest, Classroom classroom, Period period) {
        ClassroomAssignment classroomAssignment = assignClassroomAssignmentWithoutSatisfaction(
                classroomRequest, classroom, period);
        classroomAssignment.createSatisfaction();
        updateAssignmentsSatisfactionSuperpositions(period, classroomAssignment);
        changedAssignmentsAndRequests();
        return classroomAssignment;
    }

    private ClassroomAssignment assignClassroomAssignmentWithoutSatisfaction(
            ClassroomRequest classroomRequest, Classroom classroom, Period period) {
        List<ResourceAssignment> resourcesAssignmentsList = new ArrayList<>();
        Set<Entry<Resource, Integer>> requiredResources = classroomRequest.getRequiredResources().entrySet();
        Set<Entry<Resource, Integer>> optionalResources = classroomRequest.getOptionalResources().entrySet();

        assignInFreeResources(requiredResources, classroomRequest, resourcesAssignmentsList, period);
        assignInFreeResources(optionalResources, classroomRequest, resourcesAssignmentsList, period);

        ClassroomAssignment classroomAssignment = new ClassroomAssignment(
                classroomRequest, classroom, resourcesAssignmentsList);
        classroom.addAssignment(period, classroomAssignment);
        addAssignment(classroomAssignment);

        return classroomAssignment;
    }

    private void updateAssignmentsSatisfactionSuperpositions(Period period, Assignment assignment) {
        for (Entry<Period, Assignment> entry : assignment.getAssignableItem().getAssignments().entrySet())
            if (!entry.getValue().equals(assignment) && entry.getKey().intersectsWith(period)
                    && entry.getValue().isClassroomAssignment()) {
                float minutesShared = entry.getKey().minutesSharedWithPeriod(period) / 60;
                ((ClassroomAssignment) entry.getValue()).getSatisfaction()
                        .addPeriodSuperposition(period, minutesShared);
            }
    }

    private void assignInFreeResources(Set<Entry<Resource, Integer>> resources, ClassroomRequest classroomRequest,
                                       List<ResourceAssignment> resourcesAssignmentsList, Period period) {
        for (Entry<Resource, Integer> entry : resources) {
            Integer resourceRequiredQuantity = entry.getValue();
            for (MobileResource resource : getUniversity().getMobileResources())
                if (resource.getName().equals(entry.getKey().getName()) && resourceRequiredQuantity > 0) {
                    Boolean isUsefulPeriod = true;
                    for (Period resourcePeriod : resource.getAssignments().keySet())
                        if (resourcePeriod.intersectsWith(period))
                            isUsefulPeriod = false;
                    if (isUsefulPeriod) {
                        resourcesAssignmentsList.add(assignResourceAssignment(classroomRequest, resource, period));
                        resourceRequiredQuantity--;
                    }
                }
        }
    }

    public ClassroomAssignment assignRequestInAClassroom(ClassroomRequest classroomRequest, Classroom classroom) {
        Period freePeriod = (Period) getPercentageAndPeriod(classroomRequest, classroom).get(1);
        ClassroomAssignment classroomAssignment = assignClassroomAssignment(classroomRequest, classroom, freePeriod);
        return classroomAssignment;
    }

    public ClassroomAssignment assignRequestInMostSatisfactoryClassroom(ClassroomRequest classroomRequest) {
        Classroom bestClassroom = null;
        Float bestRequiredPercentage = null;
        Float bestOptionalPercentage = null;
        int bestCapacityDifference = 0;
        int lowestMinutesSuperposed = 0;
        Period freePeriod = null;

        for (Classroom c : getClassroomsDepartment().getClassrooms()) {
            float cRequiredPercentage = getPercentage(classroomRequest.getRequiredResources(), c);
            float cOptionalPercentage = getPercentage(classroomRequest.getOptionalResources(), c);
            Map<Integer, Object> percentageAndPeriod = getPercentageAndPeriod(classroomRequest, c);
            int cMinutesSuperposed = (Integer) percentageAndPeriod.get(0);
            Period cFreePeriod = (Period) percentageAndPeriod.get(1);
            int cCapacityDifference = c.getCapacity() - classroomRequest.getCapacity();

            if (bestClassroom == null) {
                bestClassroom = c;
                bestRequiredPercentage = cRequiredPercentage;
                bestOptionalPercentage = cOptionalPercentage;
                bestCapacityDifference = cCapacityDifference;
                lowestMinutesSuperposed = cMinutesSuperposed;
                freePeriod = cFreePeriod;
            } else if (cMinutesSuperposed < lowestMinutesSuperposed) {
                bestClassroom = c;
                bestRequiredPercentage = cRequiredPercentage;
                bestOptionalPercentage = cOptionalPercentage;
                bestCapacityDifference = cCapacityDifference;
                lowestMinutesSuperposed = cMinutesSuperposed;
                freePeriod = cFreePeriod;
            } else if (cMinutesSuperposed == lowestMinutesSuperposed
                    && cRequiredPercentage > bestRequiredPercentage) {
                bestClassroom = c;
                bestRequiredPercentage = cRequiredPercentage;
                bestOptionalPercentage = cOptionalPercentage;
                bestCapacityDifference = cCapacityDifference;
                lowestMinutesSuperposed = cMinutesSuperposed;
                freePeriod = cFreePeriod;
            } else if (cRequiredPercentage == bestRequiredPercentage
                    && cCapacityDifference < bestCapacityDifference
                    && cCapacityDifference > 0) {
                bestClassroom = c;
                bestRequiredPercentage = cRequiredPercentage;
                bestOptionalPercentage = cOptionalPercentage;
                lowestMinutesSuperposed = cMinutesSuperposed;
                freePeriod = cFreePeriod;
            } else if (cCapacityDifference == bestCapacityDifference
                    && cOptionalPercentage > bestOptionalPercentage) {
                bestClassroom = c;
                bestRequiredPercentage = cRequiredPercentage;
                bestOptionalPercentage = cOptionalPercentage;
                lowestMinutesSuperposed = cMinutesSuperposed;
                freePeriod = cFreePeriod;
            }
        }

        return assignClassroomAssignment(classroomRequest, bestClassroom, freePeriod);
    }

    // TODO not supported for now - review
//    public ClassroomAssignment assignClassroomAssignmentWithDesiredPeriodAndRequiredResources(
//            ClassroomRequest classroomRequest, Classroom classroom, Period period, Map<Resource, Integer> resources) {
//        Map<Resource, Integer> oldRequiredResources = classroomRequest.getRequiredResources();
//        Map<Resource, Integer> oldOptionalResources = classroomRequest.getOptionalResources();
//        classroomRequest.setRequiredResources(resources);
//        classroomRequest.setOptionalResources(new HashMap<>());
//
//        ClassroomAssignment classroomAssignment = assignClassroomAssignmentWithoutSatisfaction(
//                classroomRequest, classroom, period);
//        classroomAssignment.getRequest().setRequiredResources(oldRequiredResources);
//        classroomAssignment.getRequest().setOptionalResources(oldOptionalResources);
//
//        classroomAssignment.createSatisfaction();
//        updateAssignmentsSatisfactionSuperpositions(period, classroomAssignment);
//        changedAssignmentsAndRequests();
//        return classroomAssignment;
//    }

    private Map<Integer, Object> getPercentageAndPeriod(ClassroomRequest classroomRequest, Classroom classroom) {
        Map<Integer, Object> percentageAndPeriods = new HashMap<>();
        Period bestPeriod = null;
        int minutesShared = -1;

        List<Period> periodDivisions = classroomRequest.getDesiredHours().convertToConcrete();

        boolean isUsefulPeriod;
        for (Period reqPeriod : periodDivisions) {
            if (bestPeriod == null) {
                bestPeriod = reqPeriod;
                minutesShared = searchForHighestSuperposedMinutes(reqPeriod, classroom);
                isUsefulPeriod = minutesShared <= 0;
            } else {
                int currentMinutesShared = searchForHighestSuperposedMinutes(reqPeriod, classroom);
                if (currentMinutesShared > 0) {
                    if (currentMinutesShared < minutesShared) {
                        bestPeriod = reqPeriod;
                        minutesShared = 0;
                        isUsefulPeriod = true;
                    } else
                        isUsefulPeriod = false;
                } else {
                    bestPeriod = reqPeriod;
                    minutesShared = 0;
                    isUsefulPeriod = true;
                }
            }

            if (isUsefulPeriod)
                break;
        }

        percentageAndPeriods.put(0, minutesShared);
        percentageAndPeriods.put(1, bestPeriod);

        return percentageAndPeriods;
    }

    private int searchForHighestSuperposedMinutes(Period period, Classroom classroom) {
        int highestMinutesSuperposed = 0;
        for (Period classroomPeriod : classroom.getAssignments().keySet()) {
            int minutesSharedWithPeriod = classroomPeriod.minutesSharedWithPeriod(period);
            if (minutesSharedWithPeriod > highestMinutesSuperposed)
                highestMinutesSuperposed = minutesSharedWithPeriod;
        }
        return highestMinutesSuperposed;
    }

    private float getPercentage(Map<Resource, Integer> requestResources, Classroom classroom) {
        int cantMatched = 0;
        int cantTotal = 0;

        for (Entry<Resource, Integer> entry : requestResources.entrySet()) {
            if (classroom.hasResource(entry.getKey().getName())) {
                int classAmount = classroom.getResource(entry.getKey().getName()).getAmount();
                cantMatched += Math.min(entry.getValue(), classAmount);
            }
            cantTotal += entry.getValue();
        }

        if (cantTotal == 0)
            return 1;
        else
            return cantMatched * 100 / cantTotal;
    }

    public void modifyBookedAssignmentCause(Booking searchedAssignment, String newCause) {
        searchedAssignment.setCause(newCause);
        changedAssignmentsAndRequests();
    }

    public void deleteResourceAssignment(ResourceAssignment resourceAssignment) {
        getUniversity().deleteAssignment(resourceAssignment);
        Map<Period, Assignment> resourceAssignments = resourceAssignment.getAssignableItem().getAssignments();
        for (Entry<Period, Assignment> entry : resourceAssignments.entrySet())
            if (entry.getValue().equals(resourceAssignment))
                resourceAssignment.getAssignableItem().removeAssignment(entry.getKey());
        changedAssignmentsAndRequests();
    }

    public void deleteClassroomAssignmentFromARequest(Request aRequest) {
        ClassroomAssignment classroomAssignment = null;
        for (Assignment assignment : getAssignments())
            if (assignment.isClassroomAssignment())
                if (assignment.getRequest().equals(aRequest)) {
                    classroomAssignment = (ClassroomAssignment) assignment;
                    deleteAssignment(assignment);
                    break;
                }

        // TODO find a potential NullPointerException bug
        for (ResourceAssignment resourceAssignment : classroomAssignment.getResourcesAssignments())
            deleteResourceAssignment(resourceAssignment);

        Map<Period, Assignment> classroomAssignments = classroomAssignment.getAssignableItem().getAssignments();
        for (Entry<Period, Assignment> entry : classroomAssignments.entrySet())
            if (entry.getValue().equals(classroomAssignment)) {
                classroomAssignment.getAssignableItem().removeAssignment(entry.getKey());
                break;
            }

        changedAssignmentsAndRequests();
    }

    public void deleteAssignment(Assignment searchedAssignment) {
        try {
            if (searchedAssignment.getRequest() != null)
                searchedAssignment.getRequest().setAssigned(false);
            AssignableItem assignableItem = searchedAssignment.getAssignableItem();
            Map<Period, Assignment> assignments = assignableItem.getAssignments();

            for (Entry<Period, Assignment> entry : assignments.entrySet())
                if (entry.getValue().equals(searchedAssignment)) {
                    assignableItem.removeAssignment(entry.getKey());
                    break;
                }
            getUniversity().deleteAssignment(searchedAssignment);

            changedAssignmentsAndRequests();
        } catch (ConcurrentModificationException e) {
            // TODO try to fix this, maybe it was cause by the way we are iterating some collection
            deleteAssignment(searchedAssignment);
        }
    }

    public Assignment searchForAssignment(AssignableItem assignableItem, Period period) {
        return assignableItem.getAssignments().get(period);
    }

    public List<Booking> searchBookings(String text) {
        List<Booking> bookings = new ArrayList<>();

        for (Assignment assignment : getUniversity().getAssignments())
            if (assignment instanceof Booking)
                if (((Booking) assignment).getCause().contains(text))
                    bookings.add((Booking) assignment);

        return bookings;
    }

    public ClassroomAssignment moveAssignmentOfClassroom(ClassroomAssignment assignment, Classroom classroom) {
        Period period = null;
        for (Entry<Period, Assignment> currentEntry : (assignment.getAssignableItem().getAssignments().entrySet()))
            if (currentEntry.getValue().equals(assignment)) {
                period = currentEntry.getKey();
                break;
            }
        ClassroomRequest request = assignment.getRequest();
        deleteClassroomAssignmentFromARequest(request);
        return assignClassroomAssignment(request, classroom, period);
    }

    public void moveAssignmentOfHour(ClassroomAssignment assignment, LogicalHourFulfiller hour) {
        Period period = null;
        for (Entry<Period, Assignment> currentEntry : (assignment.getAssignableItem().getAssignments().entrySet()))
            if (currentEntry.getValue().equals(assignment)) {
                period = currentEntry.getKey();
                break;
            }
        deleteClassroomAssignmentFromARequest(assignment.getRequest());
        // TODO find potential NullPointerException bug
        period.setHourFulfiller(hour);
        assignClassroomAssignment(assignment.getRequest(), assignment.getAssignableItem(), period);
    }

    public void moveAssignmentOfPeriod(ClassroomAssignment assignment, Period newPeriod) {
        deleteClassroomAssignmentFromARequest(assignment.getRequest());
        assignClassroomAssignment(assignment.getRequest(), assignment.getAssignableItem(), newPeriod);
    }

    private void changedAssignmentsAndRequests() {
        getPublisher().changed("assignmentsChanged", getAssignments());
        getPublisher().changed("requestsChanged", getRequestsDepartment().getRequests());
    }
}
