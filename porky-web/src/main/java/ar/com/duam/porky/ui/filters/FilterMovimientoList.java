/*
 * FilterMovimientoList.java
 *
 * Created on 02/05/2008
 *
 * Copyright(c) 2007 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.ui.filters;

import ar.com.duam.api.date.FirstDayOfMonth;
import ar.com.duam.api.date.LastDayOfMonth;
import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.model.TipoConcepto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class FilterMovimientoList implements Serializable
{

    private static final long serialVersionUID = 5410510207618801095L;
    private List<Object[]> orderFields = new ArrayList<Object[]>();
    private Date desde;
    private Date hasta;
    private Concepto concepto;
    private TipoConcepto tipoConcepto;
    private String detalle;

	public FilterMovimientoList()
    {
        desde = new FirstDayOfMonth();
        hasta = new LastDayOfMonth();

        orderFields.add(new Object[]
                {
                    "fecha", "desc"
                });
    }

    /**
     * <p>Agrega el campo para ordenar. Si ya estaba le cambia la dirección. Si
     * no estaba lo pone como ascendente.</p>
     * <br>
     * 
     * @param fieldName Nombre del campo por el que se debe ordenar.
     */
    public void addOrderByField(String fieldName)
    {
        List<Object[]> newOrderFields = new ArrayList<Object[]>();
        String direction = "asc";

        for (Object[] orderField : orderFields)
        {
            if (orderField[0].equals(fieldName))
            {
                direction = (orderField[1].equals("asc") ? "desc" : "asc");
            }
            else
            {
                newOrderFields.add(orderField);
            }
        }

        this.orderFields.clear();
        this.orderFields.add(new Object[]
                {
                    fieldName, direction
                });
        this.orderFields.addAll(newOrderFields);
    }

    public Concepto getConcepto()
    {
        return concepto;
    }

    public void setConcepto(Concepto concepto)
    {
        this.concepto = concepto;
    }

    public Date getDesde()
    {
        return desde;
    }

    public void setDesde(Date desde)
    {
        this.desde = desde;
    }

    public Date getHasta()
    {
        return hasta;
    }

    public void setHasta(Date hasta)
    {
        this.hasta = hasta;
    }

    public List<Object[]> getOrderFields()
    {
        return orderFields;
    }

    public void setOrderFields(List<Object[]> orderFields)
    {
        this.orderFields = orderFields;
    }

	public TipoConcepto getTipoConcepto() 
	{
		return tipoConcepto;
	}

	public void setTipoConcepto(TipoConcepto tipoConcepto) 
	{
		this.tipoConcepto = tipoConcepto;
	}

	public String getDetalle() 
    {
		return detalle;
	}

	public void setDetalle(String detalle) 
	{
		this.detalle = detalle;
	}

}