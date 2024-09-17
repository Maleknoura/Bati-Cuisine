package org.wora.serviceImpl;

import org.wora.entity.Client;
import org.wora.repository.ClientRepository;
import org.wora.service.ClientService;

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
    public boolean clientExistsByName(String name) {
        return clientRepository.clientExistsByName(name);
    }

    @Override
    public Client getClientById(int id) {
        return clientRepository.getClientById(id);
    }
}
