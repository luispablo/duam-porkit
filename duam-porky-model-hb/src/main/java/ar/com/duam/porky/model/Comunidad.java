package ar.com.duam.porky.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import ar.com.duam.porky.model.Preferencia.Name;

@Entity @Table(name = "pky_comunidades")
@XmlAccessorType(XmlAccessType.FIELD)
public class Comunidad implements Serializable
{

    private static Logger logger = Logger.getLogger(Comunidad.class);
    private static final long serialVersionUID = -8182909331992148710L;
    
    @Id @Column(name = "com_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Version
    private Integer version;
	
    @Column(name = "com_codigo", length = 10)
    private String codigo;
    
    @Column(name = "com_nombre", length = 50, nullable = false)
    private String nombre;
    
    @OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "comunidad")
    @XmlTransient
    private Set<Concepto> conceptos;
    
    @OneToMany(mappedBy = "comunidad", cascade={CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER) @MapKey(name = "pre_com_id")
    @XmlTransient
    private Set<Preferencia> preferencias;
    
    public void setPreferencia(String name, String value)
    {
    	Preferencia preferencia = this.getPreferencia(name, value);
    	
    	// In case it DOES exist, set the value.
    	preferencia.setValor(value.toString());
    }
    
    public void setPreferencia(Name name, Integer value)
    {
    	this.setPreferencia(name.name(), value.toString());
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
    		preferencia.setComunidad(this);
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

    public Double getAhorroProgramadoMes()
    {
    	return this.getAhorroProgramadoMes(new Date());
    }
    
    public Double getAhorroProgramadoMes(Date fecha)
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");
    	String name = sdf.format(fecha);
    	
    	Preferencia ahorroProg = this.getPreferencia(name);
    	
    	if (ahorroProg == null)
    	{
    		ahorroProg = new Preferencia();
    		ahorroProg.setClave(name);
    		ahorroProg.setComunidad(this);
    		ahorroProg.setValor(this.getAhorroProgramadoMesAnterior(fecha).toString());
    		
    		preferencias.add(ahorroProg);
    	}
    	
		return Double.parseDouble(ahorroProg.getValor());
    }

    public Double getAhorroProgramadoMesAnterior(Date fecha)
    {
    	Calendar aux = Calendar.getInstance();
    	aux.setTime(fecha);
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");
    	String name = sdf.format(aux.getTime());
    	
    	Preferencia anterior = this.getPreferencia(name);
    	
    	if (anterior != null)
    	{
    		return Double.parseDouble(anterior.getValor());
    	}
    	else
    	{
    		return 0d;
    	}
    }
        
	public Set<Preferencia> getPreferencias() 
	{
		return preferencias;
	}

	public void setPreferencias(Set<Preferencia> preferencias) 
	{
		this.preferencias = preferencias;
	}
	
    @Override
    public boolean equals(Object other)
    {
        if (other != null)
        {
            if (other instanceof Comunidad)
            {
                logger.debug("comparando...");
                Comunidad that = (Comunidad) other;

                if (this == that)
                {
                    return true;
                }
                if (id != null && that.getId() != null && !id.equals(that.getId()))
                {
                    return false;
                }
                if (!(id == null && that.getId() == null))
                {
                    return false;
                }
                if (codigo != null && that.getCodigo() != null && !codigo.equals(that.getCodigo()))
                {
                    return false;
                }
                if (!(codigo == null && that.getCodigo() == null))
                {
                    return false;
                }
                if (nombre != null && that.getNombre() != null && !nombre.equals(that.getNombre()))
                {
                    return false;
                }
                if (!(nombre == null && that.getNombre() == null))
                {
                    return false;
                }

                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    @XmlTransient
    public Set<Concepto> getConceptos()
    {
        return conceptos;
    }

    public void setConceptos(Set<Concepto> conceptos)
    {
        this.conceptos = conceptos;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 13 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 13 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        hash = 13 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        return hash;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
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
}
