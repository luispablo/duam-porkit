/*
 * PageMensajeForm.java
 *
 * Created on 21/05/2008
 *
 * Copyright(c) 2007 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.pages;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import ar.com.duam.porky.model.Mensaje;
import ar.com.duam.porky.model.Usuario;
import ar.com.duam.porky.ui.SessionPorky;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class PageMensajeForm extends PagePorkyBase
{
	private static final long serialVersionUID = 1528571542748821215L;
	private static Logger logger = Logger.getLogger(PageMensajeForm.class);

	@Inject
	private EntityManager em;
	
    @Override
    protected void init()
    {
        this.add(new FormMensaje("form"));
    }

    @SuppressWarnings("rawtypes")
	class FormMensaje extends Form
    {
		private static final long serialVersionUID = -626856415256217952L;

		@SuppressWarnings({ "unchecked", "serial" })
		public FormMensaje(String id)
        {
            super(id);

            try
            {
                final SessionPorky session = (SessionPorky) this.getSession();
                Usuario usuario = session.getUsuario();
                final Mensaje mensaje = new Mensaje(usuario);

                this.setModel(new CompoundPropertyModel(mensaje));

                Button buttonEnviar = new Button("buttonEnviar")
                {

                    @Override
                    public void onSubmit()
                    {
                        try
                        {
                            em.merge(mensaje);
                            
                            Session.get().info(this.getLocalizer().getString("enviado", this));
                            this.setResponsePage(PageMovimientoList.class);
                        }
                        catch (Exception exception)
                        {
                            logger.error("ERROR", exception);
                            this.error(exception.getMessage());
                        }
                    }
                };
                buttonEnviar.setModel(new Model(this.getLocalizer().getString("enviar", PageMensajeForm.this)));
                
                this.add(buttonEnviar);
                this.add(new TextArea("texto"));
            }
            catch (Exception exception)
            {
                logger.error("ERROR", exception);
                this.error(exception.getMessage());
            }
        }
    }
}