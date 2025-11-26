// Assuming this is the class referenced in your setEngrenagens method
public class Engrenagem {
    public final int dentes;    // Teeth count (N)
    public final double raio;   // Radius (R)
    public double rpm;    // Revolutions per minute
    public final int centerX;   // X position of the center
    public final int centerY;   // Y position of the center

    // Rotation speed in degrees per frame
    public double grausPorFrame;

    // Current rotation angle
    public double angulo;

    int FPS = 60;

    // Constructor to initialize properties
    public Engrenagem(int dentes, double rpm, double torque, double raio, int centerX, int centerY, double angulo) {
        this.dentes = dentes;
        this.raio = raio;
        this.rpm = rpm;
        this.centerX = centerX;
        this.centerY = centerY;
        this.angulo = angulo;

        //conversao de rpm para graus por frame considerando 60 fps
        this.grausPorFrame = (rpm * 360.0) / 60.0 / FPS;
    }

    public void atualizarVelocidade(double novoRPM) {
        this.rpm = novoRPM;
        this.grausPorFrame = (novoRPM * 360.0) / 60.0 / FPS; //a cada atualização da animação calcula os graus por frame
    }
}