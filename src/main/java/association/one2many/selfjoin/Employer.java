package association.one2many.selfjoin;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="EMPLOYER")
public class Employer {
	
	@Id
	@Column(name="EMPLOYEE_ID")
	@GeneratedValue
	private Long employeeId;
	
	@Column(name="FIRSTNAME")
	private String firstname;
	
	@Column(name="LASTNAME")
	private String lastname;
	
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="manager_id")
	private Employer manager;

	@OneToMany(mappedBy="manager")
	private Set<Employer> subordinates; //= new HashSet<Employer>();

	public Employer() {
	}

	public Employer(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
	}

	// Getter and Setter methods

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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

	public Employer getManager() {
		return manager;
	}

	public void setManager(Employer manager) {
		this.manager = manager;
	}

	public Set<Employer> getSubordinates() {
		return subordinates;
	}

	public void setSubordinates(Set<Employer> subordinates) {
		this.subordinates = subordinates;
	}
		
	
	
}