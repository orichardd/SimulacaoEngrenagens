import java.awt.*;

public class Manager {
    int dentes1;
    double rpm1;
    double torque1;
    int dentes2;
    double rpm2;
    double torque2;
    double tamanhoDente = 10;

    Engrenagem engrenagem1;
    Engrenagem engrenagem2;

    public void setEngrenagens(int dentes1, double rpm1, double torque1, int dentes2){
        this.dentes1 = dentes1;
        this.rpm1 = rpm1;
        this.torque1 = torque1;

        this.dentes2 = dentes2;

        // Calcular RPM e torque da segunda engrenagem
        this.rpm2 = (dentes1 * rpm1) / dentes2;
        this.torque2 = (dentes2 * torque1) / dentes1;

        // 2. Inicialização dos objetos (as coordenadas serão ajustadas no MainFrameWithGears)
        double raio1 = calcularRaio(dentes1);
        double raio2 = calcularRaio(dentes2);

        engrenagem1 = new Engrenagem(dentes1, rpm1, torque1, raio1, 0, 0, 0);
        engrenagem2 = new Engrenagem(dentes2, rpm2, torque2, raio2, 0, 0, 0);
    }

    public double calcularRaio(int numeroDeDentes){
        //com 10 pixeis de tamanho por dente, calcula o raio da engrenagem usando: raio = (diametro / 2) = (numeroDeDentes * tamanhoDente) / (2 * pi)
        return (numeroDeDentes * tamanhoDente) / (2 * Math.PI);
    }

    public void criarMainFrame(){
        MainFrame mainFrame = new MainFrame(dentes1, rpm1, torque1, dentes2, rpm2, torque2);
    }
}
