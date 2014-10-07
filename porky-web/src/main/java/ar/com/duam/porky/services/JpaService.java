package ar.com.duam.porky.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.google.inject.persist.Transactional;

import ar.com.duam.api.date.FirstDayOfMonth;
import ar.com.duam.api.date.LastDayOfMonth;
import ar.com.duam.porky.model.Comunidad;
import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.model.Movimiento;
import ar.com.duam.porky.model.TipoConcepto;
import ar.com.duam.porky.model.Usuario;

public class JpaService implements IService 
{
	private static final Logger log = Logger.getLogger(JpaService.class);
	
	@Inject
    private EntityManager em;

	public <T> T find(Class<T> entityClass, Object primaryKey)
	{
		return em.find(entityClass, primaryKey);
	}
	
	@Transactional
	public void remove(Object obj)
	{
		em.remove(obj);
	}
	
	@Transactional
	public <T> T merge(T entity)
	{
		return em.merge(entity);
	}
	
	@Override
	public Usuario findUsuarioByUsername(String username) 
	{
    	Query query = em.createQuery("from Usuario where nombreUsuario = :username");
    	query.setParameter("username", username);
    	
        return (Usuario) query.getSingleResult();
	}

	@Override
	public Double getTotalPorFactor(Date desde, Date hasta,	Comunidad comunidad, Integer factor) 
	{
        StringBuffer hql = new StringBuffer(" select sum(importe) from Movimiento ");
        hql.append(" where fecha >= :desde ");
        hql.append(" and fecha <= :hasta ");
        hql.append(" and comunidad.id = :comunidad ");
        hql.append(" and concepto.tipoConcepto.factor = :factor ");

        Query query = em.createQuery(hql.toString());

        query.setParameter("desde", desde);
        query.setParameter("hasta", hasta);
        query.setParameter("comunidad", comunidad.getId());
        query.setParameter("factor", factor);

        return (Double) query.getSingleResult(); 
	}

	@Override
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

	@Override
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

	@SuppressWarnings("unchecked")
	public List<Movimiento> retrieveByFactor(Comunidad comunidad, Integer mes, Integer anio, Integer factor)
    {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.MONTH, mes - 1);
        date.set(Calendar.YEAR, anio);

        FirstDayOfMonth desde = new FirstDayOfMonth(date);
        LastDayOfMonth hasta = new LastDayOfMonth(date);

        log.info("Buscando movimientos entre '" + desde + "' y '" + hasta + "'.");

        Query query = em.createQuery("from Movimiento where fecha between :desde and :hasta and comunidad = :comunidad and concepto.tipoConcepto.factor = :factor ");

        query.setParameter("desde", desde);
        query.setParameter("hasta", hasta);
        query.setParameter("comunidad", comunidad);
        query.setParameter("factor", factor);

        return query.getResultList(); 
    }
	
    public List<Movimiento> retrieveIngresosMes(Comunidad comunidad, Integer mes, Integer anio)
    {
    	return this.retrieveByFactor(comunidad, mes, anio, 1);
    }
    
    public List<Movimiento> retrieveEgresosMes(Comunidad comunidad, Integer mes, Integer anio) throws Exception
    {
    	return this.retrieveByFactor(comunidad, mes, anio, -1);
    }
    
    public Boolean alcanzaPresupuesto(Double ingreso, Double ahorroProg, Double ahorroReal, Date fecha)
    { 
    	Calendar aux = Calendar.getInstance();
    	aux.setTime(new LastDayOfMonth(fecha));
    	
    	Integer diasMes = aux.get(Calendar.DATE);
    	log.debug("El mes de la fecha '"+ fecha +"' tiene "+ diasMes +" dias.");
    	
    	aux.setTime(fecha);
    	Integer diasRestantes = diasMes - aux.get(Calendar.DATE);
    	log.debug("A la fecha dada, al mes le quedan "+ diasRestantes +" dias.");
    	
    	// Presupuesto diario programado para el mes
    	Double presuDia = (ingreso - ahorroProg) / diasMes;
    	log.debug(" (I ["+ ingreso +"] - A ["+ ahorroProg +"]) / dias ["+ diasMes +"] = Pd ["+ presuDia +"]");
    	
    	
    	Double presuRestante = (ahorroReal - ahorroProg) / diasRestantes;
    	log.debug(" (Ar ["+ ahorroReal +"] - Ap ["+ ahorroProg +"]) / diasRestantes ["+ diasRestantes +"] = Pr ["+ presuRestante +"]");
    	
    	return presuRestante >= presuDia;
    }

	@SuppressWarnings("unchecked")
	public Map<Date, Float> getMonthlySavings(Comunidad comunidad, Date from, Date to)
    {
		Query query = em.createQuery("from Movimiento where comunidad.id = :idComunidad and fecha >= :from and fecha <= :to ");
    	query.setParameter("idComunidad", comunidad.getId());
    	query.setParameter("from", from);
    	query.setParameter("to", to);
    	
    	List<Movimiento> movimientos = query.getResultList();
    	
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Movimiento> findMovimientos(Comunidad comunidad, Date desde, Date hasta, Concepto concepto, TipoConcepto tipoConcepto, String detalle, List<Object[]> orderByFields) 
	{
        StringBuffer hql = new StringBuffer(" from Movimiento ");
        Map<String, Object> params = new HashMap<String, Object>();
        Boolean firstCondition = true;

        if (comunidad != null)
        {
            log.debug("comunidad: "+ comunidad.getId());
            
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
        
        Query query = em.createQuery(hql.toString());
        
        for (String key : params.keySet())
        {
        	query.setParameter(key, params.get(key));
        }
        
        return query.getResultList();
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
    
    public Map<Date, Double> getTotalesMensuales(Concepto concepto, Date desde, Date hasta)
    {
    	Map<Date, Double> totales = new HashMap<Date, Double>();
    	
    	Query query = em.createQuery("from Movimiento where concepto = :concepto and fecha >= :desde and fecha <= :hasta ");
    	query.setParameter("concepto", concepto);
    	query.setParameter("desde", desde);
    	query.setParameter("hasta", hasta);

        @SuppressWarnings("unchecked")
		List<Movimiento> movimientos = query.getResultList();

		if (movimientos != null)
		{
			log.debug("Se han encontrado "+ movimientos.size() +" movimientos.");

			for (Movimiento movimiento : movimientos)
			{
				FirstDayOfMonth date = new FirstDayOfMonth(movimiento.getFecha());
				Double total = totales.get(date);

				if (total != null)
				{
					log.debug("Sumando "+ movimiento.getImporte() +" al total "+ total +" para "+ date +".");
					totales.put(date, total + movimiento.getImporte());
				}
				else
				{
                    totales.put(date, new Double(movimiento.getImporte()));
					log.debug("Grabando el primer valor ("+ movimiento.getImporte() +") para "+ date +".");
				}
			}
		}
		else
		{
			log.debug("No se han encontrado movimientos.");
		}

		this.fillEmptyMonths(totales, desde, hasta);
		
		return totales;
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

    public List<String[]> userRanking()
    {
    	return null;
    }

	@SuppressWarnings("rawtypes")
	@Override
	public List findAll(Class clazz) 
	{
		return em.createQuery("from "+ clazz.getSimpleName()).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Movimiento> getMovimientos(Usuario usuario, Date desde, Date hasta, int desdeFila, int hastaFila) 
	{
		Query query = em.createQuery("FROM "+ Movimiento.class.getSimpleName() +" WHERE comunidad = :comunidad AND fecha between :desde AND :hasta ORDER BY fecha DESC");
		query.setParameter("comunidad", usuario.getComunidad());
		query.setParameter("desde", desde);
		query.setParameter("hasta", hasta);
		query.setFirstResult(desdeFila);
		query.setMaxResults(hastaFila);
		
		return query.getResultList();
	}
    
}
