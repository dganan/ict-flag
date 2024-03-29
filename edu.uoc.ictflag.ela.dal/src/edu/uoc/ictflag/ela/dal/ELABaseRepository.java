package edu.uoc.ictflag.ela.dal;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.core.dal.DBHelper;

@Stateless
@Transactional(TxType.REQUIRED)
public abstract class ELABaseRepository
{
	@PersistenceContext(unitName = "edu.uoc.ictflag.ela")
	protected EntityManager em;
	
	protected DBHelper dbHelper;

	@PostConstruct
	public void initializeDBHelper()
	{
		this.dbHelper = new DBHelper(em);
	}
	
	public void setEntityManager(EntityManager em)
	{
		this.em = em;
		this.dbHelper = new DBHelper(em);
	}
}
