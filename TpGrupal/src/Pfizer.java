
public class Pfizer extends VacunaConVencimiento {

	public Pfizer(Fecha fechaIngreso) {
		super("Pfizer", fechaIngreso);
		super.ismayorDe60 = true;
		super.temp = -18;
	}

	public boolean pacientePuedeVacunarse(Paciente p) {
		if (Fecha.diferenciaAnios(Fecha.hoy(), p.getEdad()) >  60)
			return true;
		return false;
	}
}
