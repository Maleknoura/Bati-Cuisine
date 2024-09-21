package org.wora.service;

import org.wora.entity.Client;

import java.util.Optional;

public interface ClientService {

    void addClient(Client client);

    Optional<Client> findClientByName(String name);

    Client getClientById(int id);
}