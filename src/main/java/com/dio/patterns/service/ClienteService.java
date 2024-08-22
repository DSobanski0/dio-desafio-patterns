package com.dio.patterns.service;

import com.dio.patterns.dto.ClienteDTO;
import com.dio.patterns.model.Cliente;

import java.util.List;

public interface ClienteService {

    List<ClienteDTO> buscarTodos();

    ClienteDTO buscarPorId(Long id);

    Cliente inserir(ClienteDTO clienteDTO);

    Cliente atualizar(Long id, ClienteDTO clienteDTO);

    void deletar(Long id);
}