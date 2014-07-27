package ar.edu.unq.sasa.model.academic;

import java.util.LinkedList;
import java.util.List;

public class Professor {

	private String name;

	private String phoneNumber;

	private String mail;

	private List<Subject> subjects;

	public Professor(String aName, String aPhoneNumber, String aMail, List<Subject> aSubjectList) {
		this.name = aName;
		this.phoneNumber = aPhoneNumber;
		this.mail = aMail;
		this.subjects = aSubjectList;
	}

	public Professor(String name, String phone, String mail) {
		this(name, phone, mail, new LinkedList<Subject>());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void addNewSubject(Subject aSubject) {
		if (!(this.getSubjects().contains(aSubject)))
			this.getSubjects().add(aSubject);
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
