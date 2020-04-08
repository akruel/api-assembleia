package br.com.sicredi.assembleia.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
@Entity
public class Associado {
    @Tolerate
    public Associado() {}

    @Id
    private String cpf;
    private String nome;
    private String email;
}