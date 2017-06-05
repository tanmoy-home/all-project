package association.one2many.selfjoin;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import association.HibernateUtil;
 
public class Main {
 
    public static void main(String[] args) {
 
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
 
        Employer manager1 = new Employer("Bruce", "Lee");
        
        Employer employer1 = new Employer("Jet", "Lee");
        Employer employer2 = new Employer("Jackie", "Chan");

        employer1.setManager(manager1);
        employer2.setManager(manager1);
        
        session.save(employer1);
        session.save(employer2);
        
        session.getTransaction().commit();
        session.close();
    }
}
