package ar.com.duam.porky.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "pky_grupos")
public class Grupo implements Serializable
{

    private static final long serialVersionUID = -9063238189395612201L;
    @Column(name = "gru_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(name = "gru_nombre", length = 50, nullable = false)
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "gru_com_id")
    private Comunidad comunidad;
    @ManyToMany
    @JoinTable(name = "pky_conceptos_grupos",
    joinColumns = @JoinColumn(name = "cgr_gru_id"),
    inverseJoinColumns = @JoinColumn(name = "cgr_con_id"))
    private Collection<Concepto> conceptos;

    public Collection<Concepto> getConceptos()
    {
        return conceptos;
    }

    public void setConceptos(Collection<Concepto> conceptos)
    {
        this.conceptos = conceptos;
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
}
