package edu.uoc.ictflag.security.dal;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.security.model.UserActivity;

@Stateless
@Transactional(TxType.REQUIRED)
public class UserActivityRepository extends SecurityBaseRepository implements IUserActivityRepository
{
	@Override
	public void createUserActivity(UserActivity userActivity) throws Exception
	{
		dbHelper.createOrUpdateEntity(userActivity);
	}
}
