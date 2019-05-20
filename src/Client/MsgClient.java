package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MsgClient {
    private static JTextArea textArea;
    private static JTextField textField1;
    private static BufferedReader reader;// получает от сервера
    private static PrintWriter writer; // отправляет на сервер
    private static String log;

    public static void main(String[] args) throws IOException {
        log = JOptionPane.showInputDialog("Enter text");
        go();
    }


    private static void go() throws IOException {


        JFrame f = new JFrame("DayMessanger 1.0");
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        JPanel p = new JPanel();
        textArea = new JTextArea(15, 30);
        textArea.setLineWrap(true);// перенос
        textArea.setWrapStyleWord(true);// переносить слова полностью
        textArea.setEditable(false);// нельзя изменить
        JScrollPane sp = new JScrollPane(textArea);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textField1 = new JTextField(20);
        JButton b = new JButton("Отправить");


        b.addActionListener(new send());


        p.add(sp);
        p.add(textField1);
        p.add(b);
        setNet();

        Thread thread = new Thread(new Listener());
        thread.start();

        f.getContentPane().add(BorderLayout.CENTER, p);
        f.setSize(400, 340);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static class Listener implements Runnable {


        @Override
        public void run() {
            String msg;
            try {
                while ((msg = reader.readLine()) != null) {
                    textArea.append(msg + "\n");
                }
            } catch (Exception ex) {

            }
        }
    }

    private static class send implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String msg = log + ": " + textField1.getText();
            writer.println(msg);
            writer.flush();

            textField1.setText("");
            textField1.requestFocus();
        }
    }

    private static void setNet() throws IOException {
        Socket sock = new Socket("localhost", 5000);
        InputStreamReader is = new InputStreamReader(sock.getInputStream());// поток от сервера
        reader = new BufferedReader(is);
        writer = new PrintWriter(sock.getOutputStream());
    }
}
