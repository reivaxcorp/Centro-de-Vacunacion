

public class Moderna extends Vacuna {


	private int mesesAlmacenada;
	private boolean isVencida;
	
	public Moderna(String nombre, Fecha fechaIngreso, int temp, 
			int mesesAlmacenada, boolean isVencida) {
		super(nombre, fechaIngreso, temp);
		this.mesesAlmacenada = mesesAlmacenada;
		this.isVencida = isVencida;
	}

	 
	protected void setMesAlmacenada(int mesesAlmacenada) {
		if(mesesAlmacenada > 2) {
			setVencida(true);
		}
		this.mesesAlmacenada = mesesAlmacenada;
	}

	
	protected int getMesesAlmacenada() {
		// TODO Auto-generated method stub
		return mesesAlmacenada;
	}


	protected boolean isVencida() {
		return isVencida;
	}

	protected void setVencida(boolean isVencida) {
		this.isVencida = isVencida;
	}
	


}
