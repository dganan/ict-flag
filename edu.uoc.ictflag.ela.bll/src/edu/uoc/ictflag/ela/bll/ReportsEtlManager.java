package edu.uoc.ictflag.ela.bll;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.uoc.ictflag.ela.dal.IReportsEtlRepository;

@Stateless
public class ReportsEtlManager implements IReportsEtlManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	IReportsEtlRepository reportsEtlRepository;
	
	@Inject
	public ReportsEtlManager(IReportsEtlRepository reportsEtlRepository)
	{
		this.reportsEtlRepository = reportsEtlRepository;
	}

	@Override
	public void startReportsEtlProcess() throws Exception
	{
		reportsEtlRepository.startReportsEtlProcess();
	}
}
