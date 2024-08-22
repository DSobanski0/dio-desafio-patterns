package com.dio.patterns.service.impl;

import com.dio.patterns.builder.ClienteBuilder;
import com.dio.patterns.client.ViaCepClient;
import com.dio.patterns.dto.ClienteDTO;
import com.dio.patterns.model.Cliente;
import com.dio.patterns.model.Endereco;
import com.dio.patterns.repository.ClienteRepository;
import com.dio.patterns.repository.EnderecoRepository;
import com.dio.patterns.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    // Singleton: Injetar os componentes do Spring com @Autowired.
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ViaCepClient viaCepClient;

    @Autowired
    private ClienteBuilder clienteBuilder;

    // Strategy: Implementar os métodos definidos na interface.
    // Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

    @Override
    public List<ClienteDTO> buscarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        List<ClienteDTO> clientesDto = new ArrayList<>();
        clientes.forEach(cliente -> {
            // Builder: Utilizar o Lombok para realizar construções de objetos por meio de builder.
            clientesDto.add(clienteBuilder.entityToDto(cliente));
        });
        return clientesDto;
    }

    @Override
    public ClienteDTO buscarPorId(Long id) {
        // Buscar Cliente por ID.
        Optional<Cliente> cliente = clienteRepository.findById(id);
        ClienteDTO clienteDTO = null;
        if (cliente.isPresent()) {
            clienteDTO = clienteBuilder.entityToDto(cliente.get());
        }
        return clienteDTO;
    }

    @Override
    public Cliente inserir(ClienteDTO clienteDTO) {
        Cliente cliente = clienteBuilder.dtoToEntity(clienteDTO);
        return salvarClienteComCep(cliente);
    }

    @Override
    public Cliente atualizar(Long id, ClienteDTO clienteDTO) {
        // Buscar Cliente por ID, caso exista:
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            // Builder: Utilizar o Lombok para realizar construções de objetos por meio de builder.
            Cliente cliente = clienteBuilder.dtoToEntity(clienteDTO);
            cliente.setId(id);
            return salvarClienteComCep(cliente);
        }
        return null;
    }

    @Override
    public void deletar(Long id) {
        // Deletar Cliente por ID.
        clienteRepository.deleteById(id);
    }

    private Cliente salvarClienteComCep(Cliente cliente) {
        // Verificar se o Endereco do Cliente já existe (pelo CEP).
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(Long.valueOf(cep)).orElseGet(() -> {
            // Caso não exista, integrar com o ViaCEP e persistir o retorno.
            Endereco novoEndereco = viaCepClient.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        // Inserir Cliente, vinculando o Endereco (novo ou existente).
        return clienteRepository.save(cliente);
    }

}