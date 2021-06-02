

public class Sinopharm extends Vacuna{



	public Sinopharm(String nombre, Fecha fechaIngreso, int temp) {
		super(nombre, fechaIngreso, temp);
	}
	
	@Override
	public String toString() {
		return "Sinopharm [getNombre()=" + getNombre() + ", getFechaIngreso()=" + getFechaIngreso() + ", getTemp()="
				+ getTemp() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}

}
