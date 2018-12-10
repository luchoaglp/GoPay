package ar.com.gopay.service;

import ar.com.gopay.domain.Client;
import ar.com.gopay.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public Client getById(Long id) {
        return clientRepository.findById(id).get();
    }

    public boolean existsByUsername(String username) {
        return clientRepository.existsByUsername(username);
    }

    public boolean existsByDni(String dni) {
        return clientRepository.existsByDni(dni);
    }
}
