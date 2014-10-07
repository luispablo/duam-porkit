/*
 * PageUsuarioForm.java
 *
 * Created on 13/07/2010
 *
 * Copyright(c) 2010 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.pages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import ar.com.duam.components.ui.DropDownChoiceMonths;
import ar.com.duam.components.ui.DropDownChoiceYears;
import ar.com.duam.porky.model.Comunidad;
import ar.com.duam.porky.model.Usuario;
import ar.com.duam.porky.services.IService;
import ar.com.duam.security.Crypto;

import com.google.inject.Provider;

public class PageUsuarioForm extends PagePorkyBase 
{
	private static final long serialVersionUID = 2514199966027429007L;
	
	private DropDownChoiceMonths month;
	private DropDownChoiceYears year;
	private Double ahorro;
	
	@Inject
	private Provider<IService> svcProvider;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageUsuarioForm()
	{
		final Usuario usuario = this.getUsuario();
		final Comunidad comunidad = svcProvider.get().merge(usuario.getComunidad());
		this.ahorro = comunidad.getAhorroProgramadoMes();
		
		if (!usuario.isComplete())
		{
    		error(getString("userIncomplete"));			
		}
		
		Form form = new Form("form")
		{
			private static final long serialVersionUID = 4076263058343573782L;

			@Override
			protected void onSubmit() 
			{
				// Grabar los cambios.
				svcProvider.get().merge(usuario);
				
				comunidad.setPreferencia(getPeriodo(), ahorro.toString());
				svcProvider.get().merge(comunidad);
				
				info(getString("saved"));
			}			
		};
		this.add(form);

		Locale locale = this.getLocale();
		
		form.add(month = new DropDownChoiceMonths("month", locale));
		form.add(year = new DropDownChoiceYears("year", locale));	
		
		try
		{
			Label nombreUsuario = new Label("nombreUsuario", usuario.getNombreUsuario());
			form.add(nombreUsuario);
			
			TextField nombre = new TextField<String>("nombre", new PropertyModel<String>(usuario, "nombre"));
			nombre.setRequired(true);
			form.add(nombre);
			
			TextField apellido = new TextField<String>("apellido", new PropertyModel<String>(usuario, "apellido"));
			apellido.setRequired(true);
			form.add(apellido);
			
			TextField email = new TextField<String>("email", new PropertyModel<String>(usuario, "email"));
			email.add(EmailAddressValidator.getInstance());
			email.setRequired(true);
			form.add(email);
			
			form.add(new TextField("ahorro", new PropertyModel(this, "ahorro"))); 
		}
		catch (Exception exception)
		{
			e(exception);
		}
		
		this.add(new FormPassword("formPassword", usuario));
	}

	private String getPeriodo()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, Integer.parseInt(month.getValue()) - 1);
		calendar.set(Calendar.YEAR, Integer.parseInt(year.getValue()));
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");
		
		return sdf.format(calendar.getTime());
	}
	
	public Double getAhorro() 
	{
		return ahorro;
	}

	public void setAhorro(Double ahorro) 
	{
		this.ahorro = ahorro;
	}

	@SuppressWarnings("rawtypes")
	class FormPassword extends Form
	{
		private static final long serialVersionUID = 2685704614116839109L;
		private String password;
		private String newPassword;
		private String newPasswordRepeated;

		public FormPassword(String id, Usuario usuario)
		{
			super(id);
			
			PasswordTextField password = new PasswordTextField("password", new PropertyModel<String>(this, "password"));
			password.setRequired(true);
			this.add(password);
			
			PasswordTextField newPassword = new PasswordTextField("newPassword", new PropertyModel<String>(this, "newPassword"));
			newPassword.setRequired(true);
			this.add(newPassword);
			
			PasswordTextField newPasswordRepeated = new PasswordTextField("newPasswordRepeated", new PropertyModel<String>(this, "newPasswordRepeated"));
			newPasswordRepeated.setRequired(true);
			this.add(newPasswordRepeated);
		}

		@Override
		protected void onSubmit() 
		{
			try 
			{
				if (this.newPassword.equals(this.newPasswordRepeated))
				{
					Usuario usuario = PageUsuarioForm.this.getUsuario();
					String cryptedPassword = Crypto.encrypt(this.password);
					
					if (usuario.getClave().equals(cryptedPassword))
					{
						String cryptedNewPassword = Crypto.encrypt(this.newPassword);
						
						// Update user data in database.
						usuario.setClave(cryptedNewPassword);
						svcProvider.get().merge(usuario);
						
						info(getString("passwordUpdated"));
					}
					else
					{
						error(getString("invalidPassword"));
					}
				}
				else
				{
					error(getString("passwordsDontMatch"));
				}
			} 
			catch (Exception exception) 
			{
				e(exception);
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

		public String getNewPassword() 
		{
			return newPassword;
		}

		public void setNewPassword(String newPassword) 
		{
			this.newPassword = newPassword;
		}

		public String getNewPasswordRepeated() 
		{
			return newPasswordRepeated;
		}

		public void setNewPasswordRepeated(String newPasswordRepeated) 
		{
			this.newPasswordRepeated = newPasswordRepeated;
		}
	}
	
}