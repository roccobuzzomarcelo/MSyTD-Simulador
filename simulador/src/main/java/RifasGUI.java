import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class RifasGUI extends JFrame {
    private final JTextArea infoEscenariosArea;
    private final JTextArea resultadoArea;
    private final JLabel recomendacionLabel;
    private final ChartPanel chartPanel;
    private final JButton btnSimular;
    private final JButton btnExportar;

    private final JTextField visitasAField;
    private final JTextField precioAField;
    private final JTextField visitasBField;
    private final JTextField precioBField;

    public RifasGUI() {
        FlatLightLaf.setup();

        setTitle("Simulación de Campaña de Rifas");
        setSize(512, 812);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setIconImage(new ImageIcon(getClass().getResource("/rifa.png")).getImage());

        Font fuenteBase = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteNegrita = new Font("Segoe UI", Font.BOLD, 15);

        Color azulSuave = new Color(70, 130, 180);
        Color grisClaro = new Color(245, 245, 245);

        visitasAField = new JTextField("1000", 6);
        precioAField = new JTextField("2000", 6);
        visitasBField = new JTextField("2000", 6);
        precioBField = new JTextField("1900", 6);

        infoEscenariosArea = new JTextArea(3, 50);
        infoEscenariosArea.setEditable(false);
        infoEscenariosArea.setFont(fuenteBase);
        infoEscenariosArea.setBackground(grisClaro);
        infoEscenariosArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoEscenariosArea.setText("""
                Datos de la Simulación:
                    - Escenario A: 1000 visitas, $2000 por rifa
                    - Escenario B: 2000 visitas, $1900 por rifa""");

        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setFont(fuenteBase);
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);
        resultadoArea.setBackground(Color.WHITE);
        resultadoArea.setBorder(BorderFactory.createTitledBorder("\uD83D\uDCC8 Resultados"));

        recomendacionLabel = new JLabel(" ");
        recomendacionLabel.setFont(fuenteNegrita);
        recomendacionLabel.setForeground(azulSuave);
        recomendacionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        btnSimular = new JButton(" Iniciar Simulación");
        btnSimular.setFont(fuenteNegrita);
        btnSimular.setIcon(new FlatSVGIcon("icons/play.svg", 16, 16));
        btnSimular.setBackground(azulSuave);
        btnSimular.setForeground(Color.WHITE);
        btnSimular.setFocusPainted(false);
        btnSimular.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnExportar = new JButton(" Exportar PNG");
        btnExportar.setFont(fuenteNegrita);
        btnExportar.setIcon(new FlatSVGIcon("icons/download.svg", 16, 16));
        btnExportar.setBackground(azulSuave);
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        chartPanel = new ChartPanel(null);
        chartPanel.setPreferredSize(new Dimension(600, 260));
        chartPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(grisClaro);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        topPanel.add(infoEscenariosArea, BorderLayout.CENTER);
        topPanel.add(btnSimular, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(resultadoArea, BorderLayout.NORTH);
        centerPanel.add(recomendacionLabel, BorderLayout.CENTER);
        centerPanel.add(chartPanel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(btnExportar);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnSimular.addActionListener((ActionEvent e) -> {
            btnSimular.setEnabled(false);
            btnSimular.setText("Simulando...");

            SwingUtilities.invokeLater(() -> {
                simularYMostrar();
                btnSimular.setText("Iniciar Simulación");
                btnSimular.setEnabled(true);
            });
        });

        btnExportar.addActionListener(e -> exportarGrafico());
    }

    private void simularYMostrar() {
        int visitasA = Integer.parseInt(visitasAField.getText());
        int precioA = Integer.parseInt(precioAField.getText());
        int visitasB = Integer.parseInt(visitasBField.getText());
        int precioB = Integer.parseInt(precioBField.getText());

        Simulacion simA = Rifas.simularCampania(visitasA, precioA);
        Simulacion simB = Rifas.simularCampania(visitasB, precioB);

        String textoResultado = String.format("""
                Escenario A:
                    - Visitas: %d
                    - Rifas vendidas: %d
                    - Ganancia: $%.2f

                Escenario B:
                    - Visitas: %d
                    - Rifas vendidas: %d
                    - Ganancia: $%.2f""",
                simA.visitas, simA.rifas, simA.gananciaTotal,
                simB.visitas, simB.rifas, simB.gananciaTotal);

        double diferencia = Math.abs(simA.gananciaTotal - simB.gananciaTotal);
        textoResultado += String.format("\n\nDiferencia de ganancia: $%.2f", diferencia);

        resultadoArea.setText(textoResultado);
        resultadoArea.setRows(textoResultado.split("\n").length);
        recomendacionLabel.setText(simB.gananciaTotal > simA.gananciaTotal
                ? "Recomendación: Conviene el segundo equipo."
                : "Recomendación: No conviene el segundo equipo.");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(simA.gananciaTotal, "Ganancia", "Escenario A");
        dataset.addValue(simB.gananciaTotal, "Ganancia", "Escenario B");

        JFreeChart barChart = ChartFactory.createBarChart(
                "Comparación de Ganancias",
                "Escenario",
                "Ganancia ($)",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setBackgroundPaint(Color.WHITE);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(100, 160, 255));

        chartPanel.setChart(barChart);
    }

    private void exportarGrafico() {
        try {
            ChartUtils.saveChartAsPNG(new File("grafico.png"), chartPanel.getChart(), 600, 400);
            JOptionPane.showMessageDialog(this, "Gráfico exportado como 'ganancias.png'");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar gráfico");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RifasGUI().setVisible(true));
    }
}
