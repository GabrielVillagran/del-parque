package com.example.delparque.service.impl;

import com.example.delparque.dto.Paquete;
import com.example.delparque.exception.BadRequestDataException;
import com.example.delparque.repository.PaquetesRepository;
import com.example.delparque.service.PackageService;
import com.example.delparque.service.mappers.PackageMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PackageServiceImpl implements PackageService {

    private final PaquetesRepository paquetesRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    PackageServiceImpl(PaquetesRepository paquetesRepository, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.paquetesRepository = paquetesRepository;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Paquete save(Paquete paquete) {
        if (paquete.getId() == null) {
            paquete.setId(UUID.randomUUID().toString());
        }

        if(paquete.getNombreEmpresa() == null) {
            throw new BadRequestDataException("nombre_empresa requerido", "NAME_SENDER_ERROR");
        }

        if(paquete.getNumeroGuia() == null) {
            throw new BadRequestDataException("numero_guia requerido", "NUMBER_GUIDE_ERROR");
        }

        if(paquete.getNombreCondomino() == null) {
            throw new BadRequestDataException("nombre_cond requerido", "HOUSE_NAME_ERROR");
        }

        if(paquete.getCalle() == null) {
            throw new BadRequestDataException("calle requerido", "STREET_ERROR");
        }

        if(paquete.getNumeroCasa() == null) {
            throw new BadRequestDataException("numero_casa requerido", "HOUSE_NUMBER_ERROR");
        }

        if(paquete.getRecibeInquilino() == null) {
            throw new BadRequestDataException("recibido requerido", "RECEIVED_BY_ERROR");
        }

        if(paquete.getFechaLlegada() == null) {
            throw new BadRequestDataException("fecha requerido", "DATE_ERROR");
        }

        if(paquete.getHoraLlegada() == null) {
            throw new BadRequestDataException("hora requerido", "TIME_ERROR");
        }

        if(paquete.getCasetaRecibida() == null) {
            throw new BadRequestDataException("caseta requerido", "ARRIVED_STAND_ERROR");
        }

        return PackageMapper.entityToDto(paquetesRepository.save(PackageMapper.dtoToEntity(paquete)));
    }

    @Override
    public Page<Paquete> findAll(Integer page) {

        String query = "SELECT * FROM paquetes";

        BeanPropertyRowMapper<Paquete> paquetesViewMapper = new BeanPropertyRowMapper<>(Paquete.class);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        Pageable pageable = PageRequest.of(page, 10);

        List<Paquete> paquetes = namedParameterJdbcTemplate.query(query +
                " LIMIT " + pageable.getPageSize() +
                " OFFSET " + pageable.getOffset(), mapSqlParameterSource, paquetesViewMapper);

        return new PageImpl<>(paquetes, pageable, pageable.getPageSize());
    }

    @Override
    public Paquete findById(String id) {
        return paquetesRepository.findById(id).map(PackageMapper::entityToDto).orElse(null);
    }

    @Override
    public void delete(String id) {
        paquetesRepository.deleteById(id);
    }
}