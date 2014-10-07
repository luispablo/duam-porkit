package ar.com.duam.porky.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ar.com.duam.porky.model.Comunidad;
import ar.com.duam.porky.model.Concepto;
import ar.com.duam.porky.model.Movimiento;
import ar.com.duam.porky.model.TipoConcepto;
import ar.com.duam.porky.model.Usuario;

import com.google.inject.ImplementedBy;

@ImplementedBy(JpaService.class)
public interface IService 
{

	public <T> T find(Class<T> entityClass, Object primaryKey);
	
	public void remove(Object obj); 
	
	@SuppressWarnings("rawtypes")
	public List findAll(Class clazz);
	
	public <T> T merge(T entity);
	
    public Usuario findUsuarioByUsername(String username);

    public Double getTotalPorFactor(Date desde, Date hasta, Comunidad comunidad, Integer factor);

	public Double calcularIngresoTotal(Comunidad comunidad, Date fecha);
	
	public Double calcularEgresoTotal(Comunidad comunidad, Date fecha);

	public Boolean alcanzaPresupuesto(Double ingreso, Double ahorroProg, Double ahorroReal, Date fecha);
	
	public Map<Date, Float> getMonthlySavings(Comunidad comunidad, Date from, Date to);
	
	public List<Movimiento> retrieveByFactor(Comunidad comunidad, Integer mes, Integer anio, Integer factor);

	public List<Movimiento> getMovimientos(Usuario usuario, Date desde, Date hasta, int desdeFila, int hastaFila);
	
	public List<Movimiento> findMovimientos(Comunidad comunidad, Date desde, Date hasta, Concepto concepto, TipoConcepto tipoConcepto, String detalle, List<Object[]> orderByFields);

	public Map<Date, Double> getTotalesMensuales(Concepto concepto, Date desde, Date hasta);

	public Map<Concepto, Map<Date, Double>> getTotalesMensuales(List<Concepto> conceptos, Date desde, Date hasta);

	public List<Movimiento> retrieveEgresosMes(Comunidad comunidad, Integer mes, Integer anio) throws Exception;

	public List<String[]> userRanking();
	
}
