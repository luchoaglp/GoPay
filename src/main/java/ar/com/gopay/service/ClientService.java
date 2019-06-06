package ar.com.gopay.service;

import ar.com.gopay.domain.Client;
import ar.com.gopay.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public Client getById(Long id) {
        return clientRepository.findById(id).get();
    }

    public Client getByEmail(String email) {
        return clientRepository.findByEmail(email).orElse(null);
    }

    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    public boolean existsByDni(String dni) {
        return clientRepository.existsByDni(dni);
    }

    public void editData(Client client, Long clientId) {

        Client entity = clientRepository.getOne(clientId);

        entity.setFirstName(client.getFirstName());
        entity.setLastName(client.getLastName());
        entity.setDni(client.getDni());
        entity.setPhone(client.getPhone());

        save(entity);
    }

    public void editPassword(String password, Long clientId) {

        Client entity = clientRepository.getOne(clientId);

        entity.setPassword(password);

        save(entity);
    }

}
