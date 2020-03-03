package edu.uoc.ictflag.core.model;

public interface IMappeable
{
	void preUpdate();
	
	void mapTo(Object t);
}
