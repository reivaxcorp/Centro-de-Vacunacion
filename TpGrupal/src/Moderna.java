
public class Moderna extends VacunaConVencimiento {

	public Moderna(Fecha fechaIngreso) {
		super("Moderna", fechaIngreso);
		super.ismayorDe60 = false;
		super.temp = -18;
	}

	public boolean pacientePuedeVacunarse(Paciente p) {
		if (Fecha.diferenciaAnios(Fecha.hoy(), p.getEdad()) >= 60)
			return false;
		return true;
	}

}
