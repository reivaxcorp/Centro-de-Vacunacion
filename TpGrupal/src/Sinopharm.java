

public class Sinopharm extends Vacuna{
	private boolean disponible;

	public Sinopharm(String nombre, Fecha fechaIngreso, int temp) {
		super(nombre, fechaIngreso, temp);
		this.disponible = true;
	}
	
	
	@Override
	boolean getDisponible() {
		// TODO Auto-generated method stub
		return disponible;
	}

	@Override
	void setDisponible(boolean disponible) {
		// TODO Auto-generated method stub
		 this.disponible = disponible;
	}

}
