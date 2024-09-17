package org.wora.service;

import org.wora.entity.Client;

public interface ClientService {

    void addClient(Client client);

    boolean clientExistsByName(String name);

    Client getClientById(int id);
}