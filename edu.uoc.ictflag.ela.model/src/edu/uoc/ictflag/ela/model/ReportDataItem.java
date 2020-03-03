package edu.uoc.ictflag.ela.model;

import java.util.List;

public class ReportDataItem
{
	protected List<String> groupFields;
	protected String chartLabel;
	protected String tableLabel;

	public List<String> getGroupFields()
	{
		return groupFields;
	}
	
	public void setGroupFields(List<String> groupFields)
	{
		this.groupFields = groupFields;
	}
	
	public String getChartLabel()
	{
		return chartLabel;
	}
	
	public void setChartLabel(String chartLabel)
	{
		this.chartLabel = chartLabel;
	}
	
	public String getTableLabel()
	{
		return tableLabel;
	}

	public void setTableLabel(String tableLabel)
	{
		this.tableLabel = tableLabel;
	}
}
