

public class Pfizer extends Moderna {

	private boolean disponible;

	public Pfizer(String nombre, Fecha fechaIngreso, int temp, int mesesAlmacenada,
			boolean isVencida) {
		super(nombre, fechaIngreso, temp, mesesAlmacenada, isVencida);
		this.disponible = true;
	}


	@Override
	protected void setMesAlmacenada(int mesesAlmacenada) {
		if(mesesAlmacenada >= 1 ) {
			super.setVencida(true);
			setDisponible(false);
		}
		else
			super.mesesAlmacenada = mesesAlmacenada;
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
