
public class Astrazeneca extends VacunaSinVencimiento {

	public Astrazeneca(Fecha fechaIngreso) {
		super("Astrazeneca", fechaIngreso);
		super.ismayorDe60 = false;
		super.temp = 3;
		// ppp
	}

	public boolean pacientePuedeVacunarse(Paciente p) {
		if (Fecha.diferenciaAnios(Fecha.hoy(), p.getEdad()) >  60)
			return false;
		return true;
	}

	public static void main(String[] args) {
		Astrazeneca n = new Astrazeneca(new Fecha(11, 2, 2020));
		Paciente p = new Paciente(112312, new Fecha(11, 2, 1990), false, false);
		System.out.println(n.pacientePuedeVacunarse(p));
	}
}
