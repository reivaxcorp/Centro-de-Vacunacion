
public class Sinopharm extends VacunaSinVencimiento {

	public Sinopharm(Fecha fechaIngreso) {
		super("Sinopharm", fechaIngreso);
		super.ismayorDe60 = false;
		super.temp = 3;
	}

	public boolean pacientePuedeVacunarse(Paciente p) {
		if (Fecha.diferenciaAnios(Fecha.hoy(), p.getEdad()) >  60)
			return false;
		return true;
	}
}
