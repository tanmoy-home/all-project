package association.one2many.bidirectional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import association.HibernateUtil;

public class Main {

	public static void main(String[] args) {

		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();

		Department department = new Department();
		department.setDepartmentName("Sales");
		session.save(department);
		
		Employees emp1 = new Employees("Nina", "Mayers", "111");
		Employees emp2 = new Employees("Tony", "Almeida", "222");

		emp1.setDepartment(department);
		emp2.setDepartment(department);
		
		session.save(emp1);
		session.save(emp2);

		session.getTransaction().commit();
		session.close();
	}
}