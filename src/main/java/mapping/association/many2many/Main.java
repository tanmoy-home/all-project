package mapping.association.many2many;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import util.HibernateUtil;

public class Main {
	public static void main(String[] args) {
		 
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
 
        
        Meeting meeting1 = new Meeting("Quaterly Sales meeting");
        Meeting meeting2 = new Meeting("Weekly Status meeting");
        
        Teacher teacher1 = new Teacher("Sergey", "Brin");
        Teacher teacher2 = new Teacher("Larry", "Page");

        teacher1.getMeetings().add(meeting1);
        teacher1.getMeetings().add(meeting2);
        teacher2.getMeetings().add(meeting1);
        
        session.save(teacher1);
        session.save(teacher2);
        
        session.getTransaction().commit();
        session.close();
    }
}
