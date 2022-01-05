import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public client() {

        try {

            System.out.println("sending request to server ...");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void startReading() {

        // thread - read kore data rakhbe
        Runnable r1 = () -> {

            System.out.println("reading started ...");

            try {

                while (true) {

                    String msg = br.readLine();

                    if (msg.equals("quit")) {

                        System.out.println("server terminated the chat");
                        socket.close();
                        break;
                    }

                    System.out.println("server : " + msg);
                }

            } catch (Exception e) {

                //e.printStackTrace();
                System.out.println("connection closed");
            }
        };

        new Thread(r1).start();
    }

    public void startWriting() {

        // thread - sey data niye server k send korbe
        Runnable r2 = () -> {

            System.out.println("writing started ...");

            try {

                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){

                        socket.close();
                        break;
                    }
                }

            } catch (Exception e) {

                //e.printStackTrace();
                System.out.println("connection closed");
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {

        System.out.println("client running ...");
        new client();
    }
}
