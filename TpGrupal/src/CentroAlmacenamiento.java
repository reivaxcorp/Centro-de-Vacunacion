
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class CentroAlmacenamiento {

	//private HashMap<String, ArrayList<Vacuna>> vacunasVencidas; // se usa
	//private ArrayList<VacunaConVencimiento> vacunasConVencimiento; // nuevo
	//private ArrayList<VacunaSinVencimiento> vacunasSinVencimiento; // nuevo
	
	private ArrayList<Vacuna> vacunas; // nuevo
	//private ArrayList<Vacuna> vacunasVencidas; // nuevo
	private Map<String, ArrayList<Vacuna>> vacunasVencidas;
	private int cantidadVacunasVencidas;

	public CentroAlmacenamiento() {

		//this.vacunasDisponibles = 0;

		/*vacunasVencidas = new HashMap<String, ArrayList<Vacuna>>();

		vacunasVencidas.put("Pfizer", new ArrayList<Vacuna>()); // se mantiene para obtener el O(1)
		vacunasVencidas.put("Moderna", new ArrayList<Vacuna>());
		vacunasVencidas.put("AstraZeneca", new ArrayList<Vacuna>());
		vacunasVencidas.put("Sputnik", new ArrayList<Vacuna>());
		vacunasVencidas.put("Sinopharm", new ArrayList<Vacuna>());
		 */
		//vacunasConVencimiento = new ArrayList<VacunaConVencimiento>();// nuevo
		//vacunasSinVencimiento = new ArrayList<VacunaSinVencimiento>();// nuevo
		
		
		vacunas = new ArrayList<Vacuna>();// nuevo
		//vacunasVencidas = new ArrayList<Vacuna>();
		
		this.vacunasVencidas = new HashMap<String, ArrayList<Vacuna>>();
		this.vacunasVencidas.put("Pfizer", new ArrayList<Vacuna>());
		this.vacunasVencidas.put("Moderna", new ArrayList<Vacuna>());
		this.cantidadVacunasVencidas = 0;
	}
	// ********************************************************************

	public void agregarVacunas(String nombre, int cant, Fecha ingreso) {// se modifico
		

		if (cant <= 0)
			throw new RuntimeException("La cantidad debe ser mayor a 0"); // nuevo
		
		/*else if (nombre.equals("Pfizer"))
			rellenarVacunasMenos18(nombre, ingreso, cant);
		else if (nombre.equals("Moderna"))
			rellenarVacunasMenos18(nombre, ingreso, cant);
		else if (nombre.equals("Sinopharm"))
			rellenarVacunas3(nombre, cant, ingreso);
		else if (nombre.equals("Astrazeneca"))
			rellenarVacunas3(nombre, cant, ingreso);
		else if (nombre.equals("Sputnik"))
			rellenarVacunas3(nombre, cant, ingreso);
		else
			throw new RuntimeException("Ingrese nuevamente el nombre");*/
		
		switch(nombre) {
		
		case "Pfizer":
			for (int i = 0; i < cant; i++) {
				vacunas.add(new Pfizer(ingreso));
			}
			break;
		case "Moderna":
			for (int i = 0; i < cant; i++) {
				vacunas.add(new Moderna(ingreso));
			}
			break;
		case "Sinopharm":
			for (int i = 0; i < cant; i++) {
				vacunas.add(new Sinopharm(ingreso));
			}
			break;
		case "Astrazeneca":
			for (int i = 0; i < cant; i++) {
				vacunas.add(new Astrazeneca(ingreso));
			}
			break;
		case "Sputnik":
			for (int i = 0; i < cant; i++) {
				vacunas.add(new Sputnik(ingreso));
			}
			break;
		 default:
			 throw new RuntimeException("No existe la vacuna: " + nombre); // nuevo
		
		}
	
	}

	// ********************************************************************
	/*private void rellenarVacunasMenos18(String nombre, Fecha ing, int cant) { // se modifico

		if (nombre.equals("Moderna")) {
			for (int i = 0; i < cant; i++) {
				// vacunas.get(nombre).add(new Moderna(ing, temp));
				vacunasConVencimiento.add(new Moderna(ing));

			}
		} else if (nombre.equals("Pfizer")) {
			for (int i = 0; i < cant; i++) {
				// vacunas.get(nombre).add(new Pfizer(ing, temp));
				vacunasConVencimiento.add(new Pfizer(ing));
			}
		}
	}*/

	// ********************************************************************
	/*private void rellenarVacunas3(String nombre, int cant, Fecha ingreso) { // se modifico
		for (int i = 0; i < cant; i++) {

			switch (nombre) {
			case "Sinopharm":
				// vacunas.get(nombre).add(new Sinopharm(ingreso, temp));
				vacunasSinVencimiento.add(new Sinopharm(ingreso));
				break;
			case "Astrazeneca":
				// vacunas.get(nombre).add(new Astrazeneca(ingreso, temp));
				vacunasSinVencimiento.add(new Astrazeneca(ingreso));
				break;
			case "Sputnik":
				// vacunas.get(nombre).add(new Sputnik(ingreso, temp));
				vacunasSinVencimiento.add(new Sputnik(ingreso));
				break;
			default:
				break;

			}
		}
	}*/

	// ********************************************************************
	// obtenes la vacuna que le corresponde al paciente en caso de que no esten
	// vencidas
	@SuppressWarnings("unlikely-arg-type")
	public Vacuna obtenerVacuna(Paciente p) { // no se modifico

		for (int i = 0; i < vacunas.size(); i++) {
			
			if (vacunas.get(i).pacientePuedeVacunarse(p) && 
					vacunas.get(i).isDisponible()) {
				vacunas.get(i).setDisponible(false);	
				return vacunas.get(i);
			}
			
		}

		/*for (int i = 0; i < vacunasSinVencimiento.size(); i++) {
			
			if (vacunasSinVencimiento.get(i).pacientePuedeVacunarse(p) && 
					vacunasSinVencimiento.get(i).isDisponible()) {
				vacunasSinVencimiento.get(i).setDisponible(false);
				return vacunasSinVencimiento.get(i);
			}
		}*/

		return null;

	}

	// *********************************************************************************************************************
	public boolean retirarVacuna(Vacuna vacuna) { // se modifico
		
		  for (int i = 0; i < vacunas.size(); i++) {
			  if(vacunas.get(i).equals(vacuna)) {
				vacunas.remove(i);
				return true;
			  }	
			} 
			/*
			for (int i = 0; i < vacunasSinVencimiento.size() + vacunasConVencimiento.size(); i++) {
				if (vacuna instanceof VacunaSinVencimiento) {
					vacunasSinVencimiento.remove(i);
					return true;
				} else if (vacuna instanceof VacunaConVencimiento)
					vacunasConVencimiento.remove(i);
				return true;
			}
			
			}*/
		  
		  return false;
		}
		
		




	public void eliminarVacunasVencidasOnoDisponibles(Fecha fecha) {
		verificarVacunasVencidas(fecha);
	}

	// nuevo
	public int getTotalVacunasVencidas() {
		return cantidadVacunasVencidas;
	}
	
	private void actualizarVacunaVencida(Vacuna vac) {
		cantidadVacunasVencidas++;
		Vacuna vacunaVencida = vac;
		// polimorfismo - En tiempo de ejecucion, llama a VacunaConVencimiento
		vacunaVencida.setVencida(true); 
		vacunaVencida.setDisponible(false);
		ArrayList<Vacuna> vacunas = vacunasVencidas.get(vac.getNombre());
		vacunas.add(vacunaVencida);
	    vacunasVencidas.put(vacunaVencida.getNombre(), vacunas);
	}
	
	// *********************************************************************************************************************
	private void verificarVacunasVencidas(Fecha fecha) { // se modifico
		//no actualiza!, aca elimina las vencidas o no disponibles
		//iteradores para poder eliminar
		Iterator<Vacuna> verificarVacunas = vacunas.iterator();
	
		while (verificarVacunas.hasNext()) {
			
			Vacuna vac = verificarVacunas.next();
			
			if(vac instanceof Pfizer) {
				int mesesAlmacenada = Math
						.abs(Fecha.diferenciaMes(vac.getFechaIngreso(), fecha));
				if (mesesAlmacenada == 1 && vac.getFechaIngreso().dia() != fecha.dia()) {
					// polimorfismo - En tiempo de ejecucion, llama a VacunaConVencimiento
					actualizarVacunaVencida(vac);
					verificarVacunas.remove();
			}else if (mesesAlmacenada > 1) {
					actualizarVacunaVencida(vac);
					verificarVacunas.remove();
				}
		     }
		
				if(vac instanceof Moderna) {
					int mesesAlmacenada = Math
							.abs(Fecha.diferenciaMes(vac.getFechaIngreso(), fecha));
					if (mesesAlmacenada == 1 && vac.getFechaIngreso().dia() != fecha.dia()) {
						actualizarVacunaVencida(vac);
						verificarVacunas.remove();
				}else if (mesesAlmacenada > 1) {
						actualizarVacunaVencida(vac);
						verificarVacunas.remove();
					}
		   	}
			
			/*
		for (int i = 0; i < vacunasConVencimiento.size(); i++) {
			if (vacunasConVencimiento.get(i) instanceof Pfizer) {
				int mesesAlmacenada = Math
						.abs(Fecha.diferenciaMes(vacunasConVencimiento.get(i).getFechaIngreso(), fecha));
				// comprobamos que no sea el ultimo dia del mes del vencimiento
				if (mesesAlmacenada == 1 && vacunasConVencimiento.get(i).getFechaIngreso().dia() != fecha.dia()) {
					vacunasConVencimiento.get(i).setVencida(true);// polimorfismo
					vacunasConVencimiento.get(i).setDisponible(false);
					vacunasVencidas.get("Pfizer").add(vacunasConVencimiento.get(i));
				} else if (mesesAlmacenada > 1) {
					vacunasConVencimiento.get(i).setVencida(true); // polimorfismo
					vacunasConVencimiento.get(i).setDisponible(false);
					vacunasVencidas.get("Pfizer").add(vacunasConVencimiento.get(i));

				}
			}
		}
	 
		for (int i = 0; i < vacunasConVencimiento.size(); i++) {
			if (vacunasConVencimiento.get(i) instanceof Moderna) {
				int mesesAlmacenada = Math
						.abs(Fecha.diferenciaMes(vacunasConVencimiento.get(i).getFechaIngreso(), fecha));
				// comprobamos que no sea el ultimo dia del mes del vencimiento
				if (mesesAlmacenada == 1 && vacunasConVencimiento.get(i).getFechaIngreso().dia() != fecha.dia()) { // polimofismo
					vacunasConVencimiento.get(i).setVencida(true);
					vacunasVencidas.get("Moderna").add(vacunasConVencimiento.get(i));
				} else if (mesesAlmacenada > 1) {
					vacunasConVencimiento.get(i).setVencida(true); // polimorfismo
					vacunasVencidas.get("Moderna").add(vacunasConVencimiento.get(i));

				}
			}
		}
		*/
	
	
		}
	}
	// ********************************************************************

	// *********************************************************************************************************************
	public int obtenerCantidadDevacunasPorNombre(String nombre) {// se modifico
		if (!compararN(nombre))
			throw new RuntimeException("No existe la vacuna: " + nombre);

		
		
		int vacunasDisponiblesPorNombre = 0;

		/*for (int i = 0; i < vacunasConVencimiento.size(); i++) {
			if (vacunasConVencimiento.get(i).getNombre().equals(nombre) // polimorfismo
					&& vacunasConVencimiento.get(i).isVencida() == false)
				vacunasDisponiblesPorNombre++;
		}

		for (int i = 0; i < vacunasSinVencimiento.size(); i++) {
			if (vacunasSinVencimiento.get(i).getNombre().equals(nombre)) // polimorfismo
				vacunasDisponiblesPorNombre++;
		}*/
		

		for (int i = 0; i < vacunas.size(); i++) {
			
			// polimorfismo - comprobará solo con las vacunas que corresponde
			// en caso de ser una vacuna normal, esta por defecto no estará vencida
			if (vacunas.get(i).getNombre().equals(nombre) && 
					vacunas.get(i).isVencida() == false && vacunas.get(i).isDisponible()) {
				vacunasDisponiblesPorNombre++;
			}
		}

		
		return vacunasDisponiblesPorNombre;

	}

	// *********************************************************************************************************************
	public int vacunasDisponibles() { // se modifico

		int vacunasDisponibles = 0;

		/*for (int i = 0; i < vacunasConVencimiento.size(); i++) { // polimorfismo
			if (vacunasConVencimiento.get(i).isVencida() == false && vacunasConVencimiento.get(i).isDisponible())
				vacunasDisponibles++;
		}
		for (int i = 0; i < vacunasSinVencimiento.size(); i++) { // polimorfismo
			if (vacunasSinVencimiento.get(i).isDisponible())
				vacunasDisponibles++;
		}*/
		
		for (int i = 0; i < vacunas.size(); i++) { // polimorfismo
			if (vacunas.get(i).isVencida() == false && vacunas.get(i).isDisponible())
				vacunasDisponibles++;
		}
		
		return vacunasDisponibles;
	}

	// *********************************************************************************************************************
	public int cantidadVacunasAplicablesAlPaciente(Paciente p) { // se modifico pero se dejo el arraylist
																	// con
		// nombres que no queria el profesor

		int cantidad = 0;

		/*for (int i = 0; i < vacunasSinVencimiento.size(); i++) {
			if (vacunasSinVencimiento.get(i).pacientePuedeVacunarse(p))
				cantidad++;
		}
		for (int i = 0; i < vacunasConVencimiento.size(); i++) {
			if (vacunasConVencimiento.get(i).pacientePuedeVacunarse(p))
				cantidad++;
		}*/
		
		for (int i = 0; i < vacunas.size(); i++) {
			if (vacunas.get(i).pacientePuedeVacunarse(p))
				cantidad++;
		}

		return cantidad;
	}

	// *********************************************************************************************************************
	public Map<String, Integer> getVacunasVencidas() { // no se modifico

		 Map<String, Integer> vencidas = new HashMap<String, Integer>();
		 vencidas.put("Moderna", vacunasVencidas.get("Moderna").size());
		 vencidas.put("Pfizer", vacunasVencidas.get("Pfizer").size());

		/*vencidas.put("Pfizer", vacunasVencidas.get("Pfizer").size());
		vencidas.put("Moderna", vacunasVencidas.get("Moderna").size());

		return vencidas;*/
		
		/*Iterator<Vacuna> verificarVacunas = vacunas.iterator();
		
		while (verificarVacunas.hasNext()) {
			
			Vacuna vac = verificarVacunas.next();
			
			if(vac instanceof Pfizer) {
				if(vac.isVencida()) {
					int cantVencidas = vencidas.get(vac.getNombre()) +1;
					vencidas.put(vac.getNombre(), cantVencidas);
				}
		    }
			else if(vac instanceof Moderna) {
				if(vac.isVencida()) {
					int cantVencidas = vencidas.get(vac.getNombre()) +1;
					vencidas.put(vac.getNombre(), cantVencidas);
				}
			}
		}*/
		
		return vencidas;
		
	}

	// *********************************************************************************************************************
	private boolean compararN(String nombre) { // no se modifico
		String[] nombres = { "Pfizer", "Moderna", "Sputnik", "Sinopharm", "Astrazeneca" };
		boolean algunNombre = false;
		for (int i = 0; i < nombres.length; i++) {
			algunNombre = algunNombre || nombre.equals(nombres[i]);
		}
		return algunNombre;
	}

	// *********************************************************************************************************************
	public static void main(String[] args) {

		CentroAlmacenamiento n = new CentroAlmacenamiento();
		Paciente p = new Paciente(1123564, new Fecha(10, 05, 1920), false, false); // mayor de 60
		Paciente p1 = new Paciente(1123564, new Fecha(10, 05, 1990), true, false); 
		n.agregarVacunas("Sputnik", 100, new Fecha(25, 6, 2021));
		n.agregarVacunas("Moderna", 10, new Fecha(25, 6, 2021));
		System.out.println(n.cantidadVacunasAplicablesAlPaciente(p1));

	}
}
