
public class Sputnik extends VacunaSinVencimiento {

	public Sputnik(Fecha fechaIngreso) {
		super("Sputnik", fechaIngreso);
		super.ismayorDe60 = true;
		super.temp = 3;
	}
	public boolean pacientePuedeVacunarse(Paciente p) {
		if (Fecha.diferenciaAnios(Fecha.hoy(), p.getEdad()) >= 60)
			return true;
		return false;
	}
}
