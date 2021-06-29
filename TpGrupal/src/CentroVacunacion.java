
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CentroVacunacion {

	private String nombre;
	public int capacidadVacunacionDiaria;
	private int cantidadVacunados;
	private HashMap<Integer, Paciente> vacunasAplicadas;
	private CentroAlmacenamiento centroAlmacenamiento;
	private Inscripcion inscripciones;

	private Fecha diaVacunacionAnterior;

	public CentroVacunacion(String nombreCentro, int capacidadVacunacionDiaria) {
		
		if (nombreCentro.equals("") || nombreCentro.length() > 50)
			throw new RuntimeException("Por favor, ingrese los datos correctamente, el nombre del centro no es valido o es muy largo");
		
		if(capacidadVacunacionDiaria <= 0) 
			throw new RuntimeException("Por favor, ingrese los datos correctamente, la capacidad de vacunacion debe ser mayor a 0");

		
		this.nombre = nombreCentro;
		this.capacidadVacunacionDiaria = capacidadVacunacionDiaria;
		this.cantidadVacunados = 0;
		this.centroAlmacenamiento = new CentroAlmacenamiento();
		this.inscripciones = new Inscripcion();
		this.vacunasAplicadas = new HashMap<Integer, Paciente>();
		this.diaVacunacionAnterior = new Fecha(1, 1, 1900);
		
	}

	public void ingresarVacunas(String nombreVacuna, int cantidad, Fecha fechaIngreso) {
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
	
	
	public void generarTurnos(Fecha fechaInicial) {
		
		// la fecha inicial debe ser una fecha valida
		if (Fecha.hoy().compareTo(fechaInicial) > 0)
			throw new RuntimeException("La fecha inicial es superior a la actual");
		
			 Fecha fecha = new Fecha(fechaInicial.dia(), fechaInicial.mes(), fechaInicial.anio());
			 inscripciones.retirarPacientesConTurnoVencido();
			 centroAlmacenamiento.eliminarVacunasVencidasOnoDisponibles(fecha); 
		

	    int turnosAsignados = 0;
	    
		 for (Paciente paciente : inscripciones.obtenerListaEspera()) {
			
				if (centroAlmacenamiento.disponibilidadVacunasAplicablesAlPaciente(paciente) > 0) {	
				
					asignarTurno(paciente, fecha);
					turnosAsignados ++;			
				}
			
				if (turnosAsignados == capacidadVacunacionDiaria) {
					turnosAsignados = 0;
					fecha.avanzarUnDia();
			    }		
			
		}

	}

	public List<Integer> turnosConFecha(Fecha fecha) {
		return inscripciones.dniDePacientesConTurno(fecha);
	}

	public Paciente vacunarInscripto(int dni, Fecha fechaVacunacion) {

		Paciente pacienteVacunado = null;

		if (cantidadVacunados < capacidadVacunacionDiaria 
				&& fechaVacunacion.posterior(diaVacunacionAnterior)) {

			boolean vacunado = false;

				for (Paciente paciente : inscripciones.getPacientesConTurno()) {
					if (paciente.getDni() == dni) {

						if (paciente.getFechaTurno().equals(fechaVacunacion)) {
							centroAlmacenamiento.retirarVacuna(paciente.getVacunaAsignada());
							vacunasAplicadas.put(paciente.getDni(), paciente);
							vacunado = true;
							pacienteVacunado = paciente;
						} else {
							throw new RuntimeException(
									"La fecha de vacunacion no corresponde con la fecha de inscripcion");
						}
					}
				}


			if (vacunado == false) {
				throw new RuntimeException("La persona a vacunar no esta inscripta");
			} else {
				inscripciones.retirarPacientesConTurnoVencido();
				cantidadVacunados++;
			}
		} else {
			diaVacunacionAnterior = new Fecha(fechaVacunacion.dia(), fechaVacunacion.mes(), fechaVacunacion.anio());
			cantidadVacunados = 0;
		}

		return pacienteVacunado;
	}


	private void asignarTurno(Paciente paciente, Fecha fechaInicial) {
 		
					Fecha fechaVacunacion = new Fecha(fechaInicial.dia(), fechaInicial.mes(), fechaInicial.anio());

					Vacuna vacuna = centroAlmacenamiento.obtenerVacuna(paciente);
					paciente.setVacunaAsignada(vacuna);
					paciente.setFechaTurno(fechaVacunacion);

					inscripciones.setTurnosPorFecha(fechaVacunacion, paciente);
				 
	}

	

	public HashMap<Integer, Paciente> reporteVacunacion() {
		
		HashMap<Integer, Paciente> vac = new HashMap<Integer, Paciente>();
		Iterator<Integer> dniIt = vacunasAplicadas.keySet().iterator();
		while(dniIt.hasNext()) {
			int dni = dniIt.next();
			Paciente pa = vacunasAplicadas.get(dni);
			vac.put(dni, pa);
		}
		return vac;

	}

	public Map<String, Integer> reporteVacunasVencidas() {
		return centroAlmacenamiento.getVacunasVencidas();
	}

	public static void main(String[] args) {
		CentroVacunacion centro = new CentroVacunacion("UNGS", 5);
		centro.ingresarVacunas("Sputnik", 10, Fecha.hoy());
		centro.ingresarVacunas("Astrazeneca", 10, Fecha.hoy());
		centro.ingresarVacunas("Moderna", 10, Fecha.hoy());
		centro.ingresarVacunas("Sinopharm", 10, Fecha.hoy());
		centro.ingresarVacunas("Pfizer", 10, Fecha.hoy());

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
	
	
	@Override
	public String toString() {

		StringBuilder datosInscriptos = new StringBuilder();

		datosInscriptos.append("                                       " + "----------------------------------")
				.append("\n").append("                                       ").append("Centro: " + nombre).append("\n")
				.append("                                       " + "----------------------------------").append("\n")

				.append("                                       ").append("Capacidad de vacunacion diaria:")
				.append(capacidadVacunacionDiaria).append("\n").append("Pacientes Inscriptos").append("\n");


			for (Paciente paciente : inscripciones.verListaPorPrioridad()) {

				datosInscriptos.append("****************************").append("\n").append("Paciente -> DNI:")
						.append(paciente.getDni()).append("\n").append("Prioridad -> ").append(paciente.getPrioridad())
						.append("\n").append("Personal salud -> ").append(paciente.isPersonalSalud()).append("\n")
						.append("Padecimientos -> ").append(paciente.isEnfermedadPreexistente()).append("\n")
						.append("****************************").append("\n");

			}



		datosInscriptos.append("                                       " + "----------------------------------")
				.append("\n").append("                                       ").append("Iniciando vacunación")
				.append("\n").append("                                       " + "----------------------------------")
				.append("\n");

		Iterator<Fecha> fechas = inscripciones.getFechasTurnos().keySet().iterator();

		while (fechas.hasNext()) {

			Fecha fecha = fechas.next();

			for (Paciente paciente : inscripciones.getFechasTurnos().get(fecha)) {

				datosInscriptos.append("                                       ");

				Paciente vacunado = vacunarInscripto(paciente.getDni(), Fecha.hoy());
				datosInscriptos.append((vacunado != null)
						? "Paciente (vacunado) Dni: " + vacunado.getDni() + " Vacuna: "
								+ vacunado.getVacunaAsignada().getNombre() + " Prioridad: " + vacunado.getPrioridad()
						: "").append("\n");

			}

		}
		return datosInscriptos.toString();
	}
	
	
}
