public class Simulacion {
    int visitas;
    int rifas;
    double gananciaTotal;

    Simulacion(int visitas, int rifas, double gananciaTotal) {
        this.visitas = visitas;
        this.rifas = rifas;
        this.gananciaTotal = gananciaTotal;
    }

    void mostrar() {
        System.out.println("Visitas realizadas: " + visitas);
        System.out.println("Rifas vendidas: " + rifas);
        System.out.println("Ganancia total: $" + gananciaTotal);
    }
}
