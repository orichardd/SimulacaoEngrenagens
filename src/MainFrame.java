import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class MainFrame extends BaseFrame {
    // Kinematic constant (must match the Manager's constant)
    private static final double TAMANHO_DENTE_PIXEL = 30;

    private Engrenagem engrenagem1;
    private Engrenagem engrenagem2;
    private GearPanel gearPanel; // Referência para o painel para controle do Timer

    // Variáveis para UI/Cálculos
    int dentes1;
    double rpm1;
    double torque1;
    int dentes2;
    double rpm2;
    double torque2;


    // Valores base para aplicação do slider (calculados pelo Manager/ConfigFrame)
    double rpmOriginal1;
    double torqueOriginal1;

    // Variáveis do Slider
    int rpmSliderValue = 0;
    int torqueSliderValue = 0;

    JLabel engrenagem1Label, dentes1Label, rpm1Label, torque1Label, engrenagem2Label, dentes2Label, rpm2Label, torque2Label;

    /** Calcula o raio de passo R = (N * P) / (2 * PI) */
    private double calcularRaio(int numeroDeDentes){
        return (numeroDeDentes * TAMANHO_DENTE_PIXEL) / (2 * Math.PI);
    }

    /** Calcula o offset angular necessário para o engrenamento correto (Meio Passo Angular). */
    private double calcularMeshingOffset(int dentes) {
        //calcula o offset, basicamente muda o angulo inicial da segunda engrenagem para garantir o engrenamento correto
        if(dentes2 % 2 != 0){
            return 0;
        }
        else{
            return 360.0 / (2.0 * dentes);
        }
    }

    public MainFrame(int dentes1, double rpm1, double torque1, int dentes2, double rpm2, double torque2) {
        super("Simulação de Engrenagens", 1300, 800, Color.WHITE);

        //adicionar o icone
        URL url = getClass().getResource("/icon/gearIcon.png");
        if (url == null) {
            System.err.println("ERRO: Ícone não encontrado no .jar!");
        } else {
            ImageIcon icon = new ImageIcon(url);
            setIconImage(icon.getImage());
        }

        // --- 0. Initialize Variables and Base Values ---
        this.dentes1 = dentes1;
        this.rpmOriginal1 = rpm1;
        this.torqueOriginal1 = torque1;
        this.dentes2 = dentes2;
        this.rpm1 = rpm1;
        this.torque1 = torque1;
        this.rpm2 = rpm2; // Valores iniciais calculados pelo Manager
        this.torque2 = torque2; // Valores iniciais calculados pelo Manager

        // --- 1. Initialize Gear Objects ---
        initializeEngrenagens();

        // --- 2. Add Gear Visualization Panel ---
        this.gearPanel = new GearPanel();
        adicionarComponente(gearPanel, 130, 0, 1170, 600);

        // --- 3. UI Setup ---
        JButton exitButton = new JButton("Sair da Simulação");
        exitButton.addActionListener(e -> sair());
        adicionarComponente(exitButton, 1065, 700, 200, 50);

        JButton restartButton = new JButton("Começar Novamente");
        restartButton.addActionListener(e -> reiniciar());
        adicionarComponente(restartButton, 1065, 635, 200, 50);

        mostrarInformacoes();
        adicionarSlideBar();

        // ** FIX CRUCIAL: Adiciona Listener para fechar a janela **
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sair(); // Garante que o timer seja parado
            }
        });

        setVisible(true);
    }

    private void initializeEngrenagens() {
        double raio1 = calcularRaio(dentes1);
        double raio2 = calcularRaio(dentes2);

        // Define Center Positions for visualization relative to the panel (950x500 area)
        int C1_X;
        if(dentes1 > 40){
            C1_X = (int)raio1 + 50;
        }
        else{
            C1_X = (int)raio1 + 350;
        }
        int C1_Y = 300;

        // Distância entre centros = R1 + R2
        int C2_X = C1_X + (int)(raio1 + raio2) + 15;
        int C2_Y = C1_Y;

        // Inicializa as engrenagens com as posições e valores iniciais
        engrenagem1 = new Engrenagem(dentes1, rpm1, torque1, raio1, C1_X, C1_Y, 0);

        // Offset para garantir o engrenamento visual (Gear 2)
        double initialOffset = calcularMeshingOffset(dentes2);
        engrenagem2 = new Engrenagem(dentes2, rpm2, torque2, raio2, C2_X, C2_Y, initialOffset);

        System.out.println("Raio1 = " + raio1 + ", Raio2 = " + raio2);
        System.out.println("C1_X = " + C1_X + ", C2_X = " + C2_X);

    }

    public void mostrarInformacoes(){
        // Inicializa os labels da classe
        engrenagem1Label = new JLabel("Engrenagem 1");
        engrenagem1Label.setFont(new Font("Arial", Font.BOLD, 30));
        dentes1Label = new JLabel("Dentes 1: " + dentes1);
        rpm1Label = new JLabel("RPM 1: " + String.format("%.2f", rpm1));
        torque1Label = new JLabel("Torque 1: " + String.format("%.2f", torque1));

        engrenagem2Label = new JLabel("Engrenagem 2");
        engrenagem2Label.setFont(new Font("Arial", Font.BOLD, 30));
        dentes2Label = new JLabel("Dentes 2: " + dentes2);
        rpm2Label = new JLabel("RPM 2: " + String.format("%.2f", rpm2));
        torque2Label = new JLabel("Torque 2: " + String.format("%.2f", torque2));

        // Engrenagem 1
        adicionarComponente(engrenagem1Label, 20, 20, 200, 25);
        adicionarComponente(dentes1Label, 20, 50, 200, 25);
        adicionarComponente(rpm1Label, 20, 70, 200, 25);
        adicionarComponente(torque1Label, 20, 90, 200, 25);

        // Engrenagem 2
        adicionarComponente(engrenagem2Label, 20, 160, 200, 25);
        adicionarComponente(dentes2Label, 20, 190, 200, 25);
        adicionarComponente(rpm2Label, 20, 210, 200, 25);
        adicionarComponente(torque2Label, 20, 230, 200, 25);

        // descrição sliders
        adicionarComponente(new JLabel("Controle de RPM"), 625, 685, 300, 25);
        adicionarComponente(new JLabel("Controle de Torque"), 75, 685, 250, 25);
    }


    public void adicionarSlideBar(){
        JSlider rpmSlider = new JSlider(-300, 300, 0);
        JSlider torqueSlider = new JSlider(-300, 300, 0);

        // RPM Slider listener
        rpmSlider.addChangeListener(e -> {
            this.rpmSliderValue = rpmSlider.getValue();
            atualizarInformacoes();
        });

        // Torque Slider listener
        torqueSlider.addChangeListener(e -> {
            this.torqueSliderValue = torqueSlider.getValue();
            atualizarInformacoes();
        });

        // Posicionamento
        adicionarComponente(rpmSlider, 300, 700, 750, 50);
        adicionarComponente(torqueSlider, 20, 700, 220, 50);
    }

    public void atualizarInformacoes(){
        // --- 1. Update RPM ---
        // Novo RPM 1 baseado no slider (controle percentual)
        rpm1 = rpmOriginal1 * (1.0 + (rpmSliderValue / 100.0));

        // RPM 2 recalculado usando a razão de transmissão (N1/N2)
        rpm2 = (dentes1 * rpm1) / dentes2;

        // --- 2. Update Torque ---
        // Novo Torque 1 baseado no slider
        torque1 = torqueOriginal1 * (1.0 + (torqueSliderValue / 100.0));

        // Torque 2 recalculado
        torque2 = (dentes1 * torque1) / dentes2;

        // --- 3. Update Engrenagem Speeds ---
        // Atualiza a velocidade na instância da Engrenagem, que recalcula degPerFrame
        engrenagem1.atualizarVelocidade(rpm1);
        engrenagem2.atualizarVelocidade(rpm2);


        // --- 4. Update UI Labels ---
        rpm1Label.setText("RPM 1: " + String.format("%.2f", rpm1));
        rpm2Label.setText("RPM 2: " + String.format("%.2f", rpm2));

        torque1Label.setText("Torque 1: " + String.format("%.2f", torque1));
        torque2Label.setText("Torque 2: " + String.format("%.2f", torque2));
    }

    /** FIX CRUCIAL: Para o timer de animação e fecha a janela. */
    public void sair(){
        if (gearPanel != null) {
            gearPanel.stopAnimation();
        }
        this.dispose();
    }

    public void reiniciar(){
        sair();
        // Assume que ConfigFrame está acessível globalmente ou será relançada
        new ConfigFrame();
    }

    // --- Gear Drawing and Animation Panel ---

    class GearPanel extends JPanel {
        private final Timer animationTimer;
        // Constants for simple tooth drawing (derived from TAMANHO_DENTE_PIXEL)
        private final int SHARED_TOOTH_WIDTH = (int)(TAMANHO_DENTE_PIXEL / 2);
        private final int SHARED_TOOTH_HEIGHT = (int)(TAMANHO_DENTE_PIXEL * 0.5);

        public GearPanel() {
            // Timer para animar (25ms = 40 FPS)
            this.animationTimer = new Timer(16, e -> {
                updateGearAngles();
                repaint();
            });
            this.animationTimer.start();
        }

        public void stopAnimation() {
            if (animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
            }
        }

        private void updateGearAngles() {
            // O sinal de degPerFrame é crucial: engrenagens externas giram em direções opostas
            engrenagem1.angulo = (engrenagem1.angulo + engrenagem1.grausPorFrame);
            engrenagem2.angulo = (engrenagem2.angulo - engrenagem2.grausPorFrame);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Desenha o fundo do painel
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // 1. Draw Gear 1 (Red - Driver)
            drawGear(g2d, engrenagem1, Color.DARK_GRAY); // Firebrick

            // 2. Draw Gear 2 (Blue - Driven)
            drawGear(g2d, engrenagem2, Color.DARK_GRAY); // DodgerBlue
        }

        /**
         * Desenha uma única engrenagem com dentes retangulares simples.
         */
        private void drawGear(Graphics2D g2d, Engrenagem engrenagem, Color color) {

            int radius = (int)engrenagem.raio;
            int teethCount = engrenagem.dentes;
            double rotationAngle = engrenagem.angulo;

            // 1. Desenha o corpo da Engrenagem (Círculo de Passo)
            g2d.setColor(color.darker().darker());
            g2d.fillOval(engrenagem.centerX - radius, engrenagem.centerY - radius, 2 * radius, 2 * radius);

            // 2. Define o formato do dente (simples retangular)
            Path2D tooth = new Path2D.Double();

            // O dente começa no raio de passo e se estende para fora
            tooth.moveTo(radius, -SHARED_TOOTH_WIDTH / 2.0);
            tooth.lineTo(radius + SHARED_TOOTH_HEIGHT, -SHARED_TOOTH_WIDTH / 2.0);
            tooth.lineTo(radius + SHARED_TOOTH_HEIGHT, SHARED_TOOTH_WIDTH / 2.0);
            tooth.lineTo(radius, SHARED_TOOTH_WIDTH / 2.0);
            tooth.closePath();

            // 3. Calcula o passo angular
            double angleStep = 360.0 / teethCount;

            // 4. Aplica transformação (Move para o centro, então Rotaciona)
            AffineTransform originalTransform = g2d.getTransform();
            g2d.translate(engrenagem.centerX, engrenagem.centerY);
            g2d.rotate(Math.toRadians(rotationAngle));

            g2d.setColor(color.brighter());
            g2d.setStroke(new BasicStroke(1.5f));

            // 5. Desenha todos os dentes
            for (int i = 0; i < teethCount; i++) {
                g2d.fill(tooth);
                g2d.draw(tooth);
                g2d.rotate(Math.toRadians(angleStep)); // Rota para a próxima posição
            }

            // 6. Restaura a transformação original
            g2d.setTransform(originalTransform);

            // Desenha o cubo central
            int hubRadius = radius / 4;
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillOval(engrenagem.centerX - hubRadius, engrenagem.centerY - hubRadius, 2 * hubRadius, 2 * hubRadius);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawOval(engrenagem.centerX - hubRadius, engrenagem.centerY - hubRadius, 2 * hubRadius, 2 * hubRadius);
            g2d.fillOval(engrenagem.centerX - 3, engrenagem.centerY - 3, 6, 6);
        }
    }
}