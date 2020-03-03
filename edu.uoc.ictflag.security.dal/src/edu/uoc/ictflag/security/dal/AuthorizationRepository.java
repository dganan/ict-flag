package edu.uoc.ictflag.security.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.security.model.Page;
import edu.uoc.ictflag.security.model.UserRole;
import edu.uoc.ictflag.security.model.UserRolePageAccess;
import edu.uoc.ictflag.security.model.UserRolePermissions;

@Stateless
@Transactional(TxType.REQUIRED)
public class AuthorizationRepository extends SecurityBaseRepository implements IAuthorizationRepository
{
	@Override
	public UserRolePermissions getUserRolePermissions(UserRole role, String module, String entity) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("role", role);
		parameters.put("module", module);
		parameters.put("entity", entity);
		
		return dbHelper.getFirst("SELECT p FROM UserRolePermissions p WHERE p.role = :role AND p.module = :module AND p.entity = :entity",
				UserRolePermissions.class, parameters);
	}
	
	@Override
	public boolean userRolePageAccessExists(UserRole role, String pageURL) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("role", role);
		parameters.put("relativeUrl", pageURL);
		
		return (long) dbHelper.getScalarValue(
					"SELECT COUNT(pa) FROM UserRolePageAccess pa WHERE pa.role = :role AND LOWER(pa.page.relativeUrl) = LOWER(:relativeUrl)",
				parameters) == 1;
	}
	
	@Override
	public List<Page> getAllMenuPages() throws Exception
	{
		return dbHelper.getQueryResult("SELECT p FROM Page p WHERE p.inMenu = true", Page.class);
	}
	
	@Override
	public List<Page> getUserRoleMenuPages(UserRole role) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("role", role);
		
		return dbHelper.getQueryResult("SELECT pa.page FROM UserRolePageAccess pa WHERE pa.role = :role AND pa.page.inMenu = true", Page.class,
				parameters);
	}
	
	@Override
	public void createPermission(UserRolePermissions u) throws Exception
	{
		dbHelper.addEntity(u);
	}
	
	@Override
	public void createAccess(UserRolePageAccess u) throws Exception
	{
		dbHelper.addEntity(u);
	}
}
