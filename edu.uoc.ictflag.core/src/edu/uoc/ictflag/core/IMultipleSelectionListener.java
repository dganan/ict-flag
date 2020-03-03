package edu.uoc.ictflag.core;

public interface IMultipleSelectionListener
{
	//	public void onRowSelectCheckbox(SelectEvent event);
	//	
	//	public void onRowUnselectCheckbox(UnselectEvent event);
	
	//  public void onToggleSelect(ToggleEvent event);

	public void selectItem(Long id);
	
	public void unselectItem(Long id);
	
	public String itemToString(Object item);
}
