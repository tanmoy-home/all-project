package com.rs.bbpou.dao;

import java.util.List;






import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.rs.bbpou.model.T_M_Template;

@Repository("htmRes")
@Transactional
public class HtmlResponseDaoImpl implements HtmlResponseDao {

	
	@javax.persistence.PersistenceContext
	  private EntityManager entityManager;
	
	@Override
	public String getHtmlResponse(Integer id) {
		
		
		T_M_Template m_Template=  (T_M_Template)entityManager.createQuery(
			        "from T_M_Template where templateId = :templateId")
			        .setParameter("templateId", id)
			        .getSingleResult();
		
	return	m_Template.getCharContent();
		
	//	List<T_M_Template> m_Template = entityManager.createQuery("FROM T_M_Template").getResultList();
		//session.close();
		//return m_Template.get(0).getCharContent();
		//return null;
	}
	
}
