
public abstract class VacunaConVencimiento extends Vacuna {

 
	public VacunaConVencimiento(String nombre, Fecha fechaIngreso) {
		super(nombre, fechaIngreso);
		
	}

	@Override
	public String toString() {
		return "VacunaConVencimiento [temp=" + temp + ", ismayorDe60=" + ismayorDe60 + ", isVencida=" + isVencida + "]";
	}


}
