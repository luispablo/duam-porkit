package ar.com.duam.porky.ui.pages;

import javax.inject.Inject;

import ar.com.duam.porky.model.Usuario;
import ar.com.duam.porky.services.IService;
import ar.com.duam.porky.ui.SessionPorky;
import ar.com.duam.security.Crypto;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.CompoundPropertyModel;

import com.google.inject.Provider;

public class PageCambioClave extends PagePorkyBase
{
	private static final long serialVersionUID = -9174631007122446069L;
	private static Logger logger = Logger.getLogger(PageCambioClave.class);
    private String nuevaClave;
    private String nuevaClaveRepetida;

    @Inject
    private Provider<IService> svcProvider;
    
    @Override
    protected void init()
    {
        CambioClaveForm form = new CambioClaveForm("cambioClaveForm");

        this.add(form);
    }

    public String getNuevaClave()
    {
        return nuevaClave;
    }

    public void setNuevaClave(String nuevaClave)
    {
        this.nuevaClave = nuevaClave;
    }

    public String getNuevaClaveRepetida()
    {
        return nuevaClaveRepetida;
    }

    public void setNuevaClaveRepetida(String nuevaClaveRepetida)
    {
        this.nuevaClaveRepetida = nuevaClaveRepetida;
    }

    @SuppressWarnings("rawtypes")
	class CambioClaveForm extends Form
    {
		private static final long serialVersionUID = -2500872954826703109L;

		@SuppressWarnings("unchecked")
		public CambioClaveForm(String id)
        {
            super(id, new CompoundPropertyModel(PageCambioClave.this));

            try
            {
                this.add(new PasswordTextField("nuevaClave").setRequired(true));
                this.add(new PasswordTextField("nuevaClaveRepetida").setRequired(true));
            }
            catch (Exception exception)
            {
                logger.error("EXCEPTION", exception);
                this.error(exception.getMessage());
            }
        }

        @Override
        protected void onSubmit()
        {
            try
            {
                String clave = PageCambioClave.this.getNuevaClave();
                String repetida = PageCambioClave.this.getNuevaClaveRepetida();

                if (!(clave != null && repetida != null && clave.equals(repetida)))
                {
                    this.error(this.getLocalizer().getString("noCoincidenClaves", this.getPage()));
                }
                else
                {
                    SessionPorky session = (SessionPorky) this.getSession();
                    Usuario usuario = session.getUsuario();

                    usuario.setClave(Crypto.encrypt(clave));
                    svcProvider.get().merge(usuario);

                    session.getFeedbackMessages().info(PageCambioClave.this, this.getLocalizer().getString("claveCambiada", this.getPage()));

                    this.setResponsePage(new PageMovimientoList());
                }
            }
            catch (Exception exception)
            {
                logger.error("ERROR", exception);
                this.error(exception.getMessage());
            }
        }
    }
}