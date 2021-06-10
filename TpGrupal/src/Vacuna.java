

public abstract class Vacuna{
	
	
	private String nombre;
	private Fecha fechaIngreso;
	private int temp;
	private boolean disponible; 
	
	public Vacuna(String nombre, Fecha fechaIngreso, int temp) {
		this.nombre = nombre;
		this.fechaIngreso = fechaIngreso; 
		this.temp = temp;
		this.disponible = true;
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
	
	@Override
	public String toString() {
		return "Vacuna [nombre=" + nombre + ", fechaIngreso=" + fechaIngreso + ", temp=" + temp + "]";
	}
	

	
}
