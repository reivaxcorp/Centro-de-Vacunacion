
public class Vacuna {

	private String nombre;
	private Fecha fechaIngreso;
	private boolean disponible;
	protected int temp;
	protected boolean ismayorDe60;
	protected boolean isVencida;
	
	
	public Vacuna(String nombre, Fecha fechaIngreso) {
		
		if(fechaIngreso.posterior(new Fecha(1, 3, 2020)) == false) {
			throw new IllegalArgumentException("Fecha incorrecta");	
		}
		if(CentroAlmacenamiento.compararN(nombre) == false)
			throw new IllegalArgumentException("Vacuna no catalogada");	
		
		this.nombre = nombre;
		this.fechaIngreso = fechaIngreso;
		this.disponible = true;
		this.isVencida = false;
	}

	public String getNombre() {
		return nombre;
	}
	

	public Fecha getFechaIngreso() {
		return fechaIngreso;
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
	
	// nuevo
	public void setVencida(boolean isVencida) {
		this.isVencida = isVencida;
	}
	
	public boolean isVencida() {
		return this.isVencida;
	}
	
	public boolean pacientePuedeVacunarse(Paciente p) {
		return false; 
	}
	
	@Override
	public String toString() {
		return  nombre + ", fecha de ingreso= " + fechaIngreso + ", temperatura= " + temp + " disponible= " + disponible +
				" exclusivo mayores 60= "+ ismayorDe60;
	}


	public static void main(String[] args) {

	}

}
