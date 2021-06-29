
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CentroAlmacenamiento {

	
	private ArrayList<Vacuna> vacunas; // nuevo
	private Map<String, ArrayList<Vacuna>> vacunasVencidas;
	private int cantidadVacunasVencidas;

	public CentroAlmacenamiento() {
		
		vacunas = new ArrayList<Vacuna>();// nuevo		
		this.vacunasVencidas = new HashMap<String, ArrayList<Vacuna>>();
		this.vacunasVencidas.put("Pfizer", new ArrayList<Vacuna>());
		this.vacunasVencidas.put("Moderna", new ArrayList<Vacuna>());
		this.cantidadVacunasVencidas = 0;
	}
	// ********************************************************************

	public void agregarVacunas(String nombre, int cant, Fecha ingreso) {// se modifico
		

		if (cant <= 0)
			throw new RuntimeException("La cantidad debe ser mayor a 0"); // nuevo
		
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
	// obtenes la vacuna que le corresponde al paciente en caso de que no esten
	// vencidas
	public Vacuna obtenerVacuna(Paciente p) { 

		if(p == null) return null;
		
		for (int i = 0; i < vacunas.size(); i++) {
			
			if (vacunas.get(i).pacientePuedeVacunarse(p) && 
					vacunas.get(i).isDisponible()) {
				vacunas.get(i).setDisponible(false);	
				return vacunas.get(i);
			}
			
		}
		
		return null;
	}

	// *********************************************************************************************************************
	public boolean retirarVacuna(Vacuna vacuna) { 
		 
		if(vacuna == null) return false;
		
		  for (int i = 0; i < vacunas.size(); i++) {
			  if(vacunas.get(i).equals(vacuna)) {
				vacunas.remove(i);
				return true;
			  }	
			} 
			
		  return false;
		}
	
	public void eliminarVacunasVencidasOnoDisponibles(Fecha fecha) {
		verificarVacunasVencidas(fecha);
	}

 	public int getTotalVacunasVencidas() {
		return cantidadVacunasVencidas;
	}
	
	private void separarVacunaVencida(Vacuna vac) {
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
	private void verificarVacunasVencidas(Fecha fecha) { 
		//iteradores para poder eliminar
		Iterator<Vacuna> verificarVacunas = vacunas.iterator();
	
		while (verificarVacunas.hasNext()) {
			
			Vacuna vac = verificarVacunas.next();
			
			if(vac instanceof Pfizer) {
				int mesesAlmacenada = Math
						.abs(Fecha.diferenciaMes(vac.getFechaIngreso(), fecha));
				if (mesesAlmacenada == 1 && vac.getFechaIngreso().dia() != fecha.dia()) {
					// polimorfismo - En tiempo de ejecucion, llama a VacunaConVencimiento
					separarVacunaVencida(vac);
					verificarVacunas.remove();
			}else if (mesesAlmacenada > 1) {
					separarVacunaVencida(vac);
					verificarVacunas.remove();
				}
		     }
		
				if(vac instanceof Moderna) {
					int mesesAlmacenada = Math
							.abs(Fecha.diferenciaMes(vac.getFechaIngreso(), fecha));
					if (mesesAlmacenada == 1 && vac.getFechaIngreso().dia() != fecha.dia()) {
						separarVacunaVencida(vac);
						verificarVacunas.remove();
				}else if (mesesAlmacenada > 1) {
						separarVacunaVencida(vac);
						verificarVacunas.remove();
					}
		   	}
		}
	}
	// ********************************************************************

	// *********************************************************************************************************************
	public int obtenerCantidadDevacunasPorNombre(String nombre) {
		if (!compararN(nombre))
			throw new RuntimeException("No existe la vacuna: " + nombre);

			
		int vacunasDisponiblesPorNombre = 0;

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
	public int vacunasDisponibles() {  

		int vacunasDisponibles = 0;

		for (int i = 0; i < vacunas.size(); i++) { // polimorfismo
			if (vacunas.get(i).isVencida() == false && vacunas.get(i).isDisponible())
				vacunasDisponibles++;
		}
		
		return vacunasDisponibles;
	}

	// *********************************************************************************************************************
	public int disponibilidadVacunasAplicablesAlPaciente(Paciente p) {
	
		if(p == null) return -1;
		
		int cantidad = 0;
		
		for (int i = 0; i < vacunas.size(); i++) {
			if (vacunas.get(i).pacientePuedeVacunarse(p) && vacunas.get(i).isDisponible())
				cantidad++;
		}
		return cantidad;
	}

	// *********************************************************************************************************************
	public Map<String, Integer> getVacunasVencidas() { 

		 Map<String, Integer> vencidas = new HashMap<String, Integer>();
		 vencidas.put("Moderna", vacunasVencidas.get("Moderna").size());
		 vencidas.put("Pfizer", vacunasVencidas.get("Pfizer").size());

		return vencidas;
		
	}

	// *********************************************************************************************************************
	public static boolean compararN(String nombre) { 
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
		Paciente p = new Paciente(23883383, new Fecha(10, 05, 1920), false, false); // mayor de 60
		Paciente p1 = new Paciente(23638838, new Fecha(10, 05, 1990), true, false); 
		n.agregarVacunas("Sputnik", 100, new Fecha(25, 6, 2021));
		n.agregarVacunas("Moderna", 10, new Fecha(25, 6, 2021));
		System.out.println(n);

	}
	
	@Override
	public String toString() {
		
		StringBuilder datos = new StringBuilder();
		datos.append("Vacunas ingresadas").append("\n");
		for(Vacuna va: vacunas) {
			datos.append(va).append("\n");
		}
		datos.append("Vacunas vencidas\n").append("cantidad:").append(getTotalVacunasVencidas());
		
		return datos.toString();
	}
	
}
