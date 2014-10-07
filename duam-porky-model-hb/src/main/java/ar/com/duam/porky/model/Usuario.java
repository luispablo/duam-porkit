package ar.com.duam.porky.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ar.com.duam.porky.model.Preferencia.Name;

@Entity
@Table(name = "pky_usuarios")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public class Usuario implements Serializable
{

    private static final long serialVersionUID = -5723543732248908830L;
    @Column(name = "usu_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Version
    private Integer version;
    @Column(name = "usu_nombre", length = 50, nullable = false)
    private String nombre;
    @Column(name = "usu_apellido", length = 50)
    private String apellido;
    @Column(name = "usu_nombre_usuario", length = 100, nullable = false)
    private String nombreUsuario;
    @Column(name = "usu_email", length = 150, nullable = false)
    private String email;
    
    @XmlTransient
    @Column(name = "usu_clave", length = 100, nullable = false)
    private String clave;
    
    @ManyToOne
    @XmlTransient
    @JoinColumn(name = "usu_com_id")
    private Comunidad comunidad;
    
    @OneToMany(mappedBy = "usuario", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @MapKey(name = "pre_usu_id")
    @XmlTransient
    private Set<Preferencia> preferencias;

    public String getApellido()
    {
        return apellido;
    }

    public void setApellido(String apellido)
    {
        this.apellido = apellido;
    }

    public String getClave()
    {
        return clave;
    }

    public void setClave(String clave)
    {
        this.clave = clave;
    }

    public Comunidad getComunidad()
    {
        return comunidad;
    }

    public void setComunidad(Comunidad comunidad)
    {
        this.comunidad = comunidad;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getNombreUsuario()
    {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario)
    {
        this.nombreUsuario = nombreUsuario;
    }
    
    public void setPreferencia(Name name, Integer value)
    {
    	Preferencia preferencia = this.getPreferencia(name, value.toString());
    	
    	// In case it DOES exist, set the value.
    	preferencia.setValor(value.toString());
    }
    
    public Integer getPrefIntValue(Name name, String defaultValue)
    {
    	return Integer.parseInt(this.getPreferencia(name, defaultValue).getValor());
    }
    
    public Boolean isComplete()
    {
    	return (this.nombre != null && this.nombre.length() > 0)
    			&& (this.apellido != null && this.apellido.length() > 0)
    			&& (this.email != null && this.email.length() > 0);
    }
    
    public Preferencia getPreferencia(Name name, String defaultValue)
    {
    	return this.getPreferencia(name.name(), defaultValue);
    }
    
    public Preferencia getPreferencia(String name, String defaultValue)
    {
    	Preferencia preferencia = this.getPreferencia(name);
    	
    	if (preferencia == null)
    	{
    		preferencia = new Preferencia();
    		preferencia.setClave(name);
    		preferencia.setUsuario(this);
    		preferencia.setValor(defaultValue);
    		
    		preferencias.add(preferencia);
    	}
    	
    	return preferencia;
    }

    public Preferencia getPreferencia(Name clave)
    {
    	return this.getPreferencia(clave.name());
    }
    
    public Preferencia getPreferencia(String clave)
    {
    	if (this.preferencias != null && clave != null)
    	{
    		for (Preferencia preferencia : this.preferencias)
    		{
    			if (clave.equals(preferencia.getClave()))
    			{
    				return preferencia;
    			}
    		}
    	}
    	
    	return null;
    }
    
	public Set<Preferencia> getPreferencias() 
	{
		return preferencias;
	}

	public void setPreferencias(Set<Preferencia> preferencias) 
	{
		this.preferencias = preferencias;
	}
    
}