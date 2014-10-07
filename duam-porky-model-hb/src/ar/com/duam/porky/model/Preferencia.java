package ar.com.duam.porky.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "pky_preferencias")
public class Preferencia implements Serializable
{

    public enum Name
    {

        CANTIDAD_CONCEPTOS_GRAFICO
    }
    private static final long serialVersionUID = -906656887978685956L;
    @Column(name = "pre_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne
    @JoinColumn(name = "pre_usu_id")
    private Usuario usuario;
    @Column(name = "pre_clave", length = 50, nullable = false)
    private String clave;
    @Column(name = "pre_valor", length = 100)
    private String valor;

    public String getClave()
    {
        return clave;
    }

    public void setClave(String clave)
    {
        this.clave = clave;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Usuario getUsuario()
    {
        return usuario;
    }

    public void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
    }

    public String getValor()
    {
        return valor;
    }

    public void setValor(String valor)
    {
        this.valor = valor;
    }
}
