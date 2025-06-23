import java.util.Random;

public class Rifas {
    static final double PROB_NO_ATENDIDO = 0.6;
    static final double PROB_HOMBRE = 0.8;
    static final double PROB_VENTA_HOMBRE = 0.25;
    static final double PROB_VENTA_MUJER = 0.15;

    static final double[] PROB_RIFAS_HOMBRE = { 0.1, 0.4, 0.3, 0.2 }; // rifas 1 a 4
    static final double[] PROB_RIFAS_MUJER = { 0.6, 0.3, 0.1, 0.0 };

    /**
     * Simula una campaña de venta de rifas.
     *
     * @param visitasObjetivo Número de clientes objetivo (uno por hogar).
     * @param precioRifa      Precio de cada rifa.
     * @return Un objeto Simulacion con los resultados de la campaña.
     */
    public static Simulacion simularCampania(int visitasObjetivo, double precioRifa) {
        Random random = new Random();
        int visitasRealizadas = 0;
        int rifasVendidas = 0;

        for (int i = 0; i < visitasObjetivo; i++) {
            boolean atendido = false;

            // Primer intento
            visitasRealizadas++;
            if (simularAtencion(random)) {
                atendido = true;
            }

            // Segundo intento (otra semana) si no fue atendido
            if (!atendido) {
                visitasRealizadas++;
                if (simularAtencion(random)) {
                    atendido = true;
                }
            }

            // Si fue atendido en alguno de los dos intentos
            if (atendido) {
                boolean esHombre = random.nextDouble() < PROB_HOMBRE;
                boolean seVende = random.nextDouble() < (esHombre ? PROB_VENTA_HOMBRE : PROB_VENTA_MUJER);

                if (seVende) {
                    int rifas = simularCantidadRifas(random, esHombre);
                    rifasVendidas += rifas;
                }
            }
        }

        double ganancia = rifasVendidas * precioRifa;
        return new Simulacion(visitasRealizadas, rifasVendidas, ganancia);
    }

    private static boolean simularAtencion(Random random) {
        return random.nextDouble() >= PROB_NO_ATENDIDO;
    }

    private static int simularCantidadRifas(Random random, boolean esHombre) {
        double[] prob = esHombre ? PROB_RIFAS_HOMBRE : PROB_RIFAS_MUJER;
        double r = random.nextDouble();
        double acumulado = 0.0;

        for (int i = 0; i < prob.length; i++) {
            acumulado += prob[i];
            if (r <= acumulado) {
                return i + 1;
            }
        }

        return 1;
    }
}
