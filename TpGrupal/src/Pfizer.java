

public class Pfizer extends Vacuna {

	private boolean isVencida;
	

	public Pfizer(String nombre, Fecha fechaIngreso, int temp, boolean isVencida) {
		super(nombre, fechaIngreso, temp);
		this.isVencida = isVencida;
	}

	

	protected void setVencida(boolean isVencida) {
		// TODO Auto-generated method stub
		this.isVencida = isVencida;
	}
	
	protected boolean isVencida() {
		// TODO Auto-generated method stub
		return this.isVencida;
	}
	

}
