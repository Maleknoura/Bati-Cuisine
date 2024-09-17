package org.wora.Repository;

import org.wora.Entity.Client;

public interface ClientRepository {

    void addClient(Client client);
    boolean clientExistsByEmail(String email);
    Client getClientById(int id);
    Client getClientByEmail(String email);
}
