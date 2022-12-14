package com.example.delparque.service.impl;

import com.example.delparque.dto.CondominoInfo;
import com.example.delparque.dto.Trabajador;
import com.example.delparque.dto.WorkDay;
import com.example.delparque.model.Condomino;
import com.example.delparque.model.User;
import com.example.delparque.repository.CondominosRepository;
import com.example.delparque.repository.TrabajadoresRepository;
import com.example.delparque.repository.UsersRepository;
import com.example.delparque.repository.WorkDaysRepository;
import com.example.delparque.service.TrabajadoresService;
import com.example.delparque.service.mappers.TrabajadorMapper;
import com.example.delparque.service.mappers.WorkDayMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrabajadoresServiceImpl implements TrabajadoresService {

    private final TrabajadoresRepository trabajadoresRepository;
    private final UsersRepository usersRepository;
    private final CondominosRepository condominosRepository;
    private final WorkDaysRepository workDaysRepository;

    TrabajadoresServiceImpl(TrabajadoresRepository trabajadoresRepository,
                            UsersRepository usersRepository,
                            CondominosRepository condominosRepository,
                            WorkDaysRepository workDaysRepository) {
        this.trabajadoresRepository = trabajadoresRepository;
        this.usersRepository = usersRepository;
        this.condominosRepository = condominosRepository;
        this.workDaysRepository = workDaysRepository;
    }

    @Override
    public Page<Trabajador> findAll(Integer page) {

        List<Trabajador> trabajadores = trabajadoresRepository.findAll().stream()
                .map(this::addExtraInfo).toList();

        Pageable pageable = PageRequest.of(page, 10);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), trabajadores.size());

        return new PageImpl<>(
                trabajadores.subList(start, end),
                pageable, trabajadores.size()
        );
    }

    @Override
    public Trabajador findById(String id) {
        return trabajadoresRepository.findById(id)
                .map(this::addExtraInfo).orElse(null);
    }

    @Override
    public List<Trabajador> findByName(String name) {
        return trabajadoresRepository.findByName(name).stream()
                .map(this::addExtraInfo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Trabajador save(Trabajador trabajador) {
        if (trabajador.getId() == null) {
            trabajador.setId(UUID.randomUUID().toString());
        }
        com.example.delparque.model.Trabajador save = trabajadoresRepository.save(TrabajadorMapper.dtoToEntity(trabajador));


        saveWorkDays(trabajador.getWorkDays(), trabajador.getId());

        return TrabajadorMapper.entityToDto(save);
    }

    @Override
    public void delete(String id) {
        trabajadoresRepository.deleteById(id);
    }

    private void saveWorkDays(List<WorkDay> workDays, String trabajadorId) {

        workDaysRepository.deleteAllByTrabajadorId(trabajadorId);

        workDays.forEach(workDay -> {

            if (workDay.getId() == null) {
                workDay.setId(UUID.randomUUID().toString());
            }

            com.example.delparque.model.WorkDay w = WorkDayMapper.dtoToEntity(workDay);
            w.setTrabajadorId(trabajadorId);

            workDaysRepository.save(w);
        });
    }

    private Trabajador addExtraInfo(com.example.delparque.model.Trabajador t) {
        Trabajador trabajador = TrabajadorMapper.entityToDto(t);
        Condomino condomino = condominosRepository.findById(t.getCondominoId()).orElseThrow();
        User user = usersRepository.findById(condomino.getUserId()).orElseThrow();

        CondominoInfo condominoInfo = CondominoInfo.builder()
                .userId(condomino.getId())
                .houseNumber(condomino.getHouseNumber())
                .houseStreet(condomino.getStreet())
                .owner(user.getName())
                .build();

        trabajador.setCondominoInfo(condominoInfo);

        trabajador.setWorkDays(workDaysRepository.findAllByTrabajadorId(trabajador.getId()).stream()
                .map(WorkDayMapper::entityToDto).collect(Collectors.toList()));
        return trabajador;
    }
}