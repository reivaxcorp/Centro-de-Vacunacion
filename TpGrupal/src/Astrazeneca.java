
public class Astrazeneca extends VacunaSinVencimiento {

	public Astrazeneca(Fecha fechaIngreso) {
		super("Astrazeneca", fechaIngreso);
		super.ismayorDe60 = false;
		super.temp = 3;
	}

}
