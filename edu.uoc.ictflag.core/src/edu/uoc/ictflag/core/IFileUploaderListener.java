package edu.uoc.ictflag.core;

import javax.servlet.http.Part;

public interface IFileUploaderListener
{
	public Part getFile();
	
	public void setFile(Part file);
	
	public void upload() throws Exception;
}
