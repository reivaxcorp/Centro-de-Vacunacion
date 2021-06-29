

public class Paciente {
	private boolean enfermedadPreexistente;
	private boolean personalSalud;
	private int dni;
	private Fecha edad;
	private Fecha fechaTurno;
	private Vacuna vacunaAsignada;
	private int prioridad;

	protected boolean vacunado;

	public Paciente(int dni, Fecha edad, boolean enfermedadPreexistente, boolean personalSalud) {
		
		
		if ((dni > 2000000 && dni < 50000000) == false)
			throw new IllegalArgumentException("DNI incorrecto");
		
		if((edad.anterior(Fecha.hoy()) && edad.posterior(new Fecha(1, 1, 1911))) == false) {
			throw new IllegalArgumentException("Edad incorrecta");	
		}
		
			this.dni = dni;
			this.edad = edad;
			this.enfermedadPreexistente = enfermedadPreexistente;
			this.personalSalud = personalSalud;
			this.vacunado = false;
			this.fechaTurno = null;
			this.vacunaAsignada = null;
		
		
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
	public String toString() {
		return "Paciente [enfermedadPreexistente=" + enfermedadPreexistente + ", personalSalud=" + personalSalud
				+ ", dni=" + dni + ", edad=" + edad + ", fechaTurno=" + fechaTurno + ", vacunaAsignada="
				+ vacunaAsignada + ", prioridad=" + prioridad + ", vacunado=" + vacunado + "]";
	}	

	
}
