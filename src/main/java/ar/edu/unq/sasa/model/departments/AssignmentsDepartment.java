package ar.edu.unq.sasa.model.departments;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.assignments.ResourceAssignment;
import ar.edu.unq.sasa.model.items.AssignableItem;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;

public class AssignmentsDepartment extends Department {

	public AssignmentsDepartment(University university) {
		super(university);
	}

	public ResourceAssignment asignateResourceAssignment(Request request, MobileResource mobileResource, Period period) {
		ResourceAssignment resourceAssignment = new ResourceAssignment(request, mobileResource);
		mobileResource.addAssignment(period, resourceAssignment);
		addAssignment(resourceAssignment);
		return resourceAssignment;
	}

	public BookedAssignment asignateBookedAssignment(Classroom classroom, String cause, Period period) {
		BookedAssignment bookedAssignment = new BookedAssignment(cause, classroom);
		classroom.addAssignment(period, bookedAssignment);
		addAssignment(bookedAssignment);
		updateAssignmentsSatisfactionSuperpositions(period, bookedAssignment);
		///////////////////////////////////////////////////////////
		this.getPublisher().changed("assignmentsChanged", this.getAssignments());
		this.getPublisher().changed("requestsChanged", this.getRequests());
		///////////////////////////////////////////////////////////
		return bookedAssignment;
	}

	private void addAssignment(Assignment assignment) {
		getUniversity().addAssignment(assignment);
	}

	public ClassroomAssignment asignateClassroomAssignment(ClassroomRequest classroomRequest, Classroom classroom, Period period) {
		ClassroomAssignment classroomAssignment = asignateClassroomAssignmentWithoutSatisfaction(classroomRequest, classroom, period);
		classroomAssignment.createSatisfaction();
		updateAssignmentsSatisfactionSuperpositions(period, classroomAssignment);
		///////////////////////////////////////////////////////////
		this.getPublisher().changed("assignmentsChanged", this.getAssignments());
		this.getPublisher().changed("requestsChanged", this.getRequests());
		///////////////////////////////////////////////////////////
		return classroomAssignment;
	}

	private ClassroomAssignment asignateClassroomAssignmentWithoutSatisfaction(ClassroomRequest classroomRequest, Classroom classroom, Period period) {
		List<ResourceAssignment> resourcesAssignmentsList = new ArrayList<ResourceAssignment>();
		Set<Entry<Resource, Integer>> requiredResources = classroomRequest.getRequiredResources().entrySet();
		Set<Entry<Resource, Integer>> optionalResources = classroomRequest.getOptionalResources().entrySet();

		asignateInFreeResources(requiredResources, classroomRequest, resourcesAssignmentsList, period);
		asignateInFreeResources(optionalResources, classroomRequest, resourcesAssignmentsList, period);

		ClassroomAssignment classroomAssignment = new ClassroomAssignment(classroomRequest, classroom, resourcesAssignmentsList);
		classroom.addAssignment(period, classroomAssignment);
		addAssignment(classroomAssignment);

		return classroomAssignment;
	}

	private void updateAssignmentsSatisfactionSuperpositions(Period period, Assignment assignment) {
		for (Entry<Period, Assignment> entry : assignment.getAssignableItem().getAssignments().entrySet())
			if (! entry.getValue().equals(assignment))
				if (entry.getKey().intersectsWith(period))
					if (entry.getValue().isClassroomAssignment()) {
						float minutesShared = entry.getKey().minutesSharedWithPeriod(period) / 60;
						((ClassroomAssignment) entry.getValue()).getSatisfaction().addPeriodSuperposition(period, minutesShared);
					}
	}

	private void asignateInFreeResources(Set<Entry<Resource, Integer>> resources, ClassroomRequest classroomRequest,
			List<ResourceAssignment> resourcesAssignmentsList, Period period) {
		for (Entry<Resource, Integer> entry : resources) {
			Integer cant = entry.getValue();
			for (MobileResource resource : getUniversity().getMobileResources())
				if (resource.getName().equals(entry.getKey().getName()) && cant > 0) {
					boolean isUsefullPeriod = true;
					for (Period resourcePeriod : resource.getAssignments().keySet())
						if (resourcePeriod.intersectsWith(period))
							isUsefullPeriod = false;
					if (isUsefullPeriod) {
						ResourceAssignment asig = asignateResourceAssignment(classroomRequest, resource, period);
						resourcesAssignmentsList.add(asig);
						cant--;
					}
				}
		}
	}

	public ClassroomAssignment asignateRequestInAClassroom(ClassroomRequest classroomRequest, Classroom classroom) {
		Period freePeriod = (Period) getPercentageAndPeriod(classroomRequest, classroom).get(1);
		ClassroomAssignment classroomAssignment = asignateClassroomAssignment(classroomRequest, classroom, freePeriod);
		return classroomAssignment;
	}

	public ClassroomAssignment asignateRequestInMostSatisfactoryClassroom(ClassroomRequest classroomRequest) {
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

		return asignateClassroomAssignment(classroomRequest, bestClassroom, freePeriod);
	}

	public ClassroomAssignment asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources(
			ClassroomRequest classroomRequest, Classroom classroom, Period period, Map<Resource, Integer> resources) {
		Map<Resource, Integer> oldRequiredResources = classroomRequest.getRequiredResources();
		Map<Resource, Integer> oldOptionalResources = classroomRequest.getOptionalResources();
		classroomRequest.setRequiredResources(resources);
		classroomRequest.setOptionalResources(new HashMap<Resource, Integer>());

		ClassroomAssignment classroomAssignment = asignateClassroomAssignmentWithoutSatisfaction(
				classroomRequest, classroom, period);
		classroomAssignment.getRequest().setRequiredResources(oldRequiredResources);
		classroomAssignment.getRequest().setOptionalResources(oldOptionalResources);

		classroomAssignment.createSatisfaction();
		updateAssignmentsSatisfactionSuperpositions(period, classroomAssignment);
		///////////////////////////////////////////////////////////
		this.getPublisher().changed("assignmentsChanged", this.getAssignments());
		this.getPublisher().changed("requestsChanged", this.getRequests());
		///////////////////////////////////////////////////////////
		return classroomAssignment;
	}

	private Map<Integer, Object> getPercentageAndPeriod(ClassroomRequest classroomRequest, Classroom classroom) {
		Map<Integer, Object> percentageAndPeriods = new HashMap<Integer, Object>();
		Period bestPeriod = null;
		int minutesShared = -1;

		List<Period> periodDivisions = classroomRequest.getDesiredHours().convertToConcrete();

		boolean isUsefullPeriod = true;
		for (Period reqPeriod : periodDivisions) {
			if (bestPeriod == null) {
				bestPeriod = reqPeriod;
				minutesShared = searchForHighestSuperposedMinutes(reqPeriod, classroom);
				if (minutesShared > 0)
					isUsefullPeriod = false;
				else
					isUsefullPeriod = true;
			} else {
				int currentMinutesShared = searchForHighestSuperposedMinutes(reqPeriod, classroom);
				if (currentMinutesShared > 0) {
					if (currentMinutesShared < minutesShared) {
						bestPeriod = reqPeriod;
						minutesShared = 0;
						isUsefullPeriod = true;
					} else
						isUsefullPeriod = false;
				} else {
					bestPeriod = reqPeriod;
					minutesShared = 0;
					isUsefullPeriod = true;
				}
			}

			if (isUsefullPeriod)
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
				if (entry.getValue() > classAmount)
					cantMatched += classAmount;
				else
					cantMatched += entry.getValue();
			}
			cantTotal += entry.getValue();
		}

		if (cantTotal == 0)
			return 1;
		else
			return cantMatched * 100 / cantTotal;
	}

	public void modifyBookedAssignmentCause(BookedAssignment searchedAssignment, String newCause) {
		searchedAssignment.setCause(newCause);
		///////////////////////////////////////////////////////////
		this.getPublisher().changed("assignmentsChanged", this.getAssignments());
		this.getPublisher().changed("requestsChanged", this.getRequests());
		///////////////////////////////////////////////////////////
	}

	public void deleteResourceAssignment(ResourceAssignment resourceAssignment) {
		getUniversity().deleteAssignment(resourceAssignment);
		Map<Period, Assignment> resourceAssignments = resourceAssignment.getAssignableItem().getAssignments();
		for (Entry<Period, Assignment> entry : resourceAssignments.entrySet())
			if (entry.getValue().equals(resourceAssignment))
				resourceAssignment.getAssignableItem().removeAssignment(entry.getKey());
		///////////////////////////////////////////////////////////
		this.getPublisher().changed("assignmentsChanged", this.getAssignments());
		this.getPublisher().changed("requestsChanged", this.getRequests());
		///////////////////////////////////////////////////////////
	}

	public void deleteClassroomAssignmentFromARequest(Request request) {
		ClassroomAssignment classroomAssignment = null;
		for (Assignment assignment : getUniversity().getAssignments())
			if (assignment.isClassroomAssignment())
				if (assignment.getRequest().equals(request)) {
					classroomAssignment = (ClassroomAssignment) assignment;
					deleteAssignment(assignment);
					break;
				}

		for (ResourceAssignment resourceAssignment : classroomAssignment.getResourcesAssignments())
			deleteResourceAssignment(resourceAssignment);

		Map<Period, Assignment> classroomAssignments = classroomAssignment.getAssignableItem().getAssignments();
		for (Entry<Period, Assignment> entry : classroomAssignments.entrySet())
			if (entry.getValue().equals(classroomAssignment)) {
				classroomAssignment.getAssignableItem().removeAssignment(entry.getKey());
				break;
			}

		///////////////////////////////////////////////////////////
		this.getPublisher().changed("assignmentsChanged", this.getAssignments());
		this.getPublisher().changed("requestsChanged", this.getRequests());
		///////////////////////////////////////////////////////////
	}

	public void deleteAssignment(Assignment searchedAssignment) {
		try {
			if (searchedAssignment.getRequest() != null)
				searchedAssignment.getRequest().setAsignated(false);
			AssignableItem assignableItem = searchedAssignment.getAssignableItem();
			Map<Period, Assignment> assignments = assignableItem.getAssignments();

			for (Entry<Period, Assignment> entry : assignments.entrySet())
				if (entry.getValue().equals(searchedAssignment)) {
					assignableItem.removeAssignment(entry.getKey());
					break;
				}
			getUniversity().deleteAssignment(searchedAssignment);

			///////////////////////////////////////////////////////////
			this.getPublisher().changed("assignmentsChanged", this.getAssignments());
			this.getPublisher().changed("requestsChanged", this.getRequests());
			///////////////////////////////////////////////////////////
		} catch (ConcurrentModificationException e) {
			// TODO try to fix this, maybe it was cause by the way we are iterating some collection
			deleteAssignment(searchedAssignment);
		}
	}

	public Assignment searchForAssignment(AssignableItem assignableItem, Period period) {
		return assignableItem.getAssignments().get(period);
	}

	public List<BookedAssignment> searchForBookedAssignment(String text) {
		List<BookedAssignment> bookedAssignments = new ArrayList<BookedAssignment>();

		for (Assignment assignment : getUniversity().getAssignments())
			if (assignment instanceof BookedAssignment)
				if (((BookedAssignment) assignment).getCause().contains(text))
					bookedAssignments.add((BookedAssignment) assignment);

		return bookedAssignments;
	}

	public ClassroomAssignment moveAssignmentOfClassroom(ClassroomAssignment assigment, Classroom classroom) {
		Period period = null;
		for (Entry<Period, Assignment> currentEntry : (assigment.getAssignableItem().getAssignments().entrySet()))
			if (currentEntry.getValue().equals(assigment)) {
				period = currentEntry.getKey();
				break;
			}
		ClassroomRequest request = assigment.getRequest();
		this.deleteClassroomAssignmentFromARequest(request);
		return this.asignateClassroomAssignment(request, classroom, period);
	}

    public void moveAssignmentOfHour(ClassroomAssignment assignment, LogicalHourFulfiller hour) {
		Period period = null;
		for (Entry<Period, Assignment> currentEntry : (assignment.getAssignableItem().getAssignments().entrySet()))
			if (currentEntry.getValue().equals(assignment)) {
				period = currentEntry.getKey();
				break;
			}
		deleteClassroomAssignmentFromARequest(assignment.getRequest());
		period.setHourFulfiller(hour);
		asignateClassroomAssignment(assignment.getRequest(), assignment.getAssignableItem(), period);
	}

	public void moveAssignmentOfPeriod(ClassroomAssignment assignment, Period newPeriod) {
		deleteClassroomAssignmentFromARequest(assignment.getRequest());
		asignateClassroomAssignment(assignment.getRequest(), assignment.getAssignableItem(), newPeriod);
	}
}