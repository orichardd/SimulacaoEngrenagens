import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame {
    public BaseFrame(String title, int width, int height, Color bgColor) {
        super(title);

        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(bgColor);
    }

    public void adicionarComponente(JComponent componente, int x, int y, int largura, int altura) {
        componente.setBounds(x, y, largura, altura);

        if (componente instanceof JButton) {
            JButton botao = (JButton) componente;
            botao.setBackground(Color.LIGHT_GRAY);
            botao.setOpaque(true);
            botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        else if (componente instanceof JSlider) {
            JSlider slider = (JSlider) componente;
            slider.setPaintTicks(false);       // nao desenha as marcas
            slider.setPaintLabels(false);      // nao mostra os números
            slider.setMajorTickSpacing(100);   // marca maior a cada 100
            slider.setMinorTickSpacing(10);    // marca menor a cada 10
            slider.setOpaque(false);
        }

        add(componente);
    }
}
