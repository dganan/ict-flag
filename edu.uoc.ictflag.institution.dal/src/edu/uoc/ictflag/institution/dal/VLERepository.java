package edu.uoc.ictflag.institution.dal;

import java.util.List;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.institution.model.VLE;

@Stateless
@Transactional(TxType.REQUIRED)
public class VLERepository extends InstitutionBaseRepository implements IVLERepository
{
	@Override
	public List<VLE> getVLEsList() throws Exception
	{
		return dbHelper.getQueryResult("SELECT v FROM VLE v", VLE.class);
	}
	
	@Override
	public VLE getVLE(long id) throws Exception
	{
		return dbHelper.getEntity(id, VLE.class);
	}
	
	@Override
	public void createOrUpdateVLE(VLE p) throws Exception
	{
		dbHelper.createOrUpdateEntity(p);
	}
	
	@Override
	public void deleteVLE(long id) throws Exception
	{
		dbHelper.deleteEntity(id, VLE.class);
	}
}
