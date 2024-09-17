package org.wora.repository;

import org.wora.entity.Client;

public interface ClientRepository {

    void addClient(Client client);
    boolean clientExistsByName(String email);
    Client getClientById(int id);
}
