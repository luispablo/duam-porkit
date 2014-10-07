package ar.com.duam.porky.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "pky_tipos_concepto")
public class TipoConcepto implements Serializable
{

    private static final long serialVersionUID = -7506997680075022490L;
    @Column(name = "tco_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(name = "tco_nombre", nullable = false, length = 50)
    private String nombre;
    @Column(name = "tco_factor", nullable = false)
    private Integer factor;

    public Integer getFactor()
    {
        return factor;
    }

    public void setFactor(Integer factor)
    {
        this.factor = factor;
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
