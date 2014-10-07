package ar.com.duam.porky.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "pky_movimientos")
public class Movimiento implements Serializable, Comparable
{

    private static Integer TAMANIO_MAXIMO_DETALLE = 30;
    private static final long serialVersionUID = 7138698361683782174L;
    @Column(name = "mov_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne
    @JoinColumn(name = "mov_con_id")
    private Concepto concepto;
    @ManyToOne
    @JoinColumn(name = "mov_com_id")
    private Comunidad comunidad;
    @Column(name = "mov_detalle", length = 255, nullable = false)
    private String detalle;
    @Column(name = "mov_fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "mov_importe")
    private Float importe;

    /**
     * <p>Trunca el detalle para que no se rompan los listados.</p>
     * <br>
     *
     * @return String
     */
    public String getDetalleTruncated()
    {
        if (detalle != null && detalle.length() > TAMANIO_MAXIMO_DETALLE)
        {
            return detalle.substring(0, TAMANIO_MAXIMO_DETALLE) + "(...)";
        }
        else
        {
            return detalle;
        }
    }

    public Comunidad getComunidad()
    {
        return comunidad;
    }

    public void setComunidad(Comunidad comunidad)
    {
        this.comunidad = comunidad;
    }

    public Concepto getConcepto()
    {
        return concepto;
    }

    public void setConcepto(Concepto concepto)
    {
        this.concepto = concepto;
    }

    public String getDetalle()
    {
        return detalle;
    }

    public void setDetalle(String detalle)
    {
        this.detalle = detalle;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Float getImporte()
    {
        return importe;
    }

    public void setImporte(Float importe)
    {
        this.importe = importe;
    }

    public int compareTo(Object o)
    {
        if (o instanceof Movimiento && o != null)
        {
            Date date = getFecha();
            Date otherDate = ((Movimiento) o).getFecha();

            if (date != null)
            {
                if (otherDate != null)
                {
                    return date.compareTo(otherDate) * (-1);
                }
                else
                {
                    return -1;
                }
            }
            else
            {
                return 1;
            }
        }
        else
        {
            return 0;
        }
    }
}
