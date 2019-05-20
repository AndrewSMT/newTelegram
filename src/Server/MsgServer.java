package Server;

import Database.Connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class MsgServer {
    private static ArrayList streams;
    private static Connect connect = new Connect();
    private static PrintWriter writer;


    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        go();
    }

    private static void go() throws IOException, SQLException, ClassNotFoundException {
        connect.openCon();
        streams = new ArrayList<PrintWriter>();
        ServerSocket serverSocket = new ServerSocket(5000);
        while (true) {
            Socket sock = serverSocket.accept();
            System.out.println("Client join");
             writer = new PrintWriter(sock.getOutputStream());
            streams.add(writer);
            sendHistory();
            Thread t = new Thread(new Listener(sock));
            t.start();
        }
    }
    private static void sendHistory() throws SQLException {
        String SQL = "SELECT msg from savechat";
        ResultSet rs = connect.getSt().executeQuery(SQL);
        while (rs.next()) {
            writer.println(rs.getString("msg"));
        }
        writer.flush();

    }
    private static void tellEveryone(String msg) throws SQLException, ClassNotFoundException {//рассылка всем пользователям в чате
        Iterator<PrintWriter> it = streams.iterator();
        int x = msg.indexOf(":");
        String login = msg.substring(0, x);
        save(login, msg);
        while (it.hasNext()) {
            PrintWriter writer = it.next();
            writer.println(msg);
            writer.flush();
        }
    }
    private static void save(String login, String msg) throws SQLException, ClassNotFoundException {
        String SQL = "INSERT INTO savechat (login, msg, Clientmsg) VALUES ('" +login+ "','" +msg+ "','"+login+"');";
        connect.getSt().executeUpdate(SQL);
    }
    private static class Listener implements Runnable { //чтение входящих сообщений сервером

        BufferedReader reader;

        Listener(Socket sock) throws IOException {
            InputStreamReader is = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(is);
        }

        @Override
        public void run() {
            try {
                String msg;
                while ((msg = reader.readLine()) != null) {
                    System.out.println(msg);
                    tellEveryone(msg);
                }
            } catch (Exception ex) {

            }

        }
    }
}
