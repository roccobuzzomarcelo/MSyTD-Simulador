
public class Main {
    public static void main(String[] args) {
        Simulacion resultadoA = Rifas.simularCampania(1000, 2000.0);
        Simulacion resultadoB = Rifas.simularCampania(2000, 1900.0);

        System.out.println("=== Escenario A ===");
        resultadoA.mostrar();

        System.out.println("\n=== Escenario B (con segundo equipo) ===");
        resultadoB.mostrar();

        if (resultadoB.gananciaTotal > resultadoA.gananciaTotal) {
            System.out.println("\n Conviene contratar el segundo equipo.");
        } else {
            System.out.println("\n No conviene contratar el segundo equipo.");
        }
    }
}