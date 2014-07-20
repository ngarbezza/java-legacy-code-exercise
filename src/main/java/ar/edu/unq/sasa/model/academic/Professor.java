package ar.edu.unq.sasa.model.academic;

import java.util.LinkedList;
import java.util.List;

/**
 * PROFESSOR Class denoting a Professor entity, who has personal data and a list
 * of {@link Subject}, that are all the subjects he can teach.
 *
 * CONSIDERATIONS The id instance variable is going to be final,
 *         because no changes are allowed.
 */
public class Professor {

	private String name;

	private final long id;

	private String phoneNumber;

	private String mail;

	private List<Subject> subjects;

	public Professor(String aName, long anID, String aPhoneNumber, String aMail,
			List<Subject> aSubjectList) {
		this.name = aName;
		this.id = anID;
		this.phoneNumber = aPhoneNumber;
		this.mail = aMail;
		this.subjects = aSubjectList;
	}
	
	public Professor(String name, long i, String phone, String mail) {
		this(name, i, phone, mail, new LinkedList<Subject>());
	}

	public long getId() {
		return id;
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
		if (!(this.getSubjects().contains(aSubject))) {
			this.getSubjects().add(aSubject);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (id != other.id)
			return false;
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
