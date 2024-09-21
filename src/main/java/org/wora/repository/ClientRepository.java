package org.wora.repository;

import org.wora.entity.Client;
import java.util.Optional;

import java.util.List;

public interface ClientRepository {

    void addClient(Client client);
    Optional<Client> findClientByName(String name);
    Client getClientById(int id);

}
