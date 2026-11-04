import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ServeurUDP {

    public static void main(String[] args) {

        Set<SocketAddress> clients = new HashSet<>();

        try {
            DatagramSocket serveur = new DatagramSocket(
                    new InetSocketAddress("0.0.0.0", 1234)
            );

            System.out.println("Serveur UDP demarre sur le port 1234...");
            System.out.println("En attente de messages...");

            while (true) {

                byte[] buffer = new byte[1024];

                DatagramPacket paquet = new DatagramPacket(
                        buffer,
                        buffer.length
                );

                serveur.receive(paquet);

                SocketAddress adresseClient = paquet.getSocketAddress();

                clients.add(adresseClient);

                String message = new String(
                        paquet.getData(),
                        0,
                        paquet.getLength()
                );

                System.out.println("Message recu de " + adresseClient);
                System.out.println(message);

                for (SocketAddress client : clients) {

                    if (!client.equals(adresseClient)) {

                        DatagramPacket diffusion = new DatagramPacket(
                                paquet.getData(),
                                paquet.getLength(),
                                client
                        );

                        serveur.send(diffusion);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}
