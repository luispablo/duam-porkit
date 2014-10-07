package ar.com.duam.porky.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Programacion implements Serializable 
{
	private static final long serialVersionUID = 3910416449975412678L;

	public enum Frecuencia
	{
		MENSUAL,
		BIMESTRAL,
		TRIMESTRAL,
		SEMESTRAL,
		ANUAL
	}
	
	@Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	@Column
	private Integer version;
	@Column
	private Frecuencia frecuencia;
	
	public Long getId() 
	{
		return id;
	}
	
	public void setId(Long id) 
	{
		this.id = id;
	}
	
	public Frecuencia getFrecuencia() 
	{
		return frecuencia;
	}

	public void setFrecuencia(Frecuencia frecuencia) 
	{
		this.frecuencia = frecuencia;
	}
	
}