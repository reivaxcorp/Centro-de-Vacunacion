
public class Vacuna {

	private String nombre;
	private Fecha fechaIngreso;
	protected int temp;
	private boolean disponible;
	protected boolean ismayorDe60;

	public Vacuna(String nombre, Fecha fechaIngreso) {
		this.nombre = nombre;
		this.fechaIngreso = fechaIngreso;
		this.disponible = true;
	}

	public String getNombre() {
		return nombre;
	}
	

	public Fecha getFechaIngreso() {
		return fechaIngreso;
	}
	
	public boolean getEsParaMayor() {
		return ismayorDe60;
	}

	public int getTemp() {
		return temp;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	public boolean pacientePuedeVacunarse(Paciente p) {
		if(Fecha.diferenciaAnios(Fecha.hoy(), p.getEdad()) >= 60 && ismayorDe60)
			return true;
		return false; 
	}
	/*a una vacuna le paso un paciente, si el paciente es mayor de 60 y la vacuna
	* es exclusiva para mayores de 60, podra vacunarse, si es menor dara falso, 
	* puede vacunarse pero con otra
	*/
	@Override
	public String toString() {
		return  nombre + ", fecha de ingreso= " + fechaIngreso + ", temperatura= " + temp + " disponible= " + disponible +
				" exclusivo mayores 60= "+ ismayorDe60;
	}
	

	private boolean compararN(String nombre) {
		String[] nombres = { "Pfizer", "Moderna", "Sputnik", "Sinopharm", "AstraZeneca" };
		boolean algunNombre = false;
		for (int i = 0; i < nombres.length; i++) {
			algunNombre = algunNombre || nombre.equals(nombres[i]);
		}
		return algunNombre;
	}

	public static void main(String[] args) {

	}

}
