package org.wora.ui;
import org.wora.entity.Client;
import org.wora.repository.ClientRepository;
import org.wora.service.ClientService;
import org.wora.service.serviceImpl.ClientServiceImpl;
import java.util.Optional;
import java.util.Scanner;

public class ClientUI {
    private ClientService clientService;

    public ClientUI(ClientRepository clientRepository) {
        this.clientService = new ClientServiceImpl(clientRepository);
    }

    public Client handleClientSelection(Scanner scanner) {
        System.out.println("--- Recherche de client ---");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");

        int option = scanner.nextInt();
        scanner.nextLine();

        if (option == 1) {
            return findExistingClient(scanner);
        } else if (option == 2) {
            return createNewClient(scanner);
        }
        return null;
    }

    private Client findExistingClient(Scanner scanner) {
        System.out.print("Entrez le nom du client : ");
        String clientName = scanner.nextLine();
        Optional<Client> clientOpt = clientService.findClientByName(clientName);

        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            System.out.println("Client trouvé :");
            System.out.println("Nom: " + client.getName());
            System.out.println("Adresse : " + client.getAdress());
            System.out.println("Numéro de téléphone : " + client.getNumberPhone());

            System.out.print("Souhaitez-vous continuer avec ce client ? (y/n) : ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("y")) {
                return client;
            }
        } else {
            System.out.println("Client non trouvé.");
        }
        return null;
    }

    private Client createNewClient(Scanner scanner) {
        System.out.println("--- Ajout d'un nouveau client ---");

        System.out.print("Nom du client : ");
        String name = scanner.nextLine();
        System.out.print("Adresse du client : ");
        String address = scanner.nextLine();
        System.out.print("Numéro de téléphone : ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Le client est-il un professionnel ? (oui/non) : ");
        boolean isProfessionnel = scanner.nextLine().equalsIgnoreCase("oui");

        Client client = new Client();
        client.setName(name);
        client.setAdress(address);
        client.setNumberPhone(phoneNumber);
        client.setProfessionel(isProfessionnel);

        clientService.addClient(client);
        return client;
    }
}
