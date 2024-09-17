package org.wora.repository;

import org.wora.entity.Client;

import java.util.List;

public interface ClientRepository {

    void addClient(Client client);
    Client clientExistsByName(String email);
    Client getClientById(int id);

    List<Client> findAll();
}
