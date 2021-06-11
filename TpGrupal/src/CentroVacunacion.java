
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CentroVacunacion {

	private String nombre;
	public int capacidadVacunacionDiaria;
	private int turnosAsignados;
	private int cantidadVacunadosPorFecha;
	private HashMap<Integer, Paciente> vacunasAplicadas;
 	private CentroAlmacenamiento centroAlmacenamiento;
	private Inscripcion inscripciones;
	
	private Fecha fecha;
	private Fecha diaVacunacionAnterior;

	public CentroVacunacion(String nombreCentro, int capacidadVacunacionDiaria) {
		if (nombreCentro.equals("") || capacidadVacunacionDiaria <= 0)
			throw new RuntimeException("Por favor, ingrese los datos correctamente y la cantidad debe ser mayor a 0");
		else
			this.nombre = nombreCentro;
		this.capacidadVacunacionDiaria = capacidadVacunacionDiaria;
		this.turnosAsignados = 0;
		this.cantidadVacunadosPorFecha = 0;
		this.centroAlmacenamiento = new CentroAlmacenamiento();
		this.inscripciones = new Inscripcion();
		this.vacunasAplicadas = new HashMap<Integer, Paciente>();
		this.fecha = null;
		this.diaVacunacionAnterior = new Fecha(1, 1, 1900);
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
			
		 
	
	private int asignarTurno(int prioridad, Fecha fechaInicial, int cantPacienteParaLaFecha) {

		if(cantPacienteParaLaFecha== 0)
			return 0;
		
	
		int cantPacientesParaLaSiguienteFecha = 0;// guardamos los turnos que nos falta completar para el siguiente dia
		int cantPacientesSinVacunas = 0;
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
			
			}else if (centroAlmacenamiento.cantidadVacunasAplicablesAlPaciente(paciente.getVacunasAplicables()) == 0) {
				cantPacientesSinVacunas ++;				
			}else if(cantPacientesParaLaSiguienteFecha < capacidadVacunacionDiaria ) {
				if(paciente.getFechaTurno() == null) {
					cantPacientesParaLaSiguienteFecha ++;
				}
			}
			 
		}
		
		// llegamos a la capacidad diaria, avanzamos al siguiente dia
		if(turnosAsignados == capacidadVacunacionDiaria) {
			turnosAsignados = 0;
			fechaInicial.avanzarUnDia();
	
		}
			 
		if(cantPacientesSinVacunas == 0) {
			return asignarTurno(prioridad, fechaInicial, cantPacientesParaLaSiguienteFecha); // hay vacunas, asi que seguimos asignando turnos
		}else {
			return asignarTurno(prioridad, fechaInicial, 0); // no hay mas vacunas por eso no generamos mas tunos
		}
	
	}
	

	
public void generarTurnos(Fecha fechaInicial) {
		
	
	
		if(Fecha.hoy().compareTo(fechaInicial) > 0) 
				throw  new RuntimeException();
		
		
		if(this.fecha == null)
			fecha  = new Fecha(fechaInicial.dia(), fechaInicial.mes(), fechaInicial.anio());
		 
		    retirarPacienteConTurnosVencidos();
			centroAlmacenamiento.verificarVacunasVencidas(fecha);
			
			
			
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

	public Paciente vacunarInscripto(int dni, Fecha fechaVacunacion) {
		
		Paciente pacienteVacunado = null;

	if(cantidadVacunadosPorFecha < capacidadVacunacionDiaria && 
			fechaVacunacion.posterior(diaVacunacionAnterior)) {
			
		Iterator iterator = inscripciones.getPacientesConTurno().keySet().iterator();
		boolean vacunado = false;
		
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
				cantidadVacunadosPorFecha ++;
			}
		}else {
			diaVacunacionAnterior = new Fecha(fechaVacunacion.dia(), fechaVacunacion.mes(), fechaVacunacion.anio());
			cantidadVacunadosPorFecha = 0;
		}
	
		return pacienteVacunado;
	}

	@Override
	public String toString() {
		
		 StringBuilder generarDatos = new 
                StringBuilder();
		 
	
		

		
		 generarDatos.append("                                       "
					+ "----------------------------------").append("\n")
		 .append("                                       ")
		 .append("Centro: "+nombre).append("\n")
		 .append("                                       "
					+ "----------------------------------").append("\n")
		 
		 .append("                                       ")
		 .append("Capacidad de vacunacion diaria:").append(capacidadVacunacionDiaria).append("\n");
		 
		 
		 generarDatos.append("                                       ").append("\n");
		 generarDatos.append("                                       ").append("\n");
		 generarDatos.append("                                       "
					+ "----------------------------------").append("\n")
		 .append("                                       ")
		 .append("Vacunas ingresadas").append("\n")
		 .append("                                       "
					+ "----------------------------------").append("\n")
		 
		 .append("                                       ").append("\n");
		 
		 
		 
		 Iterator<String> tipoVacunas = centroAlmacenamiento.getVacunas().keySet().iterator();
			
			while(tipoVacunas.hasNext()) {
					
					String tipoVacuna = tipoVacunas.next();
						
						Iterator<Vacuna> listaVacunas = centroAlmacenamiento.getVacunas().get(tipoVacuna).iterator();
						
						while(listaVacunas.hasNext()) {
						
							Vacuna vacuna = listaVacunas.next();
							  generarDatos
							 .append("Nombre: ").append(vacuna.getNombre()).append("\n")
							 .append("disponible: ").append((vacuna.isDisponible())? "Si": "No").append("\n")
							 .append("Temp: ").append(vacuna.getTemp()).append("\n")
							 .append("Fecha Ingreso: ").append(vacuna.getFechaIngreso()).append("\n");
						
						}

						
				
					
			}
		 
		 
			generarDatos.append("                                       "
					+ "----------------------------------").append("\n")
		 
		    .append("                                       ").append("\n");
		 
		 
		 
		 
			generarDatos.append("Pacientes Inscriptos").append("\n");
		 
		 
		 
		for(int prioridad: inscripciones.verListaPorPrioridad().keySet()) {
			
			for(Paciente paciente: inscripciones.verListaPorPrioridad().get(prioridad)) {
				
				generarDatos
				.append("****************************").append("\n")
				.append("DNI:")
				.append(paciente.getDni()).append("\n")
				.append("Prioridad: ").append(paciente.getPrioridad()).append("\n")
				.append("Personal salud: ").append((paciente.isPersonalSalud() == true)? "Si": "No").append("\n")
				.append("Padecimientos: ").append((paciente.isEnfermedadPreexistente() == true)? "Si": "No").append("\n")
				.append("****************************").append("\n");
				 
			}
			
		}
		
	
		
		 generarDatos.append("                                       ").append("\n");
		 generarDatos.append("                                       ").append("\n");
		 generarDatos.append("                                       "
					+ "----------------------------------").append("\n")
		 .append("                                       ")
		 .append("Generando turnos.....").append("\n")
		 .append("                                       "
					+ "----------------------------------").append("\n")
		 
		 .append("                                       ").append("\n");
		
		 
		 
		 
		 
		 
		 /******************************************************/
		 generarTurnos(Fecha.hoy());
		 /******************************************************/
		
		 
		 
		 generarDatos.append("                                       ").append("\n");
		 generarDatos.append("                                       ").append("\n");
		
		
		generarDatos.append(inscripciones.toString());
		
		

		
		
		 
		Iterator<Fecha> fechas = inscripciones.getFechasTurnos().keySet().iterator();
		
		int cantVacunadosEnTotal = 0;
		
		while(fechas.hasNext()) {
			
				Fecha fecha = fechas.next();
				

				generarDatos.append("                                       "
							+ "-------------------------------------------------------").append("\n")
				 .append("                                       ")
				 .append("Iniciando vacunación fecha: ").append(fecha).append("\n")
				 .append("                                       "
							+ "-------------------------------------------------------").append("\n");
				
	
				for(Paciente paciente : inscripciones.getFechasTurnos().get(fecha)) {

					generarDatos.append("                                       ");

					Paciente vacunado = vacunarInscripto(paciente.getDni(), fecha);
					generarDatos.append((vacunado!=null)? 
							"Paciente (vacunado) Dni: "+ vacunado.getDni()+
							" Vacuna: "+ vacunado.getVacunaAsignada().getNombre()+
							" Prioridad: "+ vacunado.getPrioridad(): "").append("\n");
					
					if(vacunado!=null) {
						cantVacunadosEnTotal++;
					}
					
				}
				
				
				cantidadVacunadosPorFecha = 0; // para llevar la contabilidad de los vacunados por dia no supere a la maxima vacunacion diaria
				diaVacunacionAnterior = fecha; // la siguiente fecha debe ser superior a la anterior
			
		}
		generarDatos.append("                                       "
					+ "_______________________________________________").append("\n");
		generarDatos.append("                                       Total: ").append(cantVacunadosEnTotal).append("\n");	 
		 
		 
		 
		generarDatos.append("                                       ").append("\n");
		generarDatos.append("                                       "
		  		+ "----------------------------------").append("\n")
		 .append("                                       ")
		 .append("Lista espera espera (sin turnos)").append("\n")
		 .append("                                       "
					+ "----------------------------------").append("\n");
		 
	 
	     int sinTurnos = 0;
	     
		 for(int pacienteSinTurno: inscripciones.pacientesSinTurno()) {
			 
			 generarDatos.append("                                       ");
			 generarDatos.append("Paciente Dni: ").append(pacienteSinTurno).append("\n");
							
			sinTurnos++;		 
		 }
		 generarDatos.append("                                       "
					+ "_______________________________________________").append("\n");
		 generarDatos.append("                                       Total: ").append(sinTurnos).append("\n");	 
		 generarDatos.append("                                       ").append("\n");	 	
	
		
		 
		 
		 
		 generarDatos.append(centroAlmacenamiento.toString());

		return generarDatos.toString();
	}

	public HashMap<Integer, Paciente> reporteVacunacion() {
		
		return vacunasAplicadas; 
		
	}

	public Map<String, Integer> reporteVacunasVencidas() { 
		return centroAlmacenamiento.getVacunasVencidas();
	}
	
	
	

	public static void main(String[] args) {
		
		
		CentroVacunacion centro = new CentroVacunacion("UNGS", 5);
		centro.ingresarVacunas("Sputnik", 2, Fecha.hoy());
		centro.ingresarVacunas("AstraZeneca", 1,Fecha.hoy());
		centro.ingresarVacunas("Moderna", 3,Fecha.hoy());
		centro.ingresarVacunas("Sinopharm", 4,Fecha.hoy());
		centro.ingresarVacunas("Pfizer", 2,Fecha.hoy());

		
		
		
		
		centro.inscribirPersona(34701000, new Fecha(1, 5, 1989), false, false);   
		centro.inscribirPersona(29959000, new Fecha(20, 11, 1982), false, true);  
		centro.inscribirPersona(24780201, new Fecha(1, 6, 1972), true, false);    
		centro.inscribirPersona(29223000, new Fecha(2, 5, 1982), false, true);    
		centro.inscribirPersona(13000000, new Fecha(1, 5, 1958), true, false);   
		centro.inscribirPersona(13000050, new Fecha(20, 6, 1958), false, true);   
		centro.inscribirPersona(14000000, new Fecha(1, 1, 1961), false, false);   
		centro.inscribirPersona(14005000, new Fecha(20, 12, 1961), true, false); 
		
		centro.inscribirPersona(34015000, new Fecha(20, 12, 1982), true, false); 
		centro.inscribirPersona(14015123, new Fecha(20, 12, 1976), true, false); 
		centro.inscribirPersona(24113000, new Fecha(20, 12, 1990), true, false); 
		centro.inscribirPersona(3205000, new Fecha(20, 12, 2001), true, false); 
		centro.inscribirPersona(24205600, new Fecha(20, 12, 1998), true, false); 
		centro.inscribirPersona(12115010, new Fecha(20, 12, 1977), true, false); 
		centro.inscribirPersona(13005460, new Fecha(20, 12, 2000), true, false); 
		centro.inscribirPersona(11105430, new Fecha(20, 12, 1988), true, false); 

		
		System.out.println(centro);
 	}
}
