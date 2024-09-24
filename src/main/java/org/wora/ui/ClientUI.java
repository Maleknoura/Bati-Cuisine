package org.wora.ui;

import org.wora.entity.Client;
import org.wora.repository.ClientRepository;
import org.wora.service.ClientService;
import org.wora.service.serviceImpl.ClientServiceImpl;
import org.wora.utilitaire.ValidationUtils;

import java.util.Optional;
import java.util.ServiceConfigurationError;

import static org.wora.utilitaire.InputScanner.*;

public class ClientUI {
    private ClientService clientService;

    public ClientUI(ClientRepository clientRepository) {
        this.clientService = new ClientServiceImpl(clientRepository);
    }

    public Optional<Client> handleClientSelection() {
        System.out.println("--- Recherche de client ---");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");

        int option = scanInt("Entrez votre choix :  ", ValidationUtils.POSITIVE_INT);
        Optional<Client> result;

        switch (option) {
            case 1:
                result = findExistingClient();
                break;
            case 2:
                result = createNewClient();
                break;
            default:
                result = Optional.empty();
                break;
        }

        return result;

    }

    private Optional<Client> findExistingClient() {
        String clientName = scanString("Entrez le nom de client: ", ValidationUtils.combine(
                ValidationUtils.NOT_BLANK,
                input -> clientService.findClientByName(input).isPresent()
        ));
        Optional<Client> clientOpt = clientService.findClientByName(clientName);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            System.out.println("Client trouvé :");
            System.out.println("Nom: " + client.getName());
            System.out.println("Adresse : " + client.getAdress());
            System.out.println("Numéro de téléphone : " + client.getNumberPhone());

            Boolean choice = scanBoolean("Souhaitez-vous continuer avec ce client ? (y/n) : ");
            if (choice) {
                return Optional.of(client);
            }
            return Optional.empty();
        }else {
            throw new RuntimeException("client not found ");
        }
    }

    private Optional<Client> createNewClient() {
        System.out.println("--- Ajout d'un nouveau client ---");

        String name = scanString("Nom du client : ", ValidationUtils.combine(
                ValidationUtils.NOT_BLANK
        ));
        String address = scanString("Address du client : ", ValidationUtils.NOT_BLANK);
        String phoneNumber = scanString("Numero phone : ", ValidationUtils.combine(
                ValidationUtils.NOT_BLANK,
                ValidationUtils.VALID_PHONE
        ));
        Boolean isProfessionnel = scanBoolean("Le client est-il un professionnel ? (oui/non) : ");

        Client client = new Client();
        client.setName(name);
        client.setAdress(address);
        client.setNumberPhone(phoneNumber);
        client.setProfessionel(isProfessionnel);

        if (isProfessionnel) {
            System.out.print("Entrez le taux de remise (%): ");
            Double remiseRate = scanDouble("Entrez le taux de remise (%): ", ValidationUtils.POSITIVE_DOUBLE);
            client.setRemiseRate(remiseRate);
        } else {
            client.setRemiseRate(0);
        }
        clientService.addClient(client);
        return Optional.of(client);
    }
}
