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
		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((subjects == null) ? 0 : subjects.hashCode());
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
		Professor other = (Professor) obj;
		if (mail == null) {
			if (other.mail != null)
				return false;
		} else if (!mail.equals(other.mail))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phoneNumber != other.phoneNumber)
			return false;
		if (subjects == null) {
			if (other.subjects != null)
				return false;
		} else if (!subjects.equals(other.subjects))
			return false;
		return true;
	}
}
