import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ConfigFrame extends BaseFrame{

    int dentes1;
    double rpm1;
    double torque1;
    int dentes2;

    Manager manager = new Manager();

    public ConfigFrame(){
        super("Configuração da Simulação", 400, 500, Color.WHITE);

        //adicionar os inputs de configuraçao
        JLabel textoDentes1 = new JLabel("Dentes Engrenagem 1:");
        adicionarComponente(textoDentes1, 25, 50, 200, 30);
        JTextField numDentes1Field = new JTextField();
        adicionarComponente(numDentes1Field, 250, 50, 25, 25);

        JLabel textoRPM1 = new JLabel("RPM Engrenagem 1:");
        adicionarComponente(textoRPM1, 25, 80, 200, 25);
        JTextField numRPM1Field = new JTextField();
        adicionarComponente(numRPM1Field, 250, 80, 25, 25);

        JLabel textoTorque1 = new JLabel("Torque Engrenagem 1:");
        adicionarComponente(textoTorque1, 25, 110, 200, 25);
        JTextField numTorque1Field = new JTextField();
        adicionarComponente(numTorque1Field, 250, 110, 25, 25);

        JLabel textoDentes2 = new JLabel("Dentes Engrenagem 2:");
        adicionarComponente(textoDentes2, 25, 160, 200, 25);
        JTextField numDentes2Field = new JTextField();
        adicionarComponente(numDentes2Field, 250, 160, 25, 25);


        //configuraçoes do botao
        JButton startButton = new JButton("Iniciar Simulação");
        adicionarComponente(startButton, 25, 400, 150, 40);

        //detectar enter para começar a simulação
        getRootPane().setDefaultButton(startButton);

        startButton.addActionListener(e -> {
            //verificar se os valores sao validos
            verificarValores(numDentes1Field.getText(), Integer.class);
            dentes1 = Integer.parseInt(numDentes1Field.getText());

            verificarValores(numRPM1Field.getText(), Double.class);
            rpm1 = Double.parseDouble(numRPM1Field.getText());

            verificarValores(numTorque1Field.getText(), Double.class);
            torque1 = Double.parseDouble(numTorque1Field.getText());

            verificarValores(numDentes2Field.getText(), Integer.class);
            dentes2 = Integer.parseInt(numDentes2Field.getText());

            if(dentes1 < 8 || dentes2 < 8){
                JOptionPane.showMessageDialog(this, "O número de dentes de cada engrenagem deve ser no mínimo 8.");
                // fecha o frame
                dispose();
                // abre novamente
                new ConfigFrame();
                return;
            }
            else if(dentes1 > 60 || dentes2 > 60){
                JOptionPane.showMessageDialog(this, "O número de dentes de cada engrenagem deve ser no máximo 60.");
                // fecha o frame
                dispose();
                // abre novamente
                new ConfigFrame();
                return;
            }


            iniciarSimulacao();
        });

        //botao para mostrar os créditos
        JButton creditosButton = new JButton("...");
        creditosButton.setVisible(false);
        adicionarComponente(creditosButton, 300, 400, 30, 30);
        creditosButton.addActionListener(ev -> abrirCreditosFrame());


        add(startButton);

        this.setVisible(true);
    }


    public void iniciarSimulacao() {
        //JOptionPane.showMessageDialog(this,"Dentes 1 %d, RPM 1 %.2f, Torque 1 %.2f, Dentes 2 %d".formatted(dentes1, rpm1, torque1, dentes2));
        this.dispose();
        manager.setEngrenagens(dentes1, rpm1, torque1, dentes2);
        manager.criarMainFrame();
    }

    void verificarValores(String valor, Class<?> tipo){
        boolean invalido = false;

        if(valor.isEmpty()){
            invalido = true;
        }
        else if(tipo == Integer.class){
            try {
                Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                invalido = true;
            }
        } else if (tipo == Double.class){
            try {
                Double.parseDouble(valor);
            } catch (NumberFormatException e) {
                invalido = true;
            }
        } else {
            invalido = true;
        }

        if(invalido){
            JOptionPane.showMessageDialog(this, "Algum valor inserido é inválido. Por favor tente novamente.");
            // fecha o frame
            dispose();
            // abre novamente
            new ConfigFrame();
        }
    }

    void abrirCreditosFrame(){
        new Creditos();
    }

}
