


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
	private int vacunasDisponibles = 0;
	
	protected HashMap<String, LinkedList<Vacuna>> vacunas;
	protected HashMap<String, Integer> vacunasVencidas;

	public CentroAlmacenamiento() {
		vacunas = new HashMap<String, LinkedList<Vacuna>>();
		vacunas.put("Pfizer", new LinkedList<Vacuna>());
		vacunas.put("Moderna", new LinkedList<Vacuna>());
		vacunas.put("AstraZeneca", new LinkedList<Vacuna>());
		vacunas.put("Sputnik", new LinkedList<Vacuna>());
		vacunas.put("Sinopharm", new LinkedList<Vacuna>());
 
	

	}

	
	// retiramos la vacuna que le corresponde al paciente en caso de que esten disponibles o no esten vencidas
	public Vacuna retirarVacuna(ArrayList<String> nombres) {
		

		Vacuna vacuna = null;

		
		for(String nombre: nombres) {
	
			for(int i = 0; i < vacunas.get(nombre).size(); i++) {
				
				if(vacunas.get(nombre).get(i).getDisponible() == true) {
					vacunas.get(nombre).get(i).setDisponible(false);
					vacuna =  vacunas.get(nombre).get(i);
					return vacuna;
				}
	
			}
	
		}
	
		throw new RuntimeException("Ya no quedaron vacunas: " + nombres);
	}
	
	
	public void reponerVacuna(Vacuna vacuna) {
		vacuna.setDisponible(true);
		vacunas.get(vacuna.getNombre()).add(vacuna);
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

	private void actualizarEstadoVacunas(Fecha fecha, String nombre) { // sobreCarga
		if (nombre.equals("Pfizer")) {
			Iterator vacunasPfizerList = vacunas.get("Pfizer").iterator();
			while (vacunasPfizerList.hasNext()) {
				Pfizer pfizer = (Pfizer) vacunasPfizerList.next();
				int mesesAlamacenada = Fecha.diferenciaMes(pfizer.getFechaIngreso(), fecha) ;
				pfizer.setMesAlmacenada(mesesAlamacenada);
			
		}
		if (nombre.equals("Moderna")) {
			Iterator vacunaModernaList = vacunas.get("Moderna").iterator();
			while (vacunaModernaList.hasNext()) {
				Moderna moderna = (Moderna) vacunaModernaList.next();
				int mesesAlamacenada = Fecha.diferenciaMes(moderna.getFechaIngreso(), fecha) ;
			    moderna.setMesAlmacenada(mesesAlamacenada);
			
			}
		}

	 }
	}

	public int obtenerCantidadDevacunasPorNombre(String nombre) {
		if (!compararN(nombre))
			throw new RuntimeException("No existe la vacuna: " + nombre);
		else
			verificarVacunasVencidas(new Fecha()); // fecha actual
		return vacunas.get(nombre).size();
	}

	public int vacunasDisponibles() {
		this.vacunasDisponibles = 0;

		vacunas.forEach((nombreVacuna, listVacuna)-> {

			for(Vacuna vacuna: listVacuna) {
				if(vacuna instanceof Moderna) {
					
					if(((Moderna) vacuna).isVencida() == false && ((Moderna) vacuna).getDisponible() == true)
						this.vacunasDisponibles++;
				}else if( vacuna instanceof Pfizer) {
					
					if(((Pfizer) vacuna).isVencida() == false && ((Pfizer) vacuna).getDisponible() == true) {
						this.vacunasDisponibles ++;
					}
				}else {
					if(vacuna.getDisponible() == true)
						this.vacunasDisponibles ++;
					
				}
			}
 			
		
		});
		//System.out.println(vacunasDisponibles);
		return vacunasDisponibles;
	}
	
	
	public int cantidadVacunasDisponiblePorNombre(ArrayList<String> nombres) {
		
		for(int i = 0; i < nombres.size(); i++) {
		}
		
		int cantidad = 0;
		for(int i = 0; i < nombres.size(); i++) {
			
			for(Vacuna vacuna: vacunas.get(nombres.get(i))) {

				if(vacuna.getDisponible())
					cantidad++;
		
			}
		}

		return cantidad;
	}

	public Map<String, Integer> getVacunasVencidas() {

		
		vacunas.forEach((nombreVacuna, listVacuna)-> {

			int countModerna = 0;
			int countPfizer = 0;
			
			for(Vacuna vacuna: listVacuna) {
				if(vacuna instanceof Moderna) {
					
					if(((Moderna) vacuna).isVencida() == true)
						countModerna++;
						this.vacunasVencidas.put("Moderna", countModerna);
				}else if( vacuna instanceof Pfizer) {
					
					if(((Pfizer) vacuna).isVencida() == true) {
						countPfizer++;
						this.vacunasVencidas.put("Pfizer", countPfizer);
					}
				}
			}
 			
		
		});
		
		
		return vacunasVencidas;
	}

	
	
	private void rellenarVacunasMenos18(String nombre, Fecha ing, int temp, int diasAlmacenada, int diasCaducidad,
			boolean isVencida, int cant) {
		
		if(nombre.equals("Moderna")) {
			for (int i = 0; i < cant; i++) {
				vacunas.get(nombre).add(new Moderna(nombre, ing, temp, 0, isVencida));
			}
		}else if (nombre.equals("Pfizer")) {
			for (int i = 0; i < cant; i++) {
				vacunas.get(nombre).add(new Pfizer(nombre, ing, temp, 0, isVencida));
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
 
	private boolean compararN(ArrayList<String> nombres) {
		String[] tiposVacuna = { "Pfizer", "Moderna", "Sputnik", "Sinopharm", "Astranezca" };
		boolean algunNombre = false;
		for (int i = 0; i < nombres.size(); i++) {
			for (int z = 0; z < tiposVacuna.length; z++) {
				algunNombre = algunNombre || nombres.get(i).equals(tiposVacuna[z]);
			}
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
