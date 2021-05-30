


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CentroVacunacion {

	private String nombre;
	public int capacidadVacunacionDiaria;
	private int turnosAsignados;
	private HashMap<Integer, Paciente> vacunasAplicadas;   // aun no se uso, es necesario los turnos
 	private CentroAlmacenamiento centroAlmacenamiento;
	private Inscripcion inscripciones;
	private Fecha turno;

	public CentroVacunacion(String nombreCentro, int capacidadVacunacionDiaria) {
		if (nombreCentro.equals("") || capacidadVacunacionDiaria <= 0)
			throw new RuntimeException("Por favor, ingrese los datos correctamente y la cantidad debe ser mayor a 0");
		else
			this.nombre = nombreCentro;
		this.capacidadVacunacionDiaria = capacidadVacunacionDiaria;
		this.turnosAsignados = 0;
		this.centroAlmacenamiento = new CentroAlmacenamiento();
		this.inscripciones = new Inscripcion();
		this.vacunasAplicadas = new HashMap<Integer, Paciente>();
 		this.turno = new Fecha();

	}

	public void ingresarVacunas(String nombreVacuna, int cantidad, Fecha fechaIngreso) { // listo
		centroAlmacenamiento.agregarVacunas(nombreVacuna, cantidad, fechaIngreso);
	}

	
	public int vacunasDisponibles() { // listo
		return centroAlmacenamiento.vacunasDisponibles();
	}
	
	public int vacunasDisponibles(String nombreVacuna) { // listo
		return centroAlmacenamiento.obtenerCantidadDevacunasPorNombre(nombreVacuna);
	}

	public void inscribirPersona(int dni, Fecha nacimiento, boolean tienePadecimientos, boolean esEmpleadoSalud) {
		inscripciones.inscribirCiudadano(dni, nacimiento, tienePadecimientos, esEmpleadoSalud);
	} // listo

	public ArrayList<Integer> listaDeEspera() { // falta quitarlos cuando se asigne turno
		return inscripciones.pacientesSinTurno();
	}

	
	private int asignarTurno(int prioridad, Fecha fechaInicial) {
		

		int numeroPacientesRestantes = 0;// guardamos los turnos que nos falta completar para el siguiente dia
		
		for(Paciente paciente: inscripciones.obtenerListaEspera().get(prioridad)) {
			
		
			if(turnosAsignados < capacidadVacunacionDiaria &&
					centroAlmacenamiento.cantidadVacunasDisponiblePorNombre(paciente.getVacunasAplicables()) > 0) {
				if(paciente.getFechaTurno() == null) {
					
						paciente.setFechaTurno(fechaInicial);
						Vacuna vacuna = centroAlmacenamiento.retirarVacuna(paciente.getVacunasAplicables());
						paciente.setVacuna(vacuna);
						inscripciones.agregarPacienteConTurno(paciente);
						//System.out.println("DNI: "+ paciente.getDni() + " FECHA: " + paciente.getFechaTurno() + " PRIORIDAD " + paciente.getPrioridad());
						turnosAsignados++;
					
					
 				}
			
			}else if(numeroPacientesRestantes < capacidadVacunacionDiaria ) {
				if(paciente.getFechaTurno() == null) {
					numeroPacientesRestantes ++;
				}
			}
			 // devolver stock de vacunas
			if(paciente.getFechaTurno() != null) {
				 
				
				//centroAlmacenamiento.reponerVacuna(paciente.getVacuna());
				
			}
		}
		//System.out.println(numeroPacientesRestantes);
		return numeroPacientesRestantes;
	}
	
	public void generarTurnos(Fecha fechaInicial) {
		  //centroAlmacenamiento.retirarVacuna(paciente.getVacunasAplicables());
	
			centroAlmacenamiento.verificarVacunasVencidas(fechaInicial);
	
			

			// 3	
			
			for(int prioridad: inscripciones.obtenerListaEspera().keySet()) {
	
				 int numeroPacientesRestantes = 0;
 
				switch(prioridad) {

				case 1: 

					
					
					 numeroPacientesRestantes = asignarTurno(prioridad, fechaInicial);
					 if(turnosAsignados == capacidadVacunacionDiaria) {
							turnosAsignados = 0;
							fechaInicial.avanzarUnDia();
						}
						 
					 while(numeroPacientesRestantes != 0) {
						 numeroPacientesRestantes = asignarTurno(prioridad, fechaInicial);
					 }
					
				
					 
					break;
				case 2: 
					

					
					 numeroPacientesRestantes = asignarTurno(prioridad, fechaInicial);
					 if(turnosAsignados == capacidadVacunacionDiaria) {
							turnosAsignados = 0;
							fechaInicial.avanzarUnDia();
						}
					 while(numeroPacientesRestantes != 0) {
						 numeroPacientesRestantes = asignarTurno(prioridad, fechaInicial);
					 }
				

					break; 
				case 3: 

					 numeroPacientesRestantes = asignarTurno(prioridad, fechaInicial);
					 if(turnosAsignados == capacidadVacunacionDiaria) {
						turnosAsignados = 0;
					 	fechaInicial.avanzarUnDia();
					 }
					 while(numeroPacientesRestantes != 0) {
						 numeroPacientesRestantes = asignarTurno(prioridad, fechaInicial);
					 }
					
					break; 
				case 4: 
					

				
					 numeroPacientesRestantes = asignarTurno(prioridad, fechaInicial);
					 if(turnosAsignados == capacidadVacunacionDiaria) {
							turnosAsignados = 0;
							fechaInicial.avanzarUnDia();
						}
					 while(numeroPacientesRestantes != 0) {
						 numeroPacientesRestantes = asignarTurno(prioridad, fechaInicial);
					 }
					 
					 
					break;
				default:
					break;
				}
				
			}
	
		

			// removemos el paciente de la lista en espera
			Iterator<Integer> it = inscripciones.getPacientesConTurno().keySet().iterator();

			ArrayList<Paciente> pacientesConTurno = new ArrayList<Paciente>();
			
			while(it.hasNext()) {
				
				int keyPrioridad = it.next();
				
				for(Paciente pacienteConTurno : inscripciones.getPacientesConTurno().get(keyPrioridad)) {
					
					for(Paciente pacienteEnEspera: inscripciones.obtenerListaEspera().get(keyPrioridad)) {
						
						if(pacienteConTurno.getDni() == pacienteEnEspera.getDni()) {
							
							if(existePaciente(pacientesConTurno, pacienteEnEspera) == false) {
								pacientesConTurno.add(pacienteEnEspera);

							}
							

						}
						
					}
					
				}
		 
				
			}
			

			
			
			inscripciones.quitarPacienteListaEspera(pacientesConTurno);
		
	}

	private boolean existePaciente(ArrayList<Paciente> pacientesConTurno, Paciente paciente) {
		for(Paciente pa : pacientesConTurno) {
			if(pa.getDni() == paciente.getDni()) {
				return true;
			}
		}
		return false;
	}
	
	
	public List<Integer> turnosConFecha(Fecha fecha) { // falta
		return inscripciones.dniDePacientesConTurno(fecha); // modificar por dentro
	}

	public void vacunarInscripto(int dni, Fecha fechaVacunacion) {
		
		
		Iterator iterator = inscripciones.getPacientesConTurno().keySet().iterator();
		
		while(iterator.hasNext()) {
			int prioridad = (int) iterator.next();
			for(Paciente paciente : inscripciones.getPacientesConTurno().get(prioridad)) {
				if(paciente.getDni() == dni) {
					Vacuna vacuna = centroAlmacenamiento.retirarVacuna(paciente.getVacunasAplicables());
					paciente.setVacuna(vacuna);
					vacunasAplicadas.put(paciente.getDni(), paciente);
				}
			}
			
		}
		
	}

	public HashMap<Integer, Paciente> reporteVacunacion() {
		
	
		
		return vacunasAplicadas; // luego de asignar turnos ya que ahi le indico la vacuna y se la seteo
		
		
		
	}

	public Map<String, Integer> reporteVacunasVencidas() { // listo
		return centroAlmacenamiento.getVacunasVencidas();
	}

	public static void main(String[] args) {
		CentroVacunacion centro = new CentroVacunacion("Hospital San Miguel", 2);
		centro.ingresarVacunas("Pfizer", 3, new Fecha(27, 5, 2021));
		centro.ingresarVacunas("Pfizer", 3, new Fecha(27, 9, 2021)); //vencidas, asi que tengo 3 
		centro.ingresarVacunas("Sputnik", 3, new Fecha(28, 6, 2021));
//		centro.ingresarVacunas("Moderna", 130, new Fecha(21,05,2021));
//		System.out.println(centro.vacunasDisponibles("Pfizer"));
//		System.out.println(centro.vacunasDisponibles("Moderna"));
//		centro.ingresarVacunas("Pfizer", 400, new Fecha(24,05,2021));
//		System.out.println(centro.vacunasDisponibles("Pfizer"));
//		System.out.println("-----");
//		centro.ingresarVacunas("Sputnik", 3, new Fecha(21,05,2021));
//		System.out.println(centro.vacunasDisponibles("Sputnik"));
//		centro.inscribirPersona(13768612, new Fecha(13,06,1960), false, false); //60 -> prior: 2
//		centro.inscribirPersona(45307940, new Fecha(9,05,2001), false, false);  //20 -> prior: 4
//		centro.inscribirPersona(22124064, new Fecha(31,01,1970), true, false);  //54 -> prior: 3  
		centro.inscribirPersona(10124064, new Fecha(15, 01, 1950), false, true); // 54 -> prior: 1
		centro.inscribirPersona(26124064, new Fecha(15, 01, 1970), false, true); // 54 -> prior: 1
		centro.inscribirPersona(37124064, new Fecha(15, 01, 1970), false, true); // 54 -> prior: 1
		centro.inscribirPersona(421124064, new Fecha(15, 01, 1970), false, true);
		centro.inscribirPersona(511124064, new Fecha(15, 01, 1970), false, true);
//		centro.inscribirPersona(00104064, new Fecha(25,01,1970), false, true);  //54 -> prior: 1
//		System.out.println(centro.listaDeEspera());
//		centro.iniciarVacunacion(new Fecha(13,06,1960), 10);
//		System.out.println(centro.listaDeEspera());

		centro.generarTurnos(new Fecha(27, 05, 2021));
//
//		System.out.println(centro.turnosConFecha(new Fecha(27, 05, 2021)));
//		System.out.println(centro.turnosConFecha(new Fecha(28, 05, 2021)));
		// prueba para vacunas vencidas :

//		centro.ingresarVacunas("Pfizer", 10, new Fecha(27, 06, 2021)); // Estan vencidas, la fecha actual es 26/05/2021
//		centro.ingresarVacunas("Pfizer", 3, new Fecha(27, 01, 2021)); // NO Estan vencidas, la fecha actual es
//																		// 26/05/2021
//		System.out.println("Pfizer: " + centro.vacunasDisponibles("Pfizer")); // -> solo quedan 3 Sputnik
//		centro.ingresarVacunas("Moderna", 10, new Fecha(27, 07, 2021)); // Estan vencidas, la fecha actual es 26/05/2021
//		centro.ingresarVacunas("Moderna", 3, new Fecha(27, 01, 2021)); // NO Estan vencidas, la fecha actual es
//																		// 26/05/2021
//		System.out.println("Moderna: " + centro.vacunasDisponibles("Moderna")); // -> solo quedan 3 Moderna
//		System.out.println("Vencidas: ");
//		System.out.println(centro.reporteVacunasVencidas()); // devuelve el hash pedido en el enunciado

//		HashMap<Integer,LinkedList<String>> x = new HashMap<Integer,LinkedList<String>>();
//		x.put(1, new LinkedList<String>());
//		x.get(1).add("1 : Hola");
//		x.get(1).add("2 : Hola2");
//		x.get(1).add("3 : Hola3");
//		Iterator xx = x.get(1).iterator();
//		
//		while(xx.hasNext()) {
//			String n = (String)xx.next();
//			System.out.println(n.toString());
//			xx.remove();
//			
//		}
//		System.out.println();

//		System.out.println(x.get(1).size());
	}
}
