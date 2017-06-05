package mapping.component;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StudentAddress {

    @Column(name="DEPARTMENT")
    private String department;
	@Column(name="STREET")
    private String street;
    @Column(name="CITY")
    private String city;
    @Column(name="STATE")
    private String state;
    
    public StudentAddress()
    {
        super();
    }
    
    public StudentAddress(String department, String street, String city, String state)
    {
        super();
        this.department = department;
        this.street = street;
        this.city = city;
        this.state = state;
    }

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
    
}
