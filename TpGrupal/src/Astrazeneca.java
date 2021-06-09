

public class Astrazeneca extends Vacuna{
	
	

	public Astrazeneca(String nombre, Fecha fechaIngreso, int temp) {
		super(nombre, fechaIngreso, temp);
	}

	@Override
	public String toString() {
		return "Astrazeneca [getNombre()=" + getNombre() + ", getFechaIngreso()=" + getFechaIngreso() + ", getTemp()="
				+ getTemp() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}
}
