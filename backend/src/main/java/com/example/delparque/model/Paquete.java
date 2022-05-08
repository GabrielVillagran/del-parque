package com.example.delparque.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "paquetes")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paquete {
    @Id
    private String id;

    private String emisor;

    private String receptor;

    private String caseta;

    private Boolean entregado;

    @Column(name = "id_condominio")
    private String idCondominio;
}