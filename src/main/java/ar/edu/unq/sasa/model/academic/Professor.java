package ar.edu.unq.sasa.model.academic;

import java.util.LinkedList;
import java.util.List;

public class Professor {

	private String name;

	private String phoneNumber;

	private String mail;

	private List<Subject> subjects;

	public Professor(String aName, String aPhoneNumber, String aMail, List<Subject> aSubjectList) {
		name = aName;
		phoneNumber = aPhoneNumber;
		mail = aMail;
		subjects = aSubjectList;
	}

	public Professor(String aName, String phone, String anEmail) {
		this(aName, phone, anEmail, new LinkedList<Subject>());
	}

	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String aPhoneNumber) {
		phoneNumber = aPhoneNumber;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String anEmail) {
		mail = anEmail;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void addNewSubject(Subject aSubject) {
		if (!(teaches(aSubject)))
			subjects.add(aSubject);
	}

	public boolean teaches(Subject aSubject) {
		return subjects.contains(aSubject);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mail.hashCode();
		result = prime * result + name.hashCode();
		result = prime * result + phoneNumber.hashCode();
		result = prime * result + subjects.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Professor other = (Professor) obj;
		return other != null && mail.equals(other.mail) && name.equals(other.name)
				&& phoneNumber.equals(other.phoneNumber) && subjects.equals(other.subjects);
	}
}
