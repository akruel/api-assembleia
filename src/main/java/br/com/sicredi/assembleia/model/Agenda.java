package br.com.sicredi.assembleia.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
public class Agenda implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;

}