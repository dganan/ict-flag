package edu.uoc.ictflag.core.web;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Named
@ApplicationScoped
public class ValidationHelper implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Validator validator;
	
	public <T> boolean validate(T t)
	{
		Set<ConstraintViolation<T>> violations = validator.validate(t);
		
		if (violations.size() > 0)
		{
			for (ConstraintViolation<T> cv : violations)
			{
				boolean listItem = false;
				String listItemIndex = "";
				
				String path = cv.getPropertyPath().toString().replace('.', '-');
				
				// We assume there will not be multiple nested levels (lists inside lists)
				// so we handle only one [index] in the path
				int startIndex = path.indexOf('[');
				
				if (startIndex >= 0)
				{
					listItem = true;
					
					int endIndex = path.indexOf(']', startIndex);
					
					listItemIndex = path.substring(startIndex + 1, endIndex);
					
					path = path.substring(0, startIndex) + (endIndex >= path.length() - 1 ? "" : path.substring(endIndex + 1));
				}
				
				UIComponent component = getComponent(path);
				
				if (component != null)
				{
					//component.getAttributes().put("styleClass", component.getAttributes().get("styleClass") + " focus");
					
					if (component instanceof UIInput)
					{
						component.getParent().getAttributes().put("styleClass",
								component.getParent().getAttributes().get("styleClass") + " has-error");
					}
					
					String clientId = component.getClientId();
					
					if (listItem)
					{
						int i = clientId.lastIndexOf(':');
						
						clientId = clientId.substring(0, i);
						
						i = clientId.lastIndexOf(':');
						
						clientId = clientId.substring(0, i) + ":" + listItemIndex + ":" + path;
					}
					
					FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN, cv.getMessage(), null));
				}
			}
			
			return false;
		}
		
		return true;
	}
	
	public static String getClientId(String componentId)
	{
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		
		UIComponent c = findComponent(root, componentId);
		
		if (c != null)
		{
			return c.getClientId(context);
		}
		
		return null;
	}
	
	public static UIComponent getComponent(String componentId)
	{
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		
		return findComponent(root, componentId);
	}
	
	private static UIComponent findComponent(UIComponent c, String id)
	{
		if (id.equals(c.getId()) || c.getClientId().endsWith(id))
		{
			return c;
		}
		
		Iterator<UIComponent> kids = c.getFacetsAndChildren();
		
		while (kids.hasNext())
		{
			UIComponent found = findComponent(kids.next(), id);
			
			if (found != null)
			{
				return found;
			}
		}
		
		return null;
	}
	
	public static void addValidationMessage(String message)
	{
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
	}
}
