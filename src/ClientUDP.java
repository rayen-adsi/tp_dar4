import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.IOException;

public class ClientUDP {

    public static void main(String[] args) {

        try {
            DatagramSocket client = new DatagramSocket();

            InetAddress adresseServeur =
                    InetAddress.getByName("127.0.0.1");

            int portServeur = 1234;

            Scanner scanner = new Scanner(System.in);

            System.out.print("Entrez votre nom : ");
            String nom = scanner.nextLine();

            // THREAD RECEPTEUR
            Thread recepteur = new Thread(() -> {

                try {
                    while (true) {

                        byte[] buffer = new byte[1024];

                        DatagramPacket paquet =
                                new DatagramPacket(buffer, buffer.length);

                        client.receive(paquet);

                        String message = new String(
                                paquet.getData(),
                                0,
                                paquet.getLength()
                        );

                        System.out.println("\n" + message);
                        System.out.print("Message : ");
                    }

                } catch (IOException e) {
                    if (!client.isClosed()) {
                        System.out.println("Erreur reception : "
                                + e.getMessage());
                    }
                }
            });

            recepteur.start();

            // THREAD PRINCIPAL : ENVOI
            System.out.println("Bienvenue " + nom + " !");
            System.out.println("Tapez exit pour quitter.");

            while (true) {

                System.out.print("Message : ");
                String texte = scanner.nextLine();

                if (texte.equalsIgnoreCase("exit")) {
                    break;
                }

                String message = "[" + nom + "] : " + texte;

                byte[] donnees = message.getBytes();

                DatagramPacket paquet = new DatagramPacket(
                        donnees,
                        donnees.length,
                        adresseServeur,
                        portServeur
                );

                client.send(paquet);
            }

            client.close();
            scanner.close();

            System.out.println("Client arrete.");

        } catch (IOException e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}
