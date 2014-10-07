package ar.com.duam.porky.model;

import static ar.com.duam.porky.model.Constantes.WS_FIELD_DELIMITER;
import static ar.com.duam.porky.model.Constantes.WS_LINE_DELIMITER;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity @Table(name = "pky_conceptos")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public class Concepto implements Serializable, Comparable<Concepto>
{

    private static final long serialVersionUID = 8635364578780727092L;
    @Column(name = "con_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne
    @JoinColumn(name = "con_tco_id")
    private TipoConcepto tipoConcepto;
    
    @ManyToOne 
    @JoinColumn(name = "con_com_id") 
    @XmlTransient
    private Comunidad comunidad;
    
    @Column(name = "con_nombre", length = 50)
    private String nombre;
    
    @Column(name = "con_valor_x_defecto")
    private Float valorPorDefecto = 0f;
    
    @ManyToMany(mappedBy = "conceptos", cascade={CascadeType.MERGE, CascadeType.PERSIST})  @XmlTransient
    private Collection<Grupo> grupos;

    public Concepto()
    {
    }

    public Concepto(Comunidad comunidad)
    {
        this.comunidad = comunidad;
    }

    public int compareTo(Concepto other)
    {
        return getNombre().compareTo(other.getNombre());
    }

    /**
     * <p>This method returns a truncated version of the name, with a
     * maximun of 20 characters long.</p>
     * <br>
     *
     * @return String
     */
    public String getNombreTruncated()
    {
        if (nombre.length() > 20)
        {
            return nombre.substring(0, 20).concat("(...)");
        }
        else
        {
            return nombre;
        }
    }

    public Collection<Grupo> getGrupos()
    {
        return grupos;
    }

    public void setGrupos(Collection<Grupo> grupos)
    {
        this.grupos = grupos;
    }

    public Comunidad getComunidad()
    {
        return comunidad;
    }

    public void setComunidad(Comunidad comunidad)
    {
        this.comunidad = comunidad;
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

    public TipoConcepto getTipoConcepto()
    {
        return tipoConcepto;
    }

    public void setTipoConcepto(TipoConcepto tipoConcepto)
    {
        this.tipoConcepto = tipoConcepto;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }
        if (o == this)
        {
            return true;
        }
        if (o instanceof Concepto)
        {
            return ((Concepto) o).getId().equals(id);
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    { 
        int hash = 7;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 79 * hash + (this.tipoConcepto != null ? this.tipoConcepto.hashCode() : 0);
        hash = 79 * hash + (this.comunidad != null ? this.comunidad.hashCode() : 0);
        hash = 79 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        hash = 79 * hash + (this.grupos != null ? this.grupos.hashCode() : 0);
        return hash;
    }

    public String getLineaWS()
    {
    	return this.getId().toString() + WS_FIELD_DELIMITER + this.getNombre() + WS_LINE_DELIMITER; 
    }
    
	public void setValorPorDefecto(Float valorPorDefecto) 
	{
		this.valorPorDefecto = valorPorDefecto;
	}

	public Float getValorPorDefecto() 
	{
		return valorPorDefecto;
	}
	
}