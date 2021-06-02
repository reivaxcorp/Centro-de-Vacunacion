


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

	
	// retiramos la vacuna que le corresponde al paciente en caso de que no esten vencidas
	@SuppressWarnings("unlikely-arg-type")
	public Vacuna retirarVacuna(ArrayList<String> nombres) {

		Iterator<String> tipoVacunas = vacunas.keySet().iterator();
		
		while(tipoVacunas.hasNext()) {
			
			String tipoVacuna = tipoVacunas.next();

			for(String vacunasParaRetirar: nombres) {

				if(tipoVacuna.equals(vacunasParaRetirar)) {

					if(!vacunas.get(tipoVacuna).isEmpty()) {
						
						Vacuna vacunaPaciente = null;

						switch(vacunasParaRetirar) {
						
						case "Pfizer":
							Pfizer pfizer = (Pfizer) vacunas.get(tipoVacuna).getFirst();
							vacunaPaciente  = new Pfizer(pfizer.getNombre(), pfizer.getFechaIngreso(), pfizer.getTemp(), pfizer.isVencida());
							break;
						case "Moderna":
						
							Moderna moderna = (Moderna) vacunas.get(tipoVacuna).getFirst();
							vacunaPaciente  = new Moderna(moderna.getNombre(), moderna.getFechaIngreso(), moderna.getTemp(), moderna.isVencida());
							break;
						case "AstraZeneca":
							Astrazeneca astrazeneca = (Astrazeneca) vacunas.get(tipoVacuna).getFirst();
							vacunaPaciente  = new Astrazeneca(astrazeneca.getNombre(), astrazeneca.getFechaIngreso(), astrazeneca.getTemp());

							break;
						case "Sputnik":
							Sputnik sputnik = (Sputnik) vacunas.get(tipoVacuna).getFirst();
							vacunaPaciente  = new Sputnik(sputnik.getNombre(), sputnik.getFechaIngreso(), sputnik.getTemp());

							break;
						case "Sinopharm":
							Sinopharm sinopharm = (Sinopharm) vacunas.get(tipoVacuna).getFirst();
							vacunaPaciente  = new Sinopharm(sinopharm.getNombre(), sinopharm.getFechaIngreso(), sinopharm.getTemp());

							break;
						
						}
	
						vacunas.get(tipoVacuna).removeFirst(); // removemos la vacuna, ya tenemos una copia
						return vacunaPaciente;
					}
		
				}
				
			}
			
		}

		return null;
	
	}
	
	
	public void devolverVacunaAlStock(Vacuna vacuna) {
	
	
		if(vacuna instanceof Pfizer)
			vacunas.get(vacuna.getNombre()).add((Pfizer)vacuna);
		else if(vacuna instanceof Moderna)
			vacunas.get(vacuna.getNombre()).add((Moderna)vacuna);
		else if(vacuna instanceof Astrazeneca)
			vacunas.get(vacuna.getNombre()).add((Astrazeneca)vacuna);
		else if(vacuna instanceof Sputnik)
			vacunas.get(vacuna.getNombre()).add((Sputnik)vacuna);
		else if(vacuna instanceof Sinopharm)
			vacunas.get(vacuna.getNombre()).add((Sinopharm)vacuna);


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
				
				
				if(mesesAlmacenada == 1 && pfizer.getFechaIngreso().dia() != fecha.dia()) {
				
					pfizer.setVencida(true);
					vacunasVencidas.get("Pfizer").add(pfizer);
				
				}

				
			}
		} else if (nombre.equals("Moderna")) {

			Iterator vacunaModernaList = vacunas.get("Moderna").iterator();
			while (vacunaModernaList.hasNext()) {
				Moderna moderna = (Moderna) vacunaModernaList.next();
				int mesesAlmacenada = Math.abs(Fecha.diferenciaMes(moderna.getFechaIngreso(), fecha));
				
				if(mesesAlmacenada == 2 && moderna.getFechaIngreso().dia() != fecha.dia()) {
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

		vacunas.forEach((nombreVacuna, listVacuna)-> {

			for(Vacuna vacuna: listVacuna) {

					this.vacunasDisponibles ++;
			
			}

		});
		return vacunasDisponibles;
	}
	
	
	public int cantidadVacunasDisponiblePorNombre(ArrayList<String> nombres) {
		
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
		String[] nombres = { "Pfizer", "Moderna", "Sputnik", "Sinopharm", "Astranezca" };
		boolean algunNombre = false;
		for (int i = 0; i < nombres.length; i++) {
			algunNombre = algunNombre || nombre.equals(nombres[i]);
		}
		return algunNombre;
	}


	public static void main(String[] args) {
		// test para CentroAlmacenamiento
		/*CentroAlmacenamiento n = new CentroAlmacenamiento();
		n.agregarVacunas("Pfizer", 12, new Fecha(26, 01, 2021)); // no vencidas = 12
		n.agregarVacunas("Pfizer", 6, new Fecha(27, 06, 2021)); // vencidas = 6
		
		n.agregarVacunas("Moderna", 2, new Fecha(27, 07, 2021)); // vencidas = 2
		n.agregarVacunas("Moderna", 10, new Fecha(27, 01, 2021)); // no vencidas = 10
		
		n.agregarVacunas("Sputnik", 9, new Fecha(26, 10, 2022)); // no vencidas = 9

		System.out.println("Vacunas Pfizer totales: " + n.obtenerCantidadDevacunasPorNombre("Pfizer"));
		System.out.println("Vacunas Moderna totales: " + n.obtenerCantidadDevacunasPorNombre("Moderna"));
		System.out.println("Vacunas Sputnik totales: " + n.obtenerCantidadDevacunasPorNombre("Sputnik"));
		System.out.println("Vacunas Sinopharm totales: " + n.obtenerCantidadDevacunasPorNombre("Sinopharm"));
		System.out.println("Vacunas Astranezca totales: " + n.obtenerCantidadDevacunasPorNombre("Astranezca"));
*/
	}


}
