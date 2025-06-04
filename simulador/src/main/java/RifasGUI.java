import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class RifasGUI extends JFrame {
    private final JTextArea infoEscenariosArea;
    private final JTextArea resultadoArea;
    private final JLabel recomendacionLabel;
    private final ChartPanel chartPanel;
    private final JButton btnSimular;

    public RifasGUI() {
        setTitle("Simulación Campaña de Rifas");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Texto con datos de los escenarios
        infoEscenariosArea = new JTextArea(4, 50);
        infoEscenariosArea.setEditable(false);
        infoEscenariosArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        infoEscenariosArea.setText("""
                                   Datos de la Simulaci\u00f3n:
                                   Escenario A: 1000 visitas, $2000 por rifa
                                   Escenario B: 2000 visitas, $1900 por rifa
                                   """);

        resultadoArea = new JTextArea(6, 50);
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        recomendacionLabel = new JLabel(" ");
        recomendacionLabel.setFont(new Font("Arial", Font.BOLD, 14));

        btnSimular = new JButton("Iniciar Simulación");

        // Panel para gráfico vacío al principio
        chartPanel = new ChartPanel(null);
        chartPanel.setPreferredSize(new Dimension(550, 250));

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JScrollPane(infoEscenariosArea), BorderLayout.NORTH);
        topPanel.add(btnSimular, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(resultadoArea), BorderLayout.NORTH);
        centerPanel.add(recomendacionLabel, BorderLayout.CENTER);
        centerPanel.add(chartPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout(10, 10));
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Acción botón
        btnSimular.addActionListener((ActionEvent e) -> {
            simularYMostrar();
        });
    }

    private void simularYMostrar() {
        Simulacion simA = Rifas.simularCampania(1000, 2000);
        Simulacion simB = Rifas.simularCampania(2000, 1900);

        String textoResultado = String.format("""
                                              Resultados:
                                              Escenario A:
                                                Visitas: %d
                                                Rifas vendidas: %d
                                                Ganancia: $%.2f
                                              
                                              Escenario B:
                                                Visitas: %d
                                                Rifas vendidas: %d
                                                Ganancia: $%.2f
                                              """,
                simA.visitas, simA.rifas, simA.gananciaTotal,
                simB.visitas, simB.rifas, simB.gananciaTotal);

        resultadoArea.setText(textoResultado);

        String recomendacion = simB.gananciaTotal > simA.gananciaTotal
                ? "Recomendación: Conviene el segundo equipo."
                : "Recomendación: No conviene el segundo equipo.";
        recomendacionLabel.setText(recomendacion);

        // Crear dataset para gráfico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(simA.gananciaTotal, "Ganancia", "Escenario A");
        dataset.addValue(simB.gananciaTotal, "Ganancia", "Escenario B");

        // Crear gráfico de barras
        JFreeChart barChart = ChartFactory.createBarChart(
                "Comparación de Ganancias",
                "Escenario",
                "Ganancia ($)",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.BLACK);

        chartPanel.setChart(barChart);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RifasGUI().setVisible(true);
        });
    }
}
