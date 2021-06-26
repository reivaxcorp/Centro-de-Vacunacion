
import java.util.ArrayList;

public class Paciente {
	private boolean enfermedadPreexistente;
	private boolean personalSalud;
	private int dni;
	private Fecha edad;
	private Fecha fechaTurno;
	private Vacuna vacunaAsignada;
	private int prioridad;
	private ArrayList<String> vacunasAplicables;

	protected boolean vacunado;

	public Paciente(int dni, Fecha edad, boolean enfermedadPreexistente, boolean personalSalud) {
		
			this.dni = dni;
			this.edad = edad;
			this.enfermedadPreexistente = enfermedadPreexistente;
			this.personalSalud = personalSalud;
			this.vacunado = false;
			this.vacunasAplicables = new ArrayList<String>();
			this.fechaTurno = null;
			this.vacunaAsignada = null;
		
		
	}

	public ArrayList<String> getVacunasAplicables() {
		return vacunasAplicables;
	}

	

	public int getDni() {
		return this.dni;
	}

	public Fecha getEdad() {
		return this.edad;
	}

	public boolean isEnfermedadPreexistente() {
		return this.enfermedadPreexistente;
	}

	public boolean isPersonalSalud() {
		return this.personalSalud;
	}

	public Vacuna obtenerVacunaDePaciente() {
		return null; // llenar
	}

	public Fecha getFechaTurno() {
		return this.fechaTurno;
	}

	public void setFechaTurno(Fecha t) {
		if (this.fechaTurno == null) {
			this.fechaTurno = t;
		}
	}

	public void setPrioridad(int prioridad) {	
		this.prioridad = prioridad;
	}

	public int getPrioridad() {
		return this.prioridad;
	}

	public Vacuna getVacunaAsignada() {
		return vacunaAsignada;
	}

	public void setVacunaAsignada(Vacuna vacunaAsignada) {
		this.vacunaAsignada = vacunaAsignada;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dni;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paciente other = (Paciente) obj;
		if (dni != other.dni)
			return false;
		return true;
	}
}
