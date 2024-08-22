package com.dio.patterns.dto;

import com.dio.patterns.model.Endereco;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClienteDTO {

    private String nome;

    private Endereco endereco;
}
