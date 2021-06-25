
public class Sinopharm extends VacunaSinVencimiento {

	public Sinopharm(Fecha fechaIngreso) {
		super("Sinopharm", fechaIngreso);
		super.ismayorDe60 = false;
		super.temp = 3;
	}


}
