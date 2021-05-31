


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Inscripcion {

	private Map<Integer, ArrayList<Paciente>> listaEsperaConPrioridad; // se uso
	private Map<Integer, ArrayList<Paciente>> listaConTurnos;
	
	private Map<Fecha, ArrayList<Paciente>> turnosConFecha; // guardaria la fecha y pacientes aun no se uso
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
		
		turnosConFecha = new HashMap<Fecha, ArrayList<Paciente>>();
		turnosConFecha.put(new Fecha(), new ArrayList<Paciente>());
		
		vacunasParaTodoPublico = new ArrayList<String>();
		vacunasParaTodoPublico.add("Sinopharm");
		vacunasParaTodoPublico.add("Moderna");
		vacunasParaTodoPublico.add("AstraZeneca");
		
		vacunasParaMayoresSesenta = new ArrayList<String>();
		vacunasParaMayoresSesenta.add("Pfizer");
		vacunasParaMayoresSesenta.add("Sputnik");


		
		fecha = new Fecha();

	}

	public void inscribirCiudadano(int dni, Fecha edad, boolean enfermedad, boolean personalSalud ) {
		if (Fecha.diferenciaAnios(fecha, edad) < 18)
			throw new RuntimeException("El paciente debe ser mayor de 18 años");
		
		
		listaEsperaConPrioridad.forEach((prioridad, arrayList)->{
			
			for(Paciente paciente: arrayList) {
				if(paciente.getDni() == dni) {
					throw new RuntimeException("El paciente ya se encuentra inscripto en el sistema");
				}
			}
			
		});
		
		Paciente paciente = new Paciente(dni, edad, enfermedad, personalSalud);
		
		if(paciente.isPersonalSalud()) {
			paciente.setPrioridad(1);
			paciente.setVacunasAplicables(vacunasParaTodoPublico);
		}else if (Fecha.diferenciaAnios(fecha, paciente.getEdad()) >= 60) {
			paciente.setPrioridad(2);
			paciente.setVacunasAplicables(vacunasParaMayoresSesenta);
		}else if (paciente.isEnfermedadPreexistente()) {
			paciente.setPrioridad(3);
			paciente.setVacunasAplicables(vacunasParaTodoPublico);
		}else {
			paciente.setPrioridad(4);
			paciente.setVacunasAplicables(vacunasParaTodoPublico);


		}
	
		// ingreso todos los que existen a una lista por prioridad, primero los 1, 2, 3
		// y 4

		listaEsperaConPrioridad.get(paciente.getPrioridad()).add(paciente);
//		if (inscripcionesListaEspera.get(dni).vacunado)
//			throw new RuntimeException("El paciente ya fue vacunado");
		
	
	}




	public Map<Integer, ArrayList<Paciente>> verListaPorPrioridad() { // listo
		HashMap<Integer, ArrayList<Paciente>> n = (HashMap<Integer, ArrayList<Paciente>>) listaEsperaConPrioridad;
		return n;
	}

	public ArrayList<Integer> dniDePacientesConTurno(Fecha f) { // aun no, es necesario la asignacion de turnos
		ArrayList<Integer> dniPacientes = new ArrayList<Integer>();
		turnosConFecha.get(fecha);
		for (Paciente paciente : turnosConFecha.get(fecha)) { // aun no tiene nada porque tengo que ver como le asigno
																// fecha de turno, se hace en otro metodo esto solo
																// devuelve la lista
			dniPacientes.add(paciente.getDni());
		}
		return dniPacientes;
	}
	
	public void setTurnosPorFecha(Fecha f, Paciente paciente) {
	System.out.println(f);
		if(turnosConFecha.containsKey(f) == false) {
			ArrayList<Paciente> pacientes = new ArrayList<Paciente>();
			pacientes.add(paciente);
			Fecha fecha = new Fecha(f.dia(), f.mes(), f.anio());
			turnosConFecha.put(fecha,pacientes);
		}else {
			turnosConFecha.get(f).add(paciente);
		}

	}
	
	public void quitarPacienteListaEspera(ArrayList<Paciente> pacientesConTurno) {
		
		
		for(Paciente pac : pacientesConTurno) {
			
			Iterator it = listaEsperaConPrioridad.get(pac.getPrioridad()).iterator();

			while (it.hasNext()) {
				Paciente pa = (Paciente) it.next();
				if(pa.getDni() == pac.getDni())
					it.remove();
			}
			
		}
		

	}

	
	public void agregarPacienteConTurno(final Paciente paciente) {
		
		listaConTurnos.forEach((prioridad, arrayList)->{
			
			for(Paciente pa: arrayList) {
				if(pa.getDni() == paciente.getDni()) {
					throw new RuntimeException("El paciente ya tiene un turno");
				}
			}
			
		});

		listaConTurnos.get(paciente.getPrioridad()).add(paciente);
	}
	
	public Map<Integer, ArrayList<Paciente>> getPacientesConTurno() {
		return listaConTurnos;
	}
	
	public  ArrayList<Integer>  pacientesSinTurno() {
		
		ArrayList<Integer> pacientesSinTurno = new ArrayList<Integer>();
	
		listaEsperaConPrioridad.forEach((prioridad, arrayList)->{
			for(Paciente paciente: arrayList) {
				if(paciente.getFechaTurno() == null) {
					pacientesSinTurno.add(paciente.getDni());
				}
			}
		});
		
		return pacientesSinTurno;
	}
	
	public ArrayList<Integer> pacientesInscriptos() {

		ArrayList<Integer> dnis = new ArrayList<Integer>();
		
		listaEsperaConPrioridad.forEach((prioridad, arrayList)->{
		
			for(Paciente paciente: arrayList) {
				dnis.add(paciente.getDni());
			}
			
		});
		return dnis;
	}

	
	public Map<Integer, ArrayList<Paciente>> obtenerListaEspera() {
		return listaEsperaConPrioridad;
	}
	
public static void main(String[] args) {
		
		Inscripcion inscripcion = new Inscripcion();
		inscripcion.inscribirCiudadano(30066008, new Fecha(12, 5, 1982), false, true);
		inscripcion.inscribirCiudadano(14553555, new Fecha(12, 4, 1914), false, false);
		inscripcion.inscribirCiudadano(11725668, new Fecha(2, 3, 1956), false, false);
		inscripcion.inscribirCiudadano(45223332, new Fecha(14, 3, 2000), false, false);
		inscripcion.inscribirCiudadano(45000055, new Fecha(18, 3, 2000), false, false);

		inscripcion.agregarPacienteConTurno(new Paciente(30066008, new Fecha(12, 5, 1982), false, true));
		
		System.out.println(inscripcion.getPacientesConTurno());
	}


}
