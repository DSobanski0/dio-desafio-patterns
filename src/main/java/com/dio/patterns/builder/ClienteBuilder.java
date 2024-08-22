package com.dio.patterns.builder;

import com.dio.patterns.dto.ClienteDTO;
import com.dio.patterns.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteBuilder {

    public ClienteDTO entityToDto(Cliente cliente) {
        return ClienteDTO.builder().nome(cliente.getNome()).endereco(cliente.getEndereco()).build();
    }

    public Cliente dtoToEntity(ClienteDTO clienteDTO) {
        return  Cliente.builder().nome(clienteDTO.getNome()).endereco(clienteDTO.getEndereco()).build();
    }
}
