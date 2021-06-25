
public abstract class VacunaConVencimiento extends Vacuna {

	private boolean isVencida;

	public VacunaConVencimiento(String nombre, Fecha fechaIngreso) {
		super(nombre, fechaIngreso);
		
	}

	public void setVencida(boolean isVencida) {
		this.isVencida = isVencida;
	}
	
	public boolean isVencida() {
		return this.isVencida;
	}

	@Override
	public String toString() {
		return super.toString() + " Vencida= " + isVencida;
	}

}
