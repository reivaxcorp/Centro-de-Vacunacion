
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class CentroAlmacenamiento {

	private HashMap<String, ArrayList<Vacuna>> vacunasVencidas; // se usa
	private ArrayList<VacunaConVencimiento> vacunasConVencimiento; // nuevo
	private ArrayList<VacunaSinVencimiento> vacunasSinVencimiento; // nuevo
	private int vacunasDisponibles;

	public CentroAlmacenamiento() {

		this.vacunasDisponibles = 0;

		vacunasVencidas = new HashMap<String, ArrayList<Vacuna>>();

		vacunasVencidas.put("Pfizer", new ArrayList<Vacuna>()); // se mantiene para obtener el O(1)
		vacunasVencidas.put("Moderna", new ArrayList<Vacuna>());
		vacunasVencidas.put("AstraZeneca", new ArrayList<Vacuna>());
		vacunasVencidas.put("Sputnik", new ArrayList<Vacuna>());
		vacunasVencidas.put("Sinopharm", new ArrayList<Vacuna>());

		vacunasConVencimiento = new ArrayList<VacunaConVencimiento>();// nuevo
		vacunasSinVencimiento = new ArrayList<VacunaSinVencimiento>();// nuevo
	}
	// ********************************************************************

	public void agregarVacunas(String nombre, int cant, Fecha ingreso) {// se modifico
		if (cant <= 0)
			throw new RuntimeException("La cantidad debe ser mayor a 0");
		else if (nombre.equals("Pfizer"))
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
			throw new RuntimeException("Ingrese nuevamente el nombre");
	}

	// ********************************************************************
	private void rellenarVacunasMenos18(String nombre, Fecha ing, int cant) { // se modifico

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
	}

	// ********************************************************************
	private void rellenarVacunas3(String nombre, int cant, Fecha ingreso) { // se modifico
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
	}

	// ********************************************************************
	// obtenes la vacuna que le corresponde al paciente en caso de que no esten
	// vencidas
	@SuppressWarnings("unlikely-arg-type")
	public Vacuna obtenerVacuna(Paciente p) { // no se modifico

		for (int i = 0; i < vacunasConVencimiento.size(); i++) {
			if (vacunasConVencimiento.get(i).pacientePuedeVacunarse(p))
				return vacunasConVencimiento.get(i);
		}

		for (int i = 0; i < vacunasSinVencimiento.size(); i++) {
			if (vacunasSinVencimiento.get(i).pacientePuedeVacunarse(p))
				return vacunasSinVencimiento.get(i);
		}

		return null;

	}

	// *********************************************************************************************************************
	public boolean retirarVacuna(Vacuna vacuna) { // se modifico

		for (int i = 0; i < vacunasSinVencimiento.size() + vacunasConVencimiento.size(); i++) {
			if (vacuna instanceof VacunaSinVencimiento) {
				vacunasSinVencimiento.remove(i);
				return true;
			} else if (vacuna instanceof VacunaConVencimiento)
				vacunasConVencimiento.remove(i);
			return true;
		}
		return false;

	}

	// *********************************************************************************************************************
	public void devolverVacunaAlStock(Paciente p) { // se modifico, como pruebo?
		for (int i = 0; i < vacunasSinVencimiento.size() + vacunasConVencimiento.size(); i++) {
			if(vacunasSinVencimiento.get(i).pacientePuedeVacunarse(p))
				vacunasSinVencimiento.add(vacunasSinVencimiento.get(i));
			else if(vacunasConVencimiento.get(i).pacientePuedeVacunarse(p))
				vacunasConVencimiento.add(vacunasConVencimiento.get(i));
		}

	}

	public void eliminarVacunasVencidasOnoDisponibles(Fecha fecha) {
		verificarVacunasVencidas(fecha);
	}

	// *********************************************************************************************************************
	private void verificarVacunasVencidas(Fecha fecha) { // se modifico
		//no actualiza!, aca elimina las vencidas o no disponibles
		//iteradores para poder eliminar
		Iterator<VacunaConVencimiento> vacunasConV = vacunasConVencimiento.iterator();
		Iterator<VacunaSinVencimiento> vacunasSinV = vacunasSinVencimiento.iterator();
		while (vacunasSinV.hasNext()) {
			VacunaSinVencimiento vac = vacunasSinV.next();
			if(vac.isDisponible() == false)
				vacunasSinV.remove();
		}
		while (vacunasConV.hasNext()) {
			VacunaConVencimiento vac = vacunasConV.next();
			if(vac instanceof Pfizer) {
				int mesesAlmacenada = Math
						.abs(Fecha.diferenciaMes(vac.getFechaIngreso(), fecha));
				if (mesesAlmacenada == 1 && vac.getFechaIngreso().dia() != fecha.dia()) {
					vac.setVencida(true);// polimorfismo
					vac.setDisponible(false);
					vacunasVencidas.get("Pfizer").add(vac);
					vacunasConV.remove();
			}
				else if (mesesAlmacenada > 1) {
					vac.setVencida(true); // polimorfismo
					vac.setDisponible(false);
					vacunasVencidas.get("Pfizer").add(vac);
					vacunasConV.remove();
				}
		}
			else if(vac instanceof Moderna) {
				if(vac instanceof Moderna) {
					int mesesAlmacenada = Math
							.abs(Fecha.diferenciaMes(vac.getFechaIngreso(), fecha));
					if (mesesAlmacenada == 1 && vac.getFechaIngreso().dia() != fecha.dia()) {
						vac.setVencida(true);// polimorfismo
						vac.setDisponible(false);
						vacunasVencidas.get("Moderna").add(vac);
						vacunasConV.remove();
				}
					else if (mesesAlmacenada > 1) {
						vac.setVencida(true); // polimorfismo
						vac.setDisponible(false);
						vacunasVencidas.get("Moderna").add(vac);
						vacunasConV.remove();
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
	}
	// ********************************************************************

	// *********************************************************************************************************************
	public int obtenerCantidadDevacunasPorNombre(String nombre) {// se modifico
		if (!compararN(nombre))
			throw new RuntimeException("No existe la vacuna: " + nombre);

		int vacunasDisponiblesPorNombre = 0;

		for (int i = 0; i < vacunasConVencimiento.size(); i++) {
			if (vacunasConVencimiento.get(i).getNombre().equals(nombre) // polimorfismo
					&& vacunasConVencimiento.get(i).isVencida() == false)
				vacunasDisponiblesPorNombre++;
		}

		for (int i = 0; i < vacunasSinVencimiento.size(); i++) {
			if (vacunasSinVencimiento.get(i).getNombre().equals(nombre)) // polimorfismo
				vacunasDisponiblesPorNombre++;
		}

		return vacunasDisponiblesPorNombre;
	}

	// *********************************************************************************************************************
	public int vacunasDisponibles() { // se modifico

		this.vacunasDisponibles = 0;

		for (int i = 0; i < vacunasConVencimiento.size(); i++) // polimorfismo
			if (vacunasConVencimiento.get(i).isVencida() == false && vacunasConVencimiento.get(i).isDisponible())
				vacunasDisponibles++;
		for (int i = 0; i < vacunasSinVencimiento.size(); i++) // polimorfismo
			if (vacunasSinVencimiento.get(i).isDisponible())
				vacunasDisponibles++;

		return vacunasDisponibles;
	}

	// *********************************************************************************************************************
	public int cantidadVacunasAplicablesAlPaciente(Paciente p) { // se modifico pero se dejo el arraylist
																	// con
		// nombres que no queria el profesor

		int cantidad = 0;

		for (int i = 0; i < vacunasSinVencimiento.size(); i++) {
			if (vacunasSinVencimiento.get(i).pacientePuedeVacunarse(p))
				cantidad++;
		}
		for (int i = 0; i < vacunasConVencimiento.size(); i++) {
			if (vacunasConVencimiento.get(i).pacientePuedeVacunarse(p))
				cantidad++;
		}

		return cantidad;
	}

	// *********************************************************************************************************************
	public Map<String, Integer> getVacunasVencidas() { // no se modifico

		Map<String, Integer> vencidas = new HashMap<String, Integer>();

		vencidas.put("Pfizer", vacunasVencidas.get("Pfizer").size());
		vencidas.put("Moderna", vacunasVencidas.get("Moderna").size());

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
