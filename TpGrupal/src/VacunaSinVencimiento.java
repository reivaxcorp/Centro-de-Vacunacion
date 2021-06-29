
public abstract class VacunaSinVencimiento extends Vacuna {


	public VacunaSinVencimiento(String nombre, Fecha fechaIngreso) {
		super(nombre, fechaIngreso);
	}
 
	
	@Override
	public String toString() {
		return "VacunaSinVencimiento [temp=" + temp + ", ismayorDe60=" + ismayorDe60 + ", isVencida=" + isVencida + "]";
	}

	
}
