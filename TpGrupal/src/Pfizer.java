

public class Pfizer extends Vacuna {

	private int mesesAlmacenada;
	private boolean isVencida;
	

	public Pfizer(String nombre, Fecha fechaIngreso, int temp, int mesesAlmacenada,
			boolean isVencida) {
		super(nombre, fechaIngreso, temp);
		this.mesesAlmacenada = mesesAlmacenada;
		this.isVencida = isVencida;
	}


	protected void setMesAlmacenada(int mesesAlmacenada) {
		if(mesesAlmacenada > 1) {
			setVencida(true);
		}
		this.mesesAlmacenada = mesesAlmacenada;
	}
	
	protected int getMesesAlmacenada() {
		// TODO Auto-generated method stub
		return mesesAlmacenada;
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
