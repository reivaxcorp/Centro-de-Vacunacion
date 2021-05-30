

public class Moderna extends Vacuna {


	private int mesesAlmacenada;
	private boolean isVencida;
	private boolean disponible;
	
	public Moderna(String nombre, Fecha fechaIngreso, int temp, 
			int mesesAlmacenada, boolean isVencida) {
		super(nombre, fechaIngreso, temp);
		this.mesesAlmacenada = mesesAlmacenada;
		this.isVencida = isVencida;
		this.disponible = true;
	}

	 
	protected void setMesAlmacenada(int mesesAlmacenada) {
		if(getMesesAlmacenada() >=2) {
			setVencida(true);
			setDisponible(false);
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
