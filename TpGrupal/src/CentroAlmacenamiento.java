


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class CentroAlmacenamiento {

	private static final int TEMP_MENOS18 = -18;
	private static final int TEMP_TRESGRADOS= 3;
	private static final int CADUCIDAD_PFIZER= 30;
	private static final int CADUCIDAD_MODERNA= 60;
	private HashMap<String, LinkedList<Vacuna>> vacunas;
	private HashMap<String, ArrayList<Vacuna>> vacunasVencidas;
	private int vacunasDisponibles;
	
	
	public CentroAlmacenamiento() {
		
		this.vacunasDisponibles = 0;
		vacunas = new HashMap<String, LinkedList<Vacuna>>();
		vacunas.put("Pfizer", new LinkedList<Vacuna>());
		vacunas.put("Moderna", new LinkedList<Vacuna>());
		vacunas.put("AstraZeneca", new LinkedList<Vacuna>());
		vacunas.put("Sputnik", new LinkedList<Vacuna>());
		vacunas.put("Sinopharm", new LinkedList<Vacuna>());
		
		vacunasVencidas = new HashMap<String, ArrayList<Vacuna>>();
		
		vacunasVencidas.put("Pfizer", new ArrayList<Vacuna>());
		vacunasVencidas.put("Moderna", new ArrayList<Vacuna>());
		vacunasVencidas.put("AstraZeneca", new ArrayList<Vacuna>());
		vacunasVencidas.put("Sputnik", new ArrayList<Vacuna>());
		vacunasVencidas.put("Sinopharm", new ArrayList<Vacuna>());

	}

	
	// obtenenes la vacuna que le corresponde al paciente en caso de que no esten vencidas
	@SuppressWarnings("unlikely-arg-type")
	public Vacuna obtenerVacuna(ArrayList<String> nombres) {

		Iterator<String> tipoVacunas = vacunas.keySet().iterator();
		
		while(tipoVacunas.hasNext()) {
			
			String tipoVacuna = tipoVacunas.next();

			for(String vacunasParaRetirar: nombres) {

				if(tipoVacuna.equals(vacunasParaRetirar)) {

					if(!vacunas.get(tipoVacuna).isEmpty()) {
						

						switch(vacunasParaRetirar) {
						
						case "Pfizer":
							
							for(int i = 0; i < vacunas.get(tipoVacuna).size(); i++) {
								if(vacunas.get(tipoVacuna).get(i).isDisponible() && 
										((Pfizer) vacunas.get(tipoVacuna).get(i)).isVencida() == false) {
									vacunas.get(tipoVacuna).get(i).setDisponible(false);
									return vacunas.get(tipoVacuna).get(i);
								}
							}
						
							
							break;
						case "Moderna":
						
							for(int i = 0; i < vacunas.get(tipoVacuna).size(); i++) {
								if(vacunas.get(tipoVacuna).get(i).isDisponible() &&
										((Moderna) vacunas.get(tipoVacuna).get(i)).isVencida() == false) {
									vacunas.get(tipoVacuna).get(i).setDisponible(false);
									return vacunas.get(tipoVacuna).get(i);
								}
							}
						
							
							break;
						case "AstraZeneca":
							
							for(int i = 0; i < vacunas.get(tipoVacuna).size(); i++) {
								if(vacunas.get(tipoVacuna).get(i).isDisponible()) {
									vacunas.get(tipoVacuna).get(i).setDisponible(false);
									return vacunas.get(tipoVacuna).get(i);
								}
							}
						
							
							break;
						case "Sputnik":
							
							for(int i = 0; i < vacunas.get(tipoVacuna).size(); i++) {
								if(vacunas.get(tipoVacuna).get(i).isDisponible()) {
									vacunas.get(tipoVacuna).get(i).setDisponible(false);
									return vacunas.get(tipoVacuna).get(i);
								}
							}
						
							
							break;
						case "Sinopharm":
							
							for(int i = 0; i < vacunas.get(tipoVacuna).size(); i++) {
								if(vacunas.get(tipoVacuna).get(i).isDisponible()) {
									vacunas.get(tipoVacuna).get(i).setDisponible(false);
									return vacunas.get(tipoVacuna).get(i);
								}
							}
						
							
							break;
							
						 default:
							return null;
						
						}
	
					}
		
				}
				
			}
			
		}

		return null;
	
	}
	
	
	public boolean retirarVacuna(Vacuna vacuna) {
		
		Iterator<String> tipoVacunas = vacunas.keySet().iterator();
		
		while(tipoVacunas.hasNext()) {
				
				String tipoVacuna = tipoVacunas.next();
				
				if(tipoVacuna.equals(vacuna.getNombre())) {
					
					Iterator<Vacuna> vacunaARetirar = vacunas.get(tipoVacuna).iterator();
					
					while(vacunaARetirar.hasNext()) {
						
						
						Vacuna retirar  = vacunaARetirar.next();
						
						vacunaARetirar.remove();
						
						return true;
					}

					
				}
				
		}
		return false;
	}
	
	public void devolverVacunaAlStock(Vacuna vacuna) {
	
	
		if(vacuna instanceof Pfizer) {
			Pfizer pfizer = (Pfizer)vacuna;
			pfizer.setDisponible(true);
		}else if(vacuna instanceof Moderna) {
			Moderna moderna = (Moderna)vacuna;
			moderna.setDisponible(true);
		}else  if(vacuna instanceof Astrazeneca) {
			Astrazeneca astrazeneca = (Astrazeneca)vacuna;
			astrazeneca.setDisponible(true);
		}else if(vacuna instanceof Sputnik) {
			Sputnik sputnik = (Sputnik)vacuna;
			sputnik.setDisponible(true);
		}else if(vacuna instanceof Sinopharm) {
			Sinopharm sinopharm = (Sinopharm)vacuna;
			sinopharm.setDisponible(true);
			}

		actualizarEstadoVacunas(Fecha.hoy(), vacuna.getNombre());

	}
	
	public void agregarVacunas(String nombre, int cant, Fecha ingreso) {// listo
		if (cant <= 0)
			throw new RuntimeException("La cantidad debe ser mayor a 0");
		else if (nombre.equals("Pfizer"))
			rellenarVacunasMenos18(nombre, ingreso, TEMP_MENOS18, 0, CADUCIDAD_PFIZER, false, cant);
		else if (nombre.equals("Moderna"))
			rellenarVacunasMenos18(nombre, ingreso, TEMP_MENOS18, 0, CADUCIDAD_MODERNA, false, cant);
		else if (nombre.equals("Sinopharm"))
			rellenarVacunas3(nombre, cant, ingreso, TEMP_TRESGRADOS);
		else if (nombre.equals("AstraZeneca"))
			rellenarVacunas3(nombre, cant, ingreso, TEMP_TRESGRADOS);
		else if (nombre.equals("Sputnik"))
			rellenarVacunas3(nombre, cant, ingreso, TEMP_TRESGRADOS);
		else
			throw new RuntimeException("Ingrese nuevamente el nombre");
	}

	public void verificarVacunasVencidas(Fecha fecha) {
		actualizarEstadoVacunas(fecha, "Pfizer");
		actualizarEstadoVacunas(fecha, "Moderna");
	}

	
	
	
	private void actualizarEstadoVacunas(Fecha fecha, String nombre) {
		
		if (nombre.equals("Pfizer")) {
			Iterator vacunasPfizerList = vacunas.get("Pfizer").iterator();
			while (vacunasPfizerList.hasNext()) {
				Pfizer pfizer = (Pfizer) vacunasPfizerList.next();
				int mesesAlmacenada = Math.abs(Fecha.diferenciaMes(pfizer.getFechaIngreso(), fecha));
				
				// comprobamos que no sea el ultimo dia del mes del vencimiento
				if(mesesAlmacenada == 1 && pfizer.getFechaIngreso().dia() != fecha.dia()) {

					pfizer.setVencida(true);
					vacunasVencidas.get("Pfizer").add(pfizer);
				
				}else if(mesesAlmacenada > 1) {
					pfizer.setVencida(true);
					vacunasVencidas.get("Pfizer").add(pfizer);
				
				}

				
			}
		} else if (nombre.equals("Moderna")) {

			Iterator vacunaModernaList = vacunas.get("Moderna").iterator();
			while (vacunaModernaList.hasNext()) {
				Moderna moderna = (Moderna) vacunaModernaList.next();
				int mesesAlmacenada = Math.abs(Fecha.diferenciaMes(moderna.getFechaIngreso(), fecha));

				// comprobamos que no sea el ultimo dia del mes del vencimiento
				if(mesesAlmacenada == 2 && moderna.getFechaIngreso().dia() != fecha.dia()) {
					moderna.setVencida(true);
					vacunasVencidas.get("Moderna").add(moderna);
					
				}else if(mesesAlmacenada > 2) {
					moderna.setVencida(true);
					vacunasVencidas.get("Moderna").add(moderna);
					
				}
				
			}
		}

	}

	public int obtenerCantidadDevacunasPorNombre(String nombre) {
		if (!compararN(nombre))
			throw new RuntimeException("No existe la vacuna: " + nombre);

		int vacunasDisponiblesPorNombre = 0;
		
		
		for(int i = 0; i < vacunas.get(nombre).size(); i++) {
			
			if(nombre.equals("Moderna")) {
				if(((Moderna)vacunas.get("Moderna").get(i)).isVencida() == false) {
					vacunasDisponiblesPorNombre ++;
				}
			}else if(nombre.equals("Pfizer")) {
				if(((Pfizer)vacunas.get("Pfizer").get(i)).isVencida() == false) {
					vacunasDisponiblesPorNombre ++;
				}
		    }else {
		    	vacunasDisponiblesPorNombre ++;
		    }
	
		}
		return vacunasDisponiblesPorNombre;
	}
	
 public int vacunasDisponibles() {
		
	this.vacunasDisponibles = 0;

	Iterator<String> tipoVacunas = vacunas.keySet().iterator();
		
		while(tipoVacunas.hasNext()) {
			
			String tipoVacuna = tipoVacunas.next();


						switch(tipoVacuna) {
						
						case "Pfizer":
						
							for(int i = 0; i < vacunas.get(tipoVacuna).size(); i++) {
								if(vacunas.get(tipoVacuna).get(i).isDisponible() && 
										((Pfizer) vacunas.get(tipoVacuna).get(i)).isVencida() == false)
									vacunasDisponibles++;
							}
							
							break;
						case "Moderna":
						
							for(int i = 0; i < vacunas.get(tipoVacuna).size(); i++) {
								if(vacunas.get(tipoVacuna).get(i).isDisponible() && 
										((Moderna) vacunas.get(tipoVacuna).get(i)).isVencida() == false)
									vacunasDisponibles++;
							}
							break;
						case "AstraZeneca":
							for(int i = 0; i < vacunas.get(tipoVacuna).size(); i++) {
								if(vacunas.get(tipoVacuna).get(i).isDisponible())
									vacunasDisponibles++;
							}
							break;
						case "Sputnik":
							for(int i = 0; i < vacunas.get(tipoVacuna).size(); i++) {
								if(vacunas.get(tipoVacuna).get(i).isDisponible())
									vacunasDisponibles++;
							}
							break;
						case "Sinopharm":
							for(int i = 0; i < vacunas.get(tipoVacuna).size(); i++) {
								if(vacunas.get(tipoVacuna).get(i).isDisponible())
									vacunasDisponibles++;
							}
							break;
						default:
							break;
						
						}
	
				
		}
		return vacunasDisponibles;
	}
	
	
	public int cantidadVacunasAplicablesAlPaciente(ArrayList<String> nombres) {
		
		int cantidad = 0;
		for(int i = 0; i < nombres.size(); i++) {
			
			for(Vacuna vacuna: vacunas.get(nombres.get(i))) {

				if(vacuna instanceof Moderna) {
					
					if(((Moderna) vacuna).isVencida() == false)
						cantidad++;
				}else if( vacuna instanceof Pfizer) {
					
					if(((Pfizer) vacuna).isVencida() == false) {
						cantidad++;
					}
				}else {
					cantidad++;
				}
		
			}
		}

		return cantidad;
	}

	public Map<String, Integer> getVacunasVencidas() {
	
		Map<String, Integer> vencidas = new HashMap<String, Integer>();
		
		vencidas.put("Pfizer", vacunasVencidas.get("Pfizer").size());
		vencidas.put("Moderna", vacunasVencidas.get("Moderna").size());

		
		return vencidas;
	}

	private void rellenarVacunasMenos18(String nombre, Fecha ing, int temp, int diasAlmacenada, int diasCaducidad,
			boolean isVencida, int cant) {
		
		if(nombre.equals("Moderna")) {
			for (int i = 0; i < cant; i++) {
				vacunas.get(nombre).add(new Moderna(nombre, ing, temp, isVencida));
			}
		}else if (nombre.equals("Pfizer")) {
			for (int i = 0; i < cant; i++) {
				vacunas.get(nombre).add(new Pfizer(nombre, ing, temp, isVencida));
			}
		}
	}

	private void rellenarVacunas3(String nombre, int cant, Fecha ingreso, int temp) {
		for (int i = 0; i < cant; i++) {
			
			switch(nombre) {
			case "Sinopharm": 
				vacunas.get(nombre).add(new Sinopharm(nombre, ingreso, temp));
				break;
			case "AstraZeneca": 
				vacunas.get(nombre).add(new Astrazeneca(nombre, ingreso, temp));
				break;
			case "Sputnik":
				vacunas.get(nombre).add(new Sputnik(nombre, ingreso, temp));
				break;
				default: 
					break;
				
			}
		}
	}

	private boolean compararN(String nombre) {
		String[] nombres = { "Pfizer", "Moderna", "Sputnik", "Sinopharm", "AstraZeneca" };
		boolean algunNombre = false;
		for (int i = 0; i < nombres.length; i++) {
			algunNombre = algunNombre || nombre.equals(nombres[i]);
		}
		return algunNombre;
	}

	
	@Override
	public String toString() {
		 StringBuilder datosVacunas = new 
                 StringBuilder();
		
		 
		 
		 datosVacunas.append("                                       "
					+ "----------------------------------").append("\n")
		 .append("                                       ")
		 .append("Centro: Almacenamiento").append("\n")
		 .append("                                       "
					+ "----------------------------------").append("\n")
		 
		 
		 .append("Vacunas Disponibles en total: ").append(
				 
				 obtenerCantidadDevacunasPorNombre("Pfizer")+
				 obtenerCantidadDevacunasPorNombre("Moderna")+
				 obtenerCantidadDevacunasPorNombre("Sputnik")+
				 obtenerCantidadDevacunasPorNombre("Sinopharm")+
				 obtenerCantidadDevacunasPorNombre("AstraZeneca")
				 
				 ).append("\n")	
		 .append("--------------------------------").append("\n")
		 .append("Vacunas por nombre").append("\n");
		  
		 
		 datosVacunas.append("Al dia de la fecha: ").append(Fecha.hoy()).append("\n").append("vacunas disponibles por nombre").append("\n");
		  
		 datosVacunas.append("Vacunas Pfizer totales: ").append(obtenerCantidadDevacunasPorNombre("Pfizer")).append("\n");
		 datosVacunas.append("Vacunas Moderna totales: ").append(obtenerCantidadDevacunasPorNombre("Moderna")).append("\n");
		 datosVacunas.append("Vacunas Sputnik totales: ").append(obtenerCantidadDevacunasPorNombre("Sputnik")).append("\n");
		 datosVacunas.append("Vacunas Sinopharm totales: ").append(obtenerCantidadDevacunasPorNombre("Sinopharm")).append("\n");
		 datosVacunas.append("Vacunas Astranezca totales: ").append(obtenerCantidadDevacunasPorNombre("AstraZeneca")).append("\n");
		 datosVacunas.append("\n");
		 datosVacunas.append("****************************").append("\n");
		 datosVacunas.append("*Simular fecha posterior*").append("\n");
		 datosVacunas.append("****************************").append("\n");
		 datosVacunas.append("\n");
		 
		 Fecha.setFechaHoy(20,8,2021);
		 verificarVacunasVencidas(Fecha.hoy());

		 datosVacunas.append("Al dia de la fecha: ").append(Fecha.hoy()).append("\n").append("vacunas disponibles por nombre").append("\n");
		
		  
		 datosVacunas.append("Vacunas Pfizer totales: ").append(obtenerCantidadDevacunasPorNombre("Pfizer")).append("\n");
		 datosVacunas.append("Vacunas Moderna totales: ").append(obtenerCantidadDevacunasPorNombre("Moderna")).append("\n");
		 datosVacunas.append("Vacunas Sputnik totales: ").append(obtenerCantidadDevacunasPorNombre("Sputnik")).append("\n");
		 datosVacunas.append("Vacunas Sinopharm totales: ").append(obtenerCantidadDevacunasPorNombre("Sinopharm")).append("\n");
		 datosVacunas.append("Vacunas Astranezca totales: ").append(obtenerCantidadDevacunasPorNombre("AstraZeneca")).append("\n");
		 datosVacunas.append("****************************").append("\n");
		
		
		return datosVacunas.toString();
		
	}


}
