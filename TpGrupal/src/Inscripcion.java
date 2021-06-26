
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

//no debe manejar vacunas
public class Inscripcion {

	private Map<Integer, ArrayList<Paciente>> listaEsperaConPrioridad;
	private Map<Integer, ArrayList<Paciente>> listaConTurnos;

	private TreeMap<Fecha, ArrayList<Paciente>> turnosConFecha;

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

		fecha = new Fecha();

	}

	public void inscribirCiudadano(int dni, Fecha edad, boolean enfermedad, boolean personalSalud) { //bien
		if (Fecha.diferenciaAnios(fecha, edad) < 18)
			throw new RuntimeException("El paciente debe ser mayor de 18 años");

		listaEsperaConPrioridad.forEach((prioridad, arrayList) -> {
			for (Paciente paciente : arrayList) {//*
				if (paciente.getDni() == dni) {
					throw new RuntimeException("El paciente ya se encuentra inscripto en el sistema");
				}
			}//*

		});

		Paciente paciente = new Paciente(dni, edad, enfermedad, personalSalud);

		if (paciente.isPersonalSalud()) {
			paciente.setPrioridad(1);
			
		} else if (Fecha.diferenciaAnios(fecha, paciente.getEdad()) >= 60) {
			paciente.setPrioridad(2);
			
		} else if (paciente.isEnfermedadPreexistente()) {
			paciente.setPrioridad(3);
			
		} else {
			paciente.setPrioridad(4);
			
		}

		// ingresamos segun su prioridad
		listaEsperaConPrioridad.get(paciente.getPrioridad()).add(paciente);

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
	public void retirarPacientesConTurnoVencido() {
		
		
		Iterator<Integer> prioridad = listaEsperaConPrioridad.keySet().iterator();

		while (prioridad.hasNext()) { // 1, 2, 3, 4, 5

			int priori = prioridad.next();

			ArrayList<Paciente> pacientesPorPrioridad = obtenerListaEspera().get(priori);
			// 1, <arraylist>pacientes
			// 2, <arraylist>pacientes...
			Iterator<Paciente> listPaciente = pacientesPorPrioridad.iterator();

			while (listPaciente.hasNext()) {
				Paciente pa = listPaciente.next();
				// Devolvemos vacunas al stock
				// elimamos del sistema al paciente con turno vencido
				if (pa.getFechaTurno() != null) {
					// si tiene un turno ->
					if (Fecha.hoy().posterior(pa.getFechaTurno()) && pa.getVacunaAsignada() != null) {
						
					//	centroAlmacenamiento.devolverVacunaAlStock(pa.getVacunaAsignada()); 
						// vacuna asignada disponible nuevamente en el stock
						pa.getVacunaAsignada().setDisponible(true); 
						retirarPacienteConTurno(pa);
						listPaciente.remove();
					}
					

				}

			}

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
