
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Inscripcion {

	private Map<Integer, ArrayList<Paciente>> listaEsperaConPrioridad;
	private Map<Integer, ArrayList<Paciente>> listaConTurnos;

	private TreeMap<Fecha, ArrayList<Paciente>> turnosConFecha; // guardaria la fecha y pacientes aun no se uso
	private ArrayList<String> vacunasParaTodoPublico;
	private ArrayList<String> vacunasParaMayoresSesenta;

	private Fecha fecha;

	public Inscripcion() {
		listaEsperaConPrioridad = new HashMap<Integer, ArrayList<Paciente>>();
		listaConTurnos = new HashMap<Integer, ArrayList<Paciente>>();

		// inicializamos los ArrayList en el HashMap para las prioridades
		listaEsperaConPrioridad.put(1, new ArrayList<Paciente>());
		listaEsperaConPrioridad.put(2, new ArrayList<Paciente>());
		listaEsperaConPrioridad.put(3, new ArrayList<Paciente>());
		listaEsperaConPrioridad.put(4, new ArrayList<Paciente>());

		listaConTurnos.put(1, new ArrayList<Paciente>());
		listaConTurnos.put(2, new ArrayList<Paciente>());
		listaConTurnos.put(3, new ArrayList<Paciente>());
		listaConTurnos.put(4, new ArrayList<Paciente>());

		turnosConFecha = new TreeMap<Fecha, ArrayList<Paciente>>();

		vacunasParaTodoPublico = new ArrayList<String>();
		vacunasParaTodoPublico.add("Sinopharm");
		vacunasParaTodoPublico.add("Moderna");
		vacunasParaTodoPublico.add("Astrazeneca");

		vacunasParaMayoresSesenta = new ArrayList<String>();
		vacunasParaMayoresSesenta.add("Pfizer");
		vacunasParaMayoresSesenta.add("Sputnik");

		fecha = new Fecha();

	}

	public void inscribirCiudadano(int dni, Fecha edad, boolean enfermedad, boolean personalSalud) {
		if (Fecha.diferenciaAnios(fecha, edad) < 18)
			throw new RuntimeException("El paciente debe ser mayor de 18 años");

		listaEsperaConPrioridad.forEach((prioridad, arrayList) -> {

			for (Paciente paciente : arrayList) {

				if (paciente.getDni() == dni) {
					throw new RuntimeException("El paciente ya se encuentra inscripto en el sistema");
				}
			}

		});

		Paciente paciente = new Paciente(dni, edad, enfermedad, personalSalud);

		if (paciente.isPersonalSalud()) {
			paciente.setPrioridad(1);
			//paciente.setVacunasAplicables(vacunasParaTodoPublico);
		} else if (Fecha.diferenciaAnios(fecha, paciente.getEdad()) >= 60) {
			paciente.setPrioridad(2);
			//paciente.setVacunasAplicables(vacunasParaMayoresSesenta);
		} else if (paciente.isEnfermedadPreexistente()) {
			paciente.setPrioridad(3);
			//paciente.setVacunasAplicables(vacunasParaTodoPublico);
		} else {
			paciente.setPrioridad(4);
			//paciente.setVacunasAplicables(vacunasParaTodoPublico);

		}

		// ingresamos segun su prioridad
		listaEsperaConPrioridad.get(paciente.getPrioridad()).add(paciente);

	}

	@Override
	public String toString() {

		StringBuilder inscripcion = new StringBuilder();

		inscripcion.append("                                       " + "----------------------------------")
				.append("\n");
		inscripcion.append("                                       " + "Pacientes con turnos para la fecha\n");

		Iterator<Fecha> fechas = turnosConFecha.keySet().iterator();
		while (fechas.hasNext()) {

			Fecha fecha = fechas.next();

			inscripcion.append("                                       " + "----------------------------------")
					.append("\n");
			inscripcion.append("                                       ").append(fecha).append("\n")
					.append("                                       " + "----------------------------------")
					.append("\n");

			Iterator<Paciente> pacientesList = turnosConFecha.get(fecha).iterator();

			while (pacientesList.hasNext()) {

				Paciente pa = pacientesList.next();

				inscripcion.append("DNI: ").append(pa.getDni()).append("\n").append("Prioridad: ")
						.append(pa.getPrioridad()).append("\n").append("Personal salud: ")
						.append((pa.isPersonalSalud()) ? "si" : "no").append("\n").append("Enfermedad preexistente: ")
						.append((pa.isEnfermedadPreexistente()) ? "si" : "no").append("\n").append("Fecha turno: ")
						.append(pa.getFechaTurno()).append("\n").append("Vacuna asignada: ")
						.append(pa.getVacunaAsignada().getNombre()).append("\n").append("----------------------")
						.append("\n");

			}

		}

		return inscripcion.toString();
	}

	public Map<Integer, ArrayList<Paciente>> verListaPorPrioridad() {
		return listaEsperaConPrioridad;
	}

	public ArrayList<Integer> dniDePacientesConTurno(Fecha f) {
		ArrayList<Integer> dniPacientes = new ArrayList<Integer>();

		if (turnosConFecha.get(f) != null) {
			for (Paciente paciente : turnosConFecha.get(f)) {
				dniPacientes.add(paciente.getDni());
			}
		}

		return dniPacientes;
	}

	public void setTurnosPorFecha(Fecha f, Paciente paciente) {

		if (turnosConFecha.get(f) == null) {
			ArrayList<Paciente> pacientes = new ArrayList<Paciente>();
			pacientes.add(paciente);
			Fecha fecha = new Fecha(f.dia(), f.mes(), f.anio());
			turnosConFecha.put(fecha, pacientes);

		} else {
			turnosConFecha.get(f).add(paciente);
		}

	}

	public String retirarPacienteConTurno(Paciente pa) {

		if (pa == null) {
			return "null";
		}

		Iterator<Integer> prioridad = listaConTurnos.keySet().iterator();

		while (prioridad.hasNext()) {

			int itPrioridad = prioridad.next();

			Iterator<Paciente> pacienteIt = listaConTurnos.get(itPrioridad).iterator();

			while (pacienteIt.hasNext()) {

				Paciente itPaciente = pacienteIt.next();

				if (itPaciente.getDni() == pa.getDni()) {
					pacienteIt.remove();
					return "Paciente retirado de la lista con turnos";
				}

			}

		}

		return "El paciente no se encuentro en la lista de turnos";

	}

	public void agregarPacienteConTurno(final Paciente paciente) {

		listaConTurnos.forEach((prioridad, arrayList) -> {

			for (Paciente pa : arrayList) {
				if (pa.getDni() == paciente.getDni()) {
					throw new RuntimeException("El paciente ya tiene un turno");
				}
			}

		});

		listaConTurnos.get(paciente.getPrioridad()).add(paciente);
	}

	public Map<Integer, ArrayList<Paciente>> getPacientesConTurno() {
		return listaConTurnos;
	}

	public ArrayList<Integer> pacientesSinTurno() {

		ArrayList<Integer> pacientesSinTurno = new ArrayList<Integer>();

		listaEsperaConPrioridad.forEach((prioridad, arrayList) -> {
			for (Paciente paciente : arrayList) {
				if (paciente.getFechaTurno() == null) { 
					pacientesSinTurno.add(paciente.getDni());
				}
			}
		});

		return pacientesSinTurno;
	}

	public ArrayList<Integer> pacientesInscriptos() {

		ArrayList<Integer> dnis = new ArrayList<Integer>();

		listaEsperaConPrioridad.forEach((prioridad, arrayList) -> {

			for (Paciente paciente : arrayList) {
				dnis.add(paciente.getDni());
			}

		});
		return dnis;
	}

	public TreeMap<Fecha, ArrayList<Paciente>> getFechasTurnos() {
		return turnosConFecha;
	}

	public Map<Integer, ArrayList<Paciente>> obtenerListaEspera() {
		return listaEsperaConPrioridad;
	}

}
