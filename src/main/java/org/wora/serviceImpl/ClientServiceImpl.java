package org.wora.serviceImpl;

import org.wora.entity.Client;
import org.wora.repository.ClientRepository;
import org.wora.service.ClientService;

import java.util.Optional;

public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void addClient(Client client) {

        clientRepository.addClient(client);
    }


    @Override

    public Client clientExistsByName(String name) {
        Optional<Client> optionalClient = clientRepository.findClientByName(name);
        return optionalClient.orElse(null);
    }



    @Override
    public Client getClientById(int id) {
        return clientRepository.getClientById(id);
    }
}
