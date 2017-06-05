package mapping.component;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import util.HibernateUtil;

public class Main {
	public static void main(String[] args) {
		 
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
 
        
      //Create a new EmployeeAddress object
        StudentAddress address = new StudentAddress();
        address.setDepartment("Computer");
        address.setStreet("Tharamani");
        address.setCity("Chennai");
        address.setState("TamilNadu");
        
        //Create a new Employee object
        Student student = new Student();
        //employee.setId(1);
        student.setName("JavaInterviewPoint");
        student.setAddress(address);
        
        session.save(student);        
        session.getTransaction().commit();
        session.close();
    }
}
