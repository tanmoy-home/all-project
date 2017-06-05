package mapping.component;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="STUDENT")
public class Student {

    @Id
    @GeneratedValue
    @Column(name="STUDENT_ID")
    private Long id;
    @Column(name="STUDENT_NAME")
    private String name;
    
    @Embedded
    private StudentAddress address;
	
    public Student() {
    	super();
	}
    
    public Student(Long id, String name, StudentAddress address)
    {
        super();
        this.id = id;
        this.name = name;
        this.address = address;
    }


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StudentAddress getAddress() {
		return address;
	}

	public void setAddress(StudentAddress address) {
		this.address = address;
	}
    
    
}
