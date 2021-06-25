
public class Moderna extends VacunaConVencimiento {

	public Moderna(Fecha fechaIngreso) {
		super("Moderna", fechaIngreso);
		super.ismayorDe60 = false;
		super.temp = -18;
	}

	

}
