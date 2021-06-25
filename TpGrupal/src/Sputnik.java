
public class Sputnik extends VacunaSinVencimiento {

	public Sputnik(Fecha fechaIngreso) {
		super("Sputnik", fechaIngreso);
		super.ismayorDe60 = true;
		super.temp = 3;
	}

}
