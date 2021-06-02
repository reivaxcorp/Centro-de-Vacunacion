

public class Moderna extends Vacuna {


	private boolean isVencida;
	
	public Moderna(String nombre, Fecha fechaIngreso, int temp, boolean isVencida) {
		super(nombre, fechaIngreso, temp);
		this.isVencida = isVencida;
	}

	
	protected boolean isVencida() {
		return isVencida;
	}

	protected void setVencida(boolean isVencida) {
		this.isVencida = isVencida;
	}
	


}
