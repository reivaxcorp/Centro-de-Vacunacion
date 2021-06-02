

public abstract class Vacuna{
	
	
	private String nombre;
	private Fecha fechaIngreso;
	private int temp;
	 
	public Vacuna(String nombre, Fecha fechaIngreso, int temp) {
		this.nombre = nombre;
		this.fechaIngreso = fechaIngreso; 
		this.temp = temp;
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

	@Override
	public String toString() {
		return "Vacuna [nombre=" + nombre + ", fechaIngreso=" + fechaIngreso + ", temp=" + temp + "]";
	}
	

	
}
