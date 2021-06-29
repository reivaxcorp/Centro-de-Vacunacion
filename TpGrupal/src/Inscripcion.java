
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class Inscripcion {

	private ArrayList<Paciente> pacientes;
	private TreeMap<Fecha, ArrayList<Paciente>> turnosConFecha;

	private Fecha fecha;

	public Inscripcion() {
		pacientes = new ArrayList<Paciente>();
		turnosConFecha = new TreeMap<Fecha, ArrayList<Paciente>>();
		fecha = new Fecha();
	}

	public void inscribirCiudadano(int dni, Fecha edad, boolean enfermedad, boolean personalSalud) { //bien
		
		
		
		if (Fecha.diferenciaAnios(fecha, edad) < 18)
			throw new RuntimeException("El paciente debe ser mayor de 18 años");

	
			for (Paciente paciente : pacientes) {
				if (paciente.getDni() == dni) {
					throw new RuntimeException("El paciente ya se encuentra inscripto en el sistema");
				}
			}
			
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

		pacientes.add(paciente);
		ordenarPorBurbujeo(pacientes);
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
		
		if(paciente == null) return;
		
		if (turnosConFecha.get(f) == null) {
			ArrayList<Paciente> pacientes = new ArrayList<Paciente>();
			pacientes.add(paciente);
			Fecha fecha = new Fecha(f.dia(), f.mes(), f.anio());
			turnosConFecha.put(fecha, pacientes);

		} else {
			turnosConFecha.get(f).add(paciente);
		}

	}
	
	// elimamos del sistema al paciente con turno vencido
	public void retirarPacientesConTurnoVencido() {	
		
		Iterator<Paciente> pacienteIt = pacientes.iterator();
		ArrayList<Paciente> pacientesConTurnoVencido = new ArrayList<Paciente>();
		
		while (pacienteIt.hasNext()) {

			Paciente pa = pacienteIt.next();
			
				if (pa.getFechaTurno() != null) {
					
					if (Fecha.hoy().posterior(pa.getFechaTurno()) && pa.getVacunaAsignada() != null) {
						// vacuna asignada disponible nuevamente en el stock
						pa.getVacunaAsignada().setDisponible(true);
						pacientesConTurnoVencido.add(pa);
						pacienteIt.remove();
					}		
			 }
		}
		retirarPacienteConTurno(pacientesConTurnoVencido);
	}
	
	
	private String retirarPacienteConTurno(ArrayList<Paciente> paVenc) {

		if (paVenc.isEmpty()) {
			return "null";
		}

		Iterator<Paciente> pacienteIt = pacientes.iterator();

			while (pacienteIt.hasNext()) {

				Paciente pa = pacienteIt.next();

				for(Paciente pv : paVenc) {
					if (pv.getDni() == pa.getDni()) {
						pacienteIt.remove();
						return "Paciente retirado de la lista con turnos";
					}
				}
			
			}

		return "El paciente no se encuentro en la lista de turnos";
	}

	


	public ArrayList<Paciente> getPacientesConTurno() {
		ArrayList<Paciente> pacientesConTurno = new ArrayList<Paciente>();
		
		for (Paciente paciente : pacientes) {
			if (paciente.getFechaTurno() != null) {
				pacientesConTurno.add(paciente);
			}
		}
		return pacientesConTurno;
	}

	public ArrayList<Integer> pacientesSinTurno() {

		ArrayList<Integer> pacientesSinTurno = new ArrayList<Integer>();

			for (Paciente paciente : pacientes) {
				if (paciente.getFechaTurno() == null) {
					pacientesSinTurno.add(paciente.getDni());
				}
			}
		
		return pacientesSinTurno;
	}

	
	
	public ArrayList<Paciente> verListaPorPrioridad() {

		ArrayList<Paciente> paOrdenadosList = new ArrayList<Paciente>();
			
		for (Paciente paciente : pacientes) {
			paOrdenadosList.add(paciente);
			}

		return paOrdenadosList;
	}
	

	public TreeMap<Fecha, ArrayList<Paciente>> getFechasTurnos() {
	
		TreeMap<Fecha, ArrayList<Paciente>> turnos = 
				new TreeMap<>();
		
		Iterator<Fecha> fechaIt = turnosConFecha.keySet().iterator();
		
		while(fechaIt.hasNext()) {
			Fecha fech = fechaIt.next();
			ArrayList<Paciente> pacientes = turnosConFecha.get(fech);
			turnos.put(fech, pacientes);
		}
		
		return turnos;
	
	}

	public  ArrayList<Paciente> obtenerListaEspera() {
		
		ArrayList<Paciente> listaEnEspera = new ArrayList<Paciente>();
		
		for(Paciente pa: pacientes) {
			if(pa.getFechaTurno() == null)
				listaEnEspera.add(pa);
		}
		return listaEnEspera;
		 
	}
	

	
	public static void main(String[] args) {
		
		Inscripcion inscripcion = new Inscripcion();
		
		inscripcion.inscribirCiudadano(34701000, new Fecha(1, 5, 1989), false, false); // 32 NS NP 4
		inscripcion.inscribirCiudadano(29959000, new Fecha(20, 11, 1982), false, true); // 38 S NP 1
		inscripcion.inscribirCiudadano(24780201, new Fecha(1, 6, 1972), true, false); // 49 NS P 3
		inscripcion.inscribirCiudadano(29223000, new Fecha(2, 5, 1982), false, true); // 39 S NP 1
		inscripcion.inscribirCiudadano(13000000, new Fecha(1, 5, 1958), true, false); // 63 NS P 2
		inscripcion.inscribirCiudadano(13000050, new Fecha(20, 6, 1958), false, true); // 62 S NP 1
		inscripcion.inscribirCiudadano(14000000, new Fecha(1, 1, 1961), false, false); // 60 NS NP 2
		inscripcion.inscribirCiudadano(14005000, new Fecha(20, 12, 1961), true, false); 
		
		System.out.println(inscripcion);
		
	}

	// metodo aux	
	private void ordenarPorBurbujeo(ArrayList<Paciente> a) {
		 
		for (int i = 1; i < a.size(); i++) { 
			for (int j = 0; j < a.size()-i; j++) {
 					if (a.get(j).getPrioridad() > a.get(j+1).getPrioridad()) 
					swap(j, j+1, a);   
					} 
			} 
		}   
	
	// metodo aux
	private void swap( int n, int m, ArrayList<Paciente> a){ 
		Paciente aux = a.get(n); 
		a.set(n, a.get(m));
		a.set(m, aux);
     }
	
	
	@Override
	public String toString() {
		
		StringBuilder datos = new StringBuilder();
		datos.append("Paciente sin turnos: ").append("\n");
		
		for(int pa: pacientesSinTurno())
			datos.append(pa).append("\n");
		
		datos.append("Paciente por prioridad: ").append("\n");
		
		for(Paciente pa: verListaPorPrioridad())
			datos
			.append("Prioridad:")
			.append(pa.getPrioridad())
			.append("-Dni:")
			.append(pa.getDni())
			.append("-Edad:")
			.append(Fecha.diferenciaAnios(Fecha.hoy(), pa.getEdad()))
			.append("\n");
		
		datos.append("Paciente con turnos: ").append("\n");
		
		for(Paciente pa: getPacientesConTurno())
			datos.append(pa.getDni()).append(" Fecha:").append(pa.getFechaTurno()).append("\n");
		
		return datos.toString();
	}
}
