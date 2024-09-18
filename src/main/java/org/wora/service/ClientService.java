package org.wora.service;

import org.wora.entity.Client;

public interface ClientService {

    void addClient(Client client);

    Client clientExistsByName(String name);

    Client getClientById(int id);
}