


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Inscripcion {

	private Map<Integer, ArrayList<Paciente>> listaEsperaConPrioridad; // se uso
	private Map<Integer, ArrayList<Paciente>> listaConTurnos;
	
	private HashMap<Fecha, ArrayList<Paciente>> turnosConFecha; // guardaria la fecha y pacientes aun no se uso
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
			throw new RuntimeException("El paciente debe ser mayor de 18 a�os");
		
		
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
	
		 // ingresamos segun su prioridad 
		listaEsperaConPrioridad.get(paciente.getPrioridad()).add(paciente);

	
	}




	public Map<Integer, ArrayList<Paciente>> verListaPorPrioridad() {
		return listaEsperaConPrioridad;
	}

	public ArrayList<Integer> dniDePacientesConTurno(Fecha f) { 
		ArrayList<Integer> dniPacientes = new ArrayList<Integer>();
		
		if(turnosConFecha.get(f) != null) {
			for (Paciente paciente : turnosConFecha.get(f)) { 
				dniPacientes.add(paciente.getDni());
			}
		}
		
		return dniPacientes;
	}
	
	public void setTurnosPorFecha(Fecha f, Paciente paciente) {

		if(turnosConFecha.get(f) == null) {
			ArrayList<Paciente> pacientes = new ArrayList<Paciente>();
			pacientes.add(paciente);
			Fecha fecha = new Fecha(f.dia(), f.mes(), f.anio());
			turnosConFecha.put(fecha,pacientes);
		
		}else {
			turnosConFecha.get(f).add(paciente);
		}

	}


	
	public void agregarPacienteConTurno(final Paciente paciente) {
		
		listaConTurnos.forEach((prioridad, arrayList)-> {
			
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

	public static void main(String[] args) {
		
	/*	Inscripcion inscripcion = new Inscripcion();
		inscripcion.inscribirCiudadano(30066008, new Fecha(12, 5, 1982), false, false);
		inscripcion.inscribirCiudadano(14553555, new Fecha(12, 4, 1914), false, false);
		inscripcion.inscribirCiudadano(11725668, new Fecha(2, 3, 1956), false, false);
		inscripcion.inscribirCiudadano(45223332, new Fecha(14, 3, 2000), false, false);
		inscripcion.inscribirCiudadano(45000055, new Fecha(18, 3, 2000), false, false);
	*/
		//System.out.println(inscripcion.toString());
	}
	
	public Map<Integer, ArrayList<Paciente>> obtenerListaEspera() {
		return listaEsperaConPrioridad;
	}
	
 


}
