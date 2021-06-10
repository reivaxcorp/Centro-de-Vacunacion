

public class Sinopharm extends Vacuna{



	public Sinopharm(String nombre, Fecha fechaIngreso, int temp) {
		super(nombre, fechaIngreso, temp);
	}
	
	
	@Override
	public void setDisponible(boolean disponible) {
		// TODO Auto-generated method stub
		super.setDisponible(disponible);
	}
	
	@Override
	public boolean isDisponible() {
		// TODO Auto-generated method stub
		return super.isDisponible();
	}
	
	
	@Override
	public String toString() {
		return "Sinopharm [getNombre()=" + getNombre() + ", getFechaIngreso()=" + getFechaIngreso() + ", getTemp()="
				+ getTemp() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}

}
