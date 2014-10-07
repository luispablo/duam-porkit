/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.duam.porky.ui;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import ar.com.duam.porky.model.Usuario;
import ar.com.duam.porky.services.IService;
import ar.com.duam.porky.ui.filters.FilterMovimientoList;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 *
 * @author Luis Pablo
 */
public class SessionPorky extends AuthenticatedWebSession
{
    private static final long serialVersionUID = -5592265272225840284L;

    @Inject
    private Provider<IService> svcProvider;
    
    private Usuario usuario;
    private FilterMovimientoList filter;
    private Page previousPage;
    
    public SessionPorky(Request request)
    {
        super(request);

        filter = new FilterMovimientoList();
    }


    public Page getPreviousPage()
    {
        return previousPage;
    }

    public void setPreviousPage(Page previousPage)
    {
        this.previousPage = previousPage;
    }

    public FilterMovimientoList getFilter()
    {
        return filter;
    }

    public void setFilter(FilterMovimientoList filter)
    {
        this.filter = filter;
    }

    public Usuario getUsuario()
    {
        return usuario;
    }

    public void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
    }

	@Override
	public boolean authenticate(String username, String password) 
	{
		Usuario usuario = svcProvider.get().findUsuarioByUsername(username);
		this.usuario = usuario;
		
        return  (usuario != null && usuario.getClave().equals(password));
	}

	@Override
	public Roles getRoles() 
	{
		return null;
	}
	
}