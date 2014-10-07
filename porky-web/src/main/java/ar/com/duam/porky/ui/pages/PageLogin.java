/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.duam.porky.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import ar.com.duam.security.Crypto;

/**
 *
 * @author Luis Pablo
 */
public class PageLogin extends WebPage
{
	private static final long serialVersionUID = -6895904336182776653L;
	private static final Logger logger = Logger.getLogger(PageLogin.class);

    @Override
	protected void onInitialize() 
    {
		super.onInitialize();
		
        add(new FormLogin("formLogin").add(new FeedbackPanel("feedbackPanel")));
	}

    @Override
	public void renderHead(IHeaderResponse response) 
    {
    	PackageResourceReference cssFile = new PackageResourceReference(this.getClass(), "bootstrap.min.css");
    	CssHeaderItem cssItem = CssHeaderItem.forReference(cssFile);
    	response.render(cssItem);
	}

	protected void onLogin(String username, String password)
    {
		boolean authResult = AuthenticatedWebSession.get().signIn(username, password);

		if (authResult)
		{
           setResponsePage(PageHome.class);
		}
    }
    
    @SuppressWarnings("rawtypes")
	class FormLogin extends Form
    {
		private static final long serialVersionUID = 1L;
		private String username;
        private String password;

        public FormLogin (String id)
        {
            super(id);
            
            this.add(new TextField<String>("textUsername", new PropertyModel<String>(this, "username")));
            this.add(new PasswordTextField("textPassword", new PropertyModel<String>(this, "password")));
        }

        @Override
        protected void onSubmit() 
        {
        	try
        	{
	        	String cryptedPassword = Crypto.encrypt(this.password);
	            PageLogin.this.onLogin(this.username, cryptedPassword);
        	}
        	catch (Exception exception)
        	{
        		logger.error("ERROR", exception);
        	}
        }

        public String getPassword() 
        {
            return password;
        }

        public void setPassword(String password) 
        {
            this.password = password;
        }

        public String getUsername() 
        {
            return username;
        }

        public void setUsername(String username) 
        {
            this.username = username;
        }
        
    }
       
}