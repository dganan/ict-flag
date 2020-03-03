package edu.uoc.ictflag.institution.dal;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.institution.model.Institution;

@Stateless
@Transactional(TxType.REQUIRED)
public class InstitutionRepository extends InstitutionBaseRepository implements IInstitutionRepository
{
	@Override
	public Institution getInstitution() throws Exception
	{
		return dbHelper.getFirst("SELECT i FROM Institution i", Institution.class);
	}
	
	//	@Override
	//	public Institution getInstitution(long id) throws Exception
	//	{
	//		Map<String, Object> parameters = new HashMap<String, Object>();
	//		
	//		parameters.put("id", id);
	//		
	//		return dbHelper.getFirst("SELECT i FROM Institution i WHERE i.id = :id", Institution.class, parameters);
	//	}
	
	@Override
	public void createOrUpdateInstitution(Institution i) throws Exception
	{
		Institution institution = getInstitution();
		
		if (institution.getId().equals(i.getId()))
		{
			dbHelper.createOrUpdateEntity(i);
		}
	}
}
