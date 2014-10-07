package ar.com.duam.porky.model;

import java.io.Serializable;
import java.util.Map;
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

@Entity
@Table(name = "pky_usuarios")
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
    @Column(name = "usu_clave", length = 100, nullable = false)
    private String clave;
    @ManyToOne
    @JoinColumn(name = "usu_com_id")
    private Comunidad comunidad;
    @OneToMany(mappedBy = "usuario", cascade =
    {
        CascadeType.MERGE, CascadeType.PERSIST
    })
    @MapKey(name = "clave")
    private Map<String, Preferencia> preferencias;

    public Map<String, Preferencia> getPreferencias()
    {
        return preferencias;
    }

    public void setPreferencias(Map<String, Preferencia> preferencias)
    {
        this.preferencias = preferencias;
    }

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
}
