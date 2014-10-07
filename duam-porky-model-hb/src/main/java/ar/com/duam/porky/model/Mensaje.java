package ar.com.duam.porky.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "pky_mensajes")
public class Mensaje implements Serializable
{

    private static final long serialVersionUID = -6496560302319820386L;

    public static enum EstadoMensaje
    {

        ACTIVO
    }

    public static enum TipoMensaje
    {

        QUEJA_SUGERENCIA
    }
    @Column(name = "men_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Version
    private Integer version;
    @Column(name = "men_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMensaje tipo;
    @Column(name = "men_estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoMensaje estado;
    @Column(name = "men_fecha", nullable = false)
    private Date fecha;
    @ManyToOne
    @JoinColumn(name = "men_usu_id")
    private Usuario usuario;
    @Column(name = "men_texto", length = 4000, nullable = false)
    private String texto;

    public Mensaje()
    {
    }

    public Mensaje(Usuario usuario)
    {
    	this.fecha = new Date();
        this.usuario = usuario;
        this.estado = EstadoMensaje.ACTIVO;
        this.tipo = TipoMensaje.QUEJA_SUGERENCIA;
    }

    public EstadoMensaje getEstado()
    {
        return estado;
    }

    public void setEstado(EstadoMensaje estado)
    {
        this.estado = estado;
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

    public String getTexto()
    {
        return texto;
    }

    public void setTexto(String texto)
    {
        this.texto = texto;
    }

    public TipoMensaje getTipo()
    {
        return tipo;
    }

    public void setTipo(TipoMensaje tipo)
    {
        this.tipo = tipo;
    }

    public Usuario getUsuario()
    {
        return usuario;
    }

    public void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
    }
}
