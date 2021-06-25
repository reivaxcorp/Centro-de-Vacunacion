
public class Pfizer extends VacunaConVencimiento {

	public Pfizer(Fecha fechaIngreso) {
		super("Pfizer", fechaIngreso);
		super.ismayorDe60 = true;
		super.temp = -18;
	}

	
}
