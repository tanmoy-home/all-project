package association.many2many;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="TEACHER")
public class Teacher {
	
	@Id
	@Column(name="TEACHER_ID")
	@GeneratedValue
	private Long teacherId;
	
	@Column(name="FIRSTNAME")
	private String firstname;
	
	@Column(name="LASTNAME")
	private String lastname;
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(//name="TEACHER_MEETING", optional as this is default table name considered
				joinColumns={@JoinColumn(name="TEACHER_ID")}, 
				inverseJoinColumns={@JoinColumn(name="MEETING_ID")})
	private Set<Meeting> meetings = new HashSet<Meeting>();
	
	public Teacher() {
	}

	public Teacher(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Set<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(Set<Meeting> meetings) {
		this.meetings = meetings;
	}
		
	// Getter and Setter methods
	
	
	
}
