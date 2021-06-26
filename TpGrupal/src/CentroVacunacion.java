
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

	/*
	 * alta cohesion - bajo acoplamiento
	 */
	/*
	 * cohesion: (alta)
	 * acoplamiento: (alta) x = se relacionan 3 clases: Vacuna,
	 * centroAlmacenamiento y CentroVacunatorio Si algo cambia en Vacuna, ejemplo,
	 * recibe otro atributo, es necesario cambiar el metodo agregarVacunas de
	 * centroAlmacenamiento y dependiendo de la importancia de ese atributo seria
	 * necesario agregarlo en ingresarVacunas --
	 * 
	 * 
	 */

	public void ingresarVacunas(String nombreVacuna, int cantidad, Fecha fechaIngreso) {
		centroAlmacenamiento.agregarVacunas(nombreVacuna, cantidad, fechaIngreso);
	}

	/*
	 * cohesion: (alta) 
	 * acoplamiento: (bajo) solo maneja atributos del objeto
	 */
	public int vacunasDisponibles() {

		return centroAlmacenamiento.vacunasDisponibles();
	}
	/*
	 * cohesion(alta) acoplamiento(bajo) solo maneja atributos del objeto
	 */

	public int vacunasDisponibles(String nombreVacuna) {
		return centroAlmacenamiento.obtenerCantidadDevacunasPorNombre(nombreVacuna);
	}

	/*
	 * cohesion(alta)
	 * acoplamiento(bajo) sigue la misma idea de las vacunas, aca se relacionan 3 tads
	 *
	 */
	public void inscribirPersona(int dni, Fecha nacimiento, boolean tienePadecimientos, boolean esEmpleadoSalud) {
		inscripciones.inscribirCiudadano(dni, nacimiento, tienePadecimientos, esEmpleadoSalud);
	}
	/*
	 * cohesion(alta)
	 * acoplamiento(medio) depende de tads pero solo con sus tipos primitivos en Paciente
	 *
	 */
	public ArrayList<Integer> listaDeEspera() {
		return inscripciones.pacientesSinTurno();
	}
	/*
	 * cohesion(alta)
	 * acoplamiento(medio) 
	 *
	 */
	
	public void generarTurnos(Fecha fechaInicial) {
		if (Fecha.hoy().compareTo(fechaInicial) > 0)
			throw new RuntimeException();
		
		if (this.fecha == null)
			fecha = new Fecha(fechaInicial.dia(), fechaInicial.mes(), fechaInicial.anio());
		inscripciones.retirarPacientesConTurnoVencido();
		centroAlmacenamiento.eliminarVacunasVencidasOnoDisponibles(fecha); // aun NO elimino vacunas vencidas
		//centroAlmacenamiento.actualizarVacunas(); //elimina vacunas no disponibles o vencidas
		

		for (int prioridad : inscripciones.obtenerListaEspera().keySet()) {
			// 1,2,3,4
			switch (prioridad) {
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

		if (cantidadVacunados < capacidadVacunacionDiaria && fechaVacunacion.posterior(diaVacunacionAnterior)) {

			Iterator iterator = inscripciones.getPacientesConTurno().keySet().iterator();
			boolean vacunado = false;

			while (iterator.hasNext()) {
				int prioridad = (int) iterator.next();

				for (Paciente paciente : inscripciones.getPacientesConTurno().get(prioridad)) {
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

			}

			if (vacunado == false) {
				throw new RuntimeException("La persona a vacunar no esta inscripta");
			} else {
				inscripciones.retirarPacienteConTurno(pacienteVacunado);
				cantidadVacunados++;
			}
		} else {
			diaVacunacionAnterior = new Fecha(fechaVacunacion.dia(), fechaVacunacion.mes(), fechaVacunacion.anio());
			cantidadVacunados = 0;
		}

		return pacienteVacunado;
	}


	private int asignarTurno(int prioridad, Fecha fechaInicial, int pacientesRestantes) {

		if (pacientesRestantes == 0)
			return 0;
		/*
		 * Alto acoplamiento: 3 clases dependen de si mismas: Paciente, Inscripcion,
		 * centroAlmacenamiento un Paciente tiene vacunas aplicables que eso se lo debe
		 * setear mediante la clase Inscripcion y mediante centroAlmacenamiento me da la
		 * cantidad de vacunas que hay de ese tipo en el momento si cambia algo de una
		 * clase, aunque sea un atributo habra que modificar algunas otras cosas
		 * 
		 */

		int turnosParaSiguienteFecha = 0;// guardamos los turnos que nos falta completar para el siguiente dia

		for (Paciente paciente : inscripciones.obtenerListaEspera().get(prioridad)) {
			if (turnosAsignados < capacidadVacunacionDiaria
					&& centroAlmacenamiento.cantidadVacunasAplicablesAlPaciente(paciente) > 0) { //modificar
				//para que dado un paciente me diga si esa vacuna es aplicable
				if (paciente.getFechaTurno() == null) {
					
					Fecha fechaVacunacion = new Fecha(fechaInicial.dia(), fechaInicial.mes(), fechaInicial.anio());
					paciente.setFechaTurno(fechaVacunacion);

					Vacuna vacuna = centroAlmacenamiento.obtenerVacuna(paciente);
					paciente.setVacunaAsignada(vacuna);
					paciente.setFechaTurno(fechaVacunacion);

					inscripciones.setTurnosPorFecha(fechaVacunacion, paciente);
					inscripciones.agregarPacienteConTurno(paciente);
					turnosAsignados++;

				}

			} else if (turnosParaSiguienteFecha < capacidadVacunacionDiaria) {
				if (paciente.getFechaTurno() == null) {
					turnosParaSiguienteFecha++;
				}
			}

		}

		if (turnosAsignados == capacidadVacunacionDiaria) {
			turnosAsignados = 0;
			fechaInicial.avanzarUnDia();

		}

		// System.out.println(numeroPacientesRestantes);
		return asignarTurno(prioridad, fechaInicial, turnosParaSiguienteFecha);
	}

	@Override
	public String toString() {

		StringBuilder datosInscriptos = new StringBuilder();

		datosInscriptos.append("                                       " + "----------------------------------")
				.append("\n").append("                                       ").append("Centro: " + nombre).append("\n")
				.append("                                       " + "----------------------------------").append("\n")

				.append("                                       ").append("Capacidad de vacunacion diaria:")
				.append(capacidadVacunacionDiaria).append("\n").append("Pacientes Inscriptos").append("\n");

		for (int prioridad : inscripciones.verListaPorPrioridad().keySet()) {

			for (Paciente paciente : inscripciones.verListaPorPrioridad().get(prioridad)) {

				datosInscriptos.append("****************************").append("\n").append("Paciente -> DNI:")
						.append(paciente.getDni()).append("\n").append("Prioridad -> ").append(paciente.getPrioridad())
						.append("\n").append("Personal salud -> ").append(paciente.isPersonalSalud()).append("\n")
						.append("Padecimientos -> ").append(paciente.isEnfermedadPreexistente()).append("\n")
						.append("****************************").append("\n");

			}

		}

		datosInscriptos.append(inscripciones.toString());

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
		centro.ingresarVacunas("AstraZeneca", 10, Fecha.hoy());
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
}
