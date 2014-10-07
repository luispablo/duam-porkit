package ar.com.duam.porky.model;

import java.io.Serializable;
import java.util.List;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import org.apache.log4j.Logger;

@Entity
@Table(name = "pky_comunidades")
public class Comunidad implements Serializable
{

    private static Logger logger = Logger.getLogger(Comunidad.class);
    private static final long serialVersionUID = -8182909331992148710L;
    @Id
    @Column(name = "com_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(name = "com_codigo", length = 10)
    private String codigo;
    @Column(name = "com_nombre", length = 50, nullable = false)
    private String nombre;
    @OneToMany(cascade =
    {
        CascadeType.PERSIST, CascadeType.MERGE
    }, mappedBy = "comunidad")
    private List<Movimiento> movimientos;
    @OneToMany(cascade =
    {
        CascadeType.PERSIST, CascadeType.MERGE
    }, mappedBy = "comunidad")
    private Set<Concepto> conceptos;

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

    public List<Movimiento> getMovimientos()
    {
        return movimientos;
    }

    public void setMovimientos(List<Movimiento> movimientos)
    {
        this.movimientos = movimientos;
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
