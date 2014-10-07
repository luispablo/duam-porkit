/*
 * PorkyManagerJPA.java
 *
 * Created on 08/03/2009
 *
 * Copyright(c) 2008 DUAM Sistemas.
 * All Rights Reserved.
 * This software is the proprietary information of DUAM Sistemas.
 */
package ar.com.duam.porky.services.impl;


/**
 * @author DUAM Sistemas (http://www.duamsistemas.com.ar)
 */
public class PorkyManagerJPA //extends PersistenceManagerJPA implements PorkyManager
{
	/*
    private static Logger logger = Logger.getLogger(PorkyManagerJPA.class);

    public List<Concepto> findByComunidad(Long idComunidad)
    {
    	String hql = "from Concepto where comunidad.id = :idComunidad";
    	Map<String, Long> params = new HashMap<String, Long>();
    	params.put("idComunidad", idComunidad);
    	
    	return getJpaTemplate().findByNamedParams(hql, params);
    }
    
    public Usuario findByLogin(String login) throws Exception
    {
        String hql = "from Usuario where nombreUsuario = :nombreUsuario";
        Map<String, String> params = new HashMap<String, String>();
        params.put("nombreUsuario", login);

        @SuppressWarnings("unchecked")
		List<Usuario> result = getJpaTemplate().findByNamedParams(hql, params);

        if (result != null && !result.isEmpty())
        {
            return (Usuario) result.get(0);
        }
        else
        {
            return null;
        }
    }

	public List<Movimiento> findMovimientos(Comunidad comunidad, Date desde, Date hasta, Concepto concepto, TipoConcepto tipoConcepto, String detalle)
	{
		return this.findMovimientos(comunidad, desde, hasta, concepto, tipoConcepto, detalle, null);
	}
	
    @SuppressWarnings("unchecked")
	public List<Movimiento> findMovimientos(Comunidad comunidad, Date desde, Date hasta, Concepto concepto, TipoConcepto tipoConcepto, String detalle, List<Object[]> orderByFields)
    {
        StringBuffer hql = new StringBuffer(" from Movimiento ");
        Map<String, Object> params = new HashMap<String, Object>();
        Boolean firstCondition = true;

        if (comunidad != null)
        {
            logger.debug("comunidad: "+ comunidad.getId());
            
            hql.append(firstCondition? " where ": " and ");
            hql.append(" comunidad.id = :comunidad ");
            params.put("comunidad", comunidad.getId());
            firstCondition = false;
        }
        if (desde != null)
        {
            hql.append(firstCondition? " where ": " and ");
            hql.append(" fecha >= :desde ");
            params.put("desde", desde);
            firstCondition = false;
        }
        if (hasta != null)
        {
            hql.append(firstCondition? " where ": " and ");
            hql.append(" fecha <= :hasta ");
            params.put("hasta", hasta);
            firstCondition = false;
        }
        if (concepto != null)
        {
            hql.append(firstCondition? " where ": " and ");
            hql.append(" concepto = :concepto ");
            params.put("concepto", concepto);
            firstCondition = false;
        }
        if (tipoConcepto != null)
        {
        	hql.append(firstCondition? " where ": " and ");
        	hql.append(" concepto.tipoConcepto = :tipoConcepto ");
        	params.put("tipoConcepto", tipoConcepto);
        	firstCondition = false;
        }
        if (detalle != null)
        {
        	hql.append(firstCondition? " where ": " and ");
        	hql.append(" detalle like :detalle ");
        	params.put("detalle", "%"+ detalle +"%");
        	firstCondition = false;
        }

        if (orderByFields != null && !orderByFields.isEmpty())
        {
        	hql.append(this.buildOrderByCriteria(orderByFields));
        }
        
        return (List<Movimiento>) this.getJpaTemplate().findByNamedParams(hql.toString(), params);
    }

    private String buildOrderByCriteria(List<Object[]> orderByFields)
    {
    	String criteria = new String();
    	
    	if (orderByFields != null && !orderByFields.isEmpty())
    	{
    		for (Object[] row : orderByFields)
    		{
    			if (criteria.length() > 0)
    			{
    				criteria += ", ";
    			}
    			
    			criteria += " " + row[0] + " " + row[1];
    		}
    	}
    	
    	return " order by "+ criteria;
    }
    
    public Double getTotalPorFactor(Date desde, Date hasta, Comunidad comunidad, Integer factor)
    {
        StringBuffer hql = new StringBuffer(" select sum(importe) from Movimiento ");
        hql.append(" where fecha >= :desde ");
        hql.append(" and fecha <= :hasta ");
        hql.append(" and comunidad.id = :comunidad ");
        hql.append(" and concepto.tipoConcepto.factor = :factor ");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("desde", desde);
        params.put("hasta", hasta);
        params.put("comunidad", comunidad.getId());
        params.put("factor", factor);

        return (Double) getJpaTemplate().findByNamedParams(hql.toString(), params).get(0);
    }

    @SuppressWarnings("unchecked")
	public Map<Date, Float> getMonthlySavings(Comunidad comunidad, Date from, Date to)
    {
    	String hql = " from Movimiento ";
    	hql += " where comunidad.id = :idComunidad ";
    	hql += " and fecha >= :from ";
    	hql += " and fecha <= :to ";
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("idComunidad", comunidad.getId());
    	params.put("from", from);
    	params.put("to", to);
    	
    	List<Movimiento> movimientos = this.getJpaTemplate().findByNamedParams(hql, params);
    	
    	Map<Date, Float> savings = new HashMap<Date, Float>();
    	
    	if (movimientos != null)
    	{
    		for (Movimiento movimiento : movimientos)
    		{
    			FirstDayOfMonth date = new FirstDayOfMonth(movimiento.getFecha());
    			Float saving = savings.get(date);
    			
    			if (saving == null)
    			{
    				savings.put(date, movimiento.getSaldo());
    			}
    			else
    			{
    				savings.remove(date);
    				savings.put(date, saving + movimiento.getSaldo());
    			}
    		}
    	}
    	
    	return savings;
    }
    
    public Map<Date, Double> getTotalesMensuales(Concepto concepto, Date desde, Date hasta)
    {
		Map<Date, Double> totales = new HashMap<Date, Double>();

        String hql = "from Movimiento where concepto = :concepto and fecha >= :desde and fecha <= :hasta ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("concepto", concepto);
        params.put("desde", desde);
        params.put("hasta", hasta);

        @SuppressWarnings("unchecked")
		List<Movimiento> movimientos = getJpaTemplate().findByNamedParams(hql, params);

		if (movimientos != null)
		{
			logger.debug("Se han encontrado "+ movimientos.size() +" movimientos.");

			for (Movimiento movimiento : movimientos)
			{
				FirstDayOfMonth date = new FirstDayOfMonth(movimiento.getFecha());
				Double total = totales.get(date);

				if (total != null)
				{
					logger.debug("Sumando "+ movimiento.getImporte() +" al total "+ total +" para "+ date +".");
					totales.put(date, total + movimiento.getImporte());
				}
				else
				{
                    totales.put(date, new Double(movimiento.getImporte()));
					logger.debug("Grabando el primer valor ("+ movimiento.getImporte() +") para "+ date +".");
				}
			}
		}
		else
		{
			logger.debug("No se han encontrado movimientos.");
		}

		this.fillEmptyMonths(totales, desde, hasta);
		
		return totales;
    }
    
    private void fillEmptyMonths(Map<Date, Double> totales, Date from, Date to)
    {
    	List<Date> dates = new ArrayList<Date>(totales.keySet());
    	
    	Calendar aux = Calendar.getInstance();
    	aux.setTime(from);
    	Calendar top = Calendar.getInstance();
    	top.setTime(to);
    	
    	while (aux.before(top))
    	{
    		if (!this.containsMonth(dates, aux))
    		{
    			totales.put(aux.getTime(), 0d);
    		}
    		
    		aux.add(Calendar.MONTH, 1);
    	}
    }
    
    private Boolean containsMonth(List<Date> dates, Calendar item)
    {
    	Calendar aux = Calendar.getInstance();
    	
    	for (Date date : dates)
    	{
    		aux.setTime(date);
    		
    		if (aux.get(Calendar.MONTH) == item.get(Calendar.MONTH))
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }

    public Map<Concepto, Map<Date, Double>> getTotalesMensuales(List<Concepto> conceptos, Date desde, Date hasta)
    {
		Map<Concepto, Map<Date, Double>> totales = new HashMap<Concepto, Map<Date, Double>>();

		for (Concepto concepto : conceptos)
		{
			Map<Date, Double> totalesConcepto = getTotalesMensuales(concepto, desde, hasta);

			totales.put(concepto, totalesConcepto);
		}

		return totales;
    }

    @SuppressWarnings("unchecked")
	public List<Movimiento> retrieveByFactor(Comunidad comunidad, Integer mes, Integer anio, Integer factor)
    {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.MONTH, mes - 1);
        date.set(Calendar.YEAR, anio);

        FirstDayOfMonth desde = new FirstDayOfMonth(date);
        LastDayOfMonth hasta = new LastDayOfMonth(date);

        logger.info("Buscando movimientos entre '" + desde + "' y '" + hasta + "'.");

        String hql = "from Movimiento where fecha between :desde and :hasta and comunidad = :comunidad and concepto.tipoConcepto.factor = :factor ";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("desde", desde);
        params.put("hasta", hasta);
        params.put("comunidad", comunidad);
        params.put("factor", factor);

        return getJpaTemplate().findByNamedParams(hql, params);
    }

    public Boolean alcanzaPresupuesto(Double ingreso, Double ahorroProg, Double ahorroReal, Date fecha)
    { 
    	Calendar aux = Calendar.getInstance();
    	aux.setTime(new LastDayOfMonth(fecha));
    	
    	Integer diasMes = aux.get(Calendar.DATE);
    	logger.debug("El mes de la fecha '"+ fecha +"' tiene "+ diasMes +" dias.");
    	
    	aux.setTime(fecha);
    	Integer diasRestantes = diasMes - aux.get(Calendar.DATE);
    	logger.debug("A la fecha dada, al mes le quedan "+ diasRestantes +" dias.");
    	
    	// Presupuesto diario programado para el mes
    	Double presuDia = (ingreso - ahorroProg) / diasMes;
    	logger.debug(" (I ["+ ingreso +"] - A ["+ ahorroProg +"]) / dias ["+ diasMes +"] = Pd ["+ presuDia +"]");
    	
    	
    	Double presuRestante = (ahorroReal - ahorroProg) / diasRestantes;
    	logger.debug(" (Ar ["+ ahorroReal +"] - Ap ["+ ahorroProg +"]) / diasRestantes ["+ diasRestantes +"] = Pr ["+ presuRestante +"]");
    	
    	return presuRestante >= presuDia;
    }
    
    public Double calcularIngresoTotal(Comunidad comunidad, Date fecha)
    {
    	Calendar aux = Calendar.getInstance();
    	aux.setTime(fecha);
    	
    	List<Movimiento> ingresos = retrieveIngresosMes(comunidad, aux.get(Calendar.MONTH) + 1, aux.get(Calendar.YEAR));
    	Double total = 0d;
    	
    	if (ingresos != null)
    	{
    		for (Movimiento mov : ingresos)
    		{
    			total += mov.getImporte();
    		}
    	}
    	
    	return total;
    }
    
    public Double calcularEgresoTotal(Comunidad comunidad, Date fecha)
    {
    	Calendar aux = Calendar.getInstance();
    	aux.setTime(fecha);
    	
    	try
    	{
        	List<Movimiento> egresos = retrieveEgresosMes(comunidad, aux.get(Calendar.MONTH) + 1, aux.get(Calendar.YEAR));
        	Double total = 0d;
        	
        	if (egresos != null)
        	{
        		for (Movimiento mov : egresos)
        		{
        			total += mov.getImporte();
        		}
        	}
        	
        	return total;
    	}
    	catch (Exception ex)
    	{
    		throw new RuntimeException(ex);
    	}
    }
    
    public List<Movimiento> retrieveIngresosMes(Comunidad comunidad, Integer mes, Integer anio)
    {
    	return this.retrieveByFactor(comunidad, mes, anio, 1);
    }
    
    public List<Movimiento> retrieveEgresosMes(Comunidad comunidad, Integer mes, Integer anio) throws Exception
    {
    	return this.retrieveByFactor(comunidad, mes, anio, -1);
    }
*/
}