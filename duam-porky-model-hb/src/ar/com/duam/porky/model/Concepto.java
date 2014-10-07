package ar.com.duam.porky.model;

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

@Entity
@Table(name = "pky_conceptos")
public class Concepto implements Serializable, Comparable
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
    private Comunidad comunidad;
    @Column(name = "con_nombre", length = 50)
    private String nombre;
    @ManyToMany(mappedBy = "conceptos", cascade =
    {
        CascadeType.MERGE, CascadeType.PERSIST
    })
    private Collection<Grupo> grupos;

    public Concepto()
    {
    }

    public Concepto(Comunidad comunidad)
    {
        this.comunidad = comunidad;
    }

    public int compareTo(Object other)
    {
        return getNombre().compareTo(((Concepto) other).getNombre());
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
}
