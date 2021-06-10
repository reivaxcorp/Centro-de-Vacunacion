
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CentroVacunacion {

	private String nombre;
	public int capacidadVacunacionDiaria;
	private int turnosAsignados;
	private int cantidadVacunados;
	private HashMap<Integer, Paciente> vacunasAplicadas;
 	private CentroAlmacenamiento centroAlmacenamiento;
	private Inscripcion inscripciones;
	
	private Fecha fecha;
	private Fecha diaVacunacionAnterior;
	private Fecha diaVacunacionActual;

	public CentroVacunacion(String nombreCentro, int capacidadVacunacionDiaria) {
		if (nombreCentro.equals("") || capacidadVacunacionDiaria <= 0)
			throw new RuntimeException("Por favor, ingrese los datos correctamente y la cantidad debe ser mayor a 0");
		else
			this.nombre = nombreCentro;
		this.capacidadVacunacionDiaria = capacidadVacunacionDiaria;
		this.turnosAsignados = 0;
		this.cantidadVacunados = 0;
		this.centroAlmacenamiento = new CentroAlmacenamiento();
		this.inscripciones = new Inscripcion();
		this.vacunasAplicadas = new HashMap<Integer, Paciente>();
		this.fecha = null;
		this.diaVacunacionAnterior = new Fecha(1, 1, 1900);
		this.diaVacunacionActual = Fecha.hoy();
	}

	public void ingresarVacunas(String nombreVacuna, int cantidad, Fecha fechaIngreso) { // listo
		centroAlmacenamiento.agregarVacunas(nombreVacuna, cantidad, fechaIngreso);
	}

	
	public int vacunasDisponibles() {
		
		return centroAlmacenamiento.vacunasDisponibles();
	}
	
	public int vacunasDisponibles(String nombreVacuna) {
		return centroAlmacenamiento.obtenerCantidadDevacunasPorNombre(nombreVacuna);
	}

	public void inscribirPersona(int dni, Fecha nacimiento, boolean tienePadecimientos, boolean esEmpleadoSalud) {
		inscripciones.inscribirCiudadano(dni, nacimiento, tienePadecimientos, esEmpleadoSalud);
	} 

	public ArrayList<Integer> listaDeEspera() { 
		return inscripciones.pacientesSinTurno();
	}

	
	private void retirarPacienteConTurnosVencidos() {
 
		Iterator<Integer> prioridad = inscripciones.obtenerListaEspera().keySet().iterator();
		
		while(prioridad.hasNext()) {
			
			int priori = prioridad.next();
			
			ArrayList<Paciente> pacientesPorPrioridad = inscripciones.obtenerListaEspera().get(priori);
			
			Iterator<Paciente> listPaciente = pacientesPorPrioridad.iterator();
			
			while(listPaciente.hasNext()) {
				
				Paciente pa = listPaciente.next();
				
				
				// Devolvemos vacunas al stock
				// elimamos del sistema al paciente con turno vencido
				
				 if(pa.getFechaTurno() != null) {
					 
					 if(Fecha.hoy().posterior(pa.getFechaTurno()) &&
							 pa.getVacunaAsignada() != null ) {
						 
						 centroAlmacenamiento.devolverVacunaAlStock(pa.getVacunaAsignada());
						 inscripciones.retirarPacienteConTurno(pa);
						 listPaciente.remove();
						 
					 } 
					 
				 }
				 
				
		     	}
			 
			
			 }
			
		}
			
		 
	
	private int asignarTurno(int prioridad, Fecha fechaInicial, int pacientesRestantes) {

		if(pacientesRestantes== 0)
			return 0;
		
	
		int turnosParaSiguienteFecha = 0;// guardamos los turnos que nos falta completar para el siguiente dia
		
		for(Paciente paciente: inscripciones.obtenerListaEspera().get(prioridad)) {
			
		
			if(turnosAsignados < capacidadVacunacionDiaria &&
					centroAlmacenamiento.cantidadVacunasAplicablesAlPaciente(paciente.getVacunasAplicables()) > 0) {
				if(paciente.getFechaTurno() == null) {
					
						Fecha fechaVacunacion = new Fecha(fechaInicial.dia(), fechaInicial.mes(), fechaInicial.anio());
						paciente.setFechaTurno(fechaVacunacion);
				
						Vacuna vacuna = centroAlmacenamiento.obtenerVacuna(paciente.getVacunasAplicables());
						paciente.setVacunaAsignada(vacuna);
						paciente.setFechaTurno(fechaVacunacion);
						
						
						inscripciones.setTurnosPorFecha(fechaVacunacion, paciente);
 						inscripciones.agregarPacienteConTurno(paciente);
						turnosAsignados++;
					
					
			 
 				} 
			
			}else if(turnosParaSiguienteFecha < capacidadVacunacionDiaria ) {
				if(paciente.getFechaTurno() == null) {
					turnosParaSiguienteFecha ++;
				}
			}
			 
		}
		

		if(turnosAsignados == capacidadVacunacionDiaria) {
			turnosAsignados = 0;
			fechaInicial.avanzarUnDia();
	
		}
			 
		
		//System.out.println(numeroPacientesRestantes);
		return asignarTurno(prioridad, fechaInicial, turnosParaSiguienteFecha);
	}
	

	
public void generarTurnos(Fecha fechaInicial) {
		
	
	
		if(Fecha.hoy().compareTo(fechaInicial) > 0) 
				throw  new RuntimeException();
		
		
		if(this.fecha == null)
			fecha  = new Fecha(fechaInicial.dia(), fechaInicial.mes(), fechaInicial.anio());
		 
			centroAlmacenamiento.verificarVacunasVencidas(fecha);
			retirarPacienteConTurnosVencidos();
			
			
			
			for(int prioridad: inscripciones.obtenerListaEspera().keySet()) {
	 
				switch(prioridad) {
				case 1: 
					 asignarTurno(prioridad, fecha, capacidadVacunacionDiaria);				 
					break;
				case 2: 
					 asignarTurno(prioridad, fecha, capacidadVacunacionDiaria);
		     		break; 
				case 3: 
					 asignarTurno(prioridad, fecha, capacidadVacunacionDiaria);			
					break; 
				case 4: 	
					 asignarTurno(prioridad, fecha, capacidadVacunacionDiaria);
				   break;
				default:
					break;
				}
				
			}
			
			 
	
	}





	public List<Integer> turnosConFecha(Fecha fecha) { 
		return inscripciones.dniDePacientesConTurno(fecha); 
	}

	public void vacunarInscripto(int dni, Fecha fechaVacunacion) {
		
	
	if(cantidadVacunados < capacidadVacunacionDiaria && 
			fechaVacunacion.posterior(diaVacunacionAnterior)) {
			
		Iterator iterator = inscripciones.getPacientesConTurno().keySet().iterator();
		boolean vacunado = false;
		Paciente pacienteVacunado = null;
		
		while(iterator.hasNext()) {
			int prioridad = (int) iterator.next();
			
			for(Paciente paciente : inscripciones.getPacientesConTurno().get(prioridad)) {
				if(paciente.getDni() == dni) {
					
					if(paciente.getFechaTurno().equals(fechaVacunacion)) {
						centroAlmacenamiento.retirarVacuna(paciente.getVacunaAsignada());
						vacunasAplicadas.put(paciente.getDni(), paciente);
						vacunado = true;
						pacienteVacunado = paciente;
					}else {
						throw new RuntimeException("La fecha de vacunacion no corresponde con la fecha de inscripcion");
					}
				}
			}
			
	
		}
		
			if(vacunado == false) {
					throw new RuntimeException("La persona a vacunar no esta inscripta"); 
			}else {
				inscripciones.retirarPacienteConTurno(pacienteVacunado);
				cantidadVacunados ++;
			}
		}else {
			diaVacunacionAnterior = new Fecha(fechaVacunacion.dia(), fechaVacunacion.mes(), fechaVacunacion.anio());
			cantidadVacunados = 0;
		}
	}

	@Override
	public String toString() {
		
		int turnosAsignados = 0;
		if(turnosAsignados < capacidadVacunacionDiaria) {
			
			
			Iterator<Fecha> fechas = inscripciones.getFechasTurnos().keySet().iterator();
			
			ArrayList<Fecha> fechasOrdenadas = new ArrayList<Fecha>();
			
			
			while(fechas.hasNext()) {
				
 				Fecha fecha = fechas.next();
		
 				for(Paciente paciente : inscripciones.getFechasTurnos().get(fecha)) {
					
 					
 					vacunarInscripto(paciente.getDni(), Fecha.hoy());
					
				}
				
				
			}
		
		}
		
		
		 StringBuilder datosInscriptos = new 
                 StringBuilder();
		
		 datosInscriptos.append("                                       "
					+ "----------------------------------").append("\n")
		 .append("                                       ")
		 .append("Centro: "+nombre).append("\n")
		 .append("                                       "
					+ "----------------------------------").append("\n")
		 
		 .append("                                       ")
		 .append("Capacidad de vacunacion diaria:").append(capacidadVacunacionDiaria).append("\n")
		 .append("Pacientes Inscriptos").append("\n");
		  
		for(int prioridad: inscripciones.verListaPorPrioridad().keySet()) {
			
			for(Paciente paciente: inscripciones.verListaPorPrioridad().get(prioridad)) {
				
				 datosInscriptos
				.append("****************************").append("\n")
				.append("Paciente -> DNI:")
				.append(paciente.getDni()).append("\n")
				.append("Prioridad -> ").append(paciente.getPrioridad()).append("\n")
				.append("Personal salud -> ").append(paciente.isPersonalSalud()).append("\n")
				.append("Padecimientos -> ").append(paciente.isEnfermedadPreexistente()).append("\n")
				.append("****************************").append("\n");
				 
			}
			
		}
		
		
	
		
		datosInscriptos.append(inscripciones.toString());
		datosInscriptos.append(centroAlmacenamiento.toString());

		return datosInscriptos.toString();
	}

	public HashMap<Integer, Paciente> reporteVacunacion() {
		
		return vacunasAplicadas; 
		
	}

	public Map<String, Integer> reporteVacunasVencidas() { 
		return centroAlmacenamiento.getVacunasVencidas();
	}
	
	
	

	public static void main(String[] args) {
		CentroVacunacion centro = new CentroVacunacion("UNGS", 5);
		centro.ingresarVacunas("Sputnik", 10, Fecha.hoy());
		centro.ingresarVacunas("AstraZeneca", 10,Fecha.hoy());
		centro.ingresarVacunas("Moderna", 10,Fecha.hoy());
		centro.ingresarVacunas("Sinopharm", 10,Fecha.hoy());
		centro.ingresarVacunas("Pfizer", 10,Fecha.hoy());

		
		centro.inscribirPersona(34701000, new Fecha(1, 5, 1989), false, false);   
		centro.inscribirPersona(29959000, new Fecha(20, 11, 1982), false, true);  
		centro.inscribirPersona(24780201, new Fecha(1, 6, 1972), true, false);    
		centro.inscribirPersona(29223000, new Fecha(2, 5, 1982), false, true);    
		centro.inscribirPersona(13000000, new Fecha(1, 5, 1958), true, false);   
		centro.inscribirPersona(13000050, new Fecha(20, 6, 1958), false, true);   
		centro.inscribirPersona(14000000, new Fecha(1, 1, 1961), false, false);   
		centro.inscribirPersona(14005000, new Fecha(20, 12, 1961), true, false); 
		
		
		centro.generarTurnos(Fecha.hoy());
		

		
		
		System.out.println(centro);
 	}
}
