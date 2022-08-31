package com.example.delparque.controllers;

import com.example.delparque.dto.Condomino;
import com.example.delparque.service.CondominosService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/condominos")
public class CondominosController {

    private final CondominosService condominosService;

    CondominosController(CondominosService condominosService) {
        this.condominosService = condominosService;
    }

    @GetMapping()
    public Page<Condomino> findAll(@RequestParam Integer page) {
        return condominosService.findAll(page);
    }

    @GetMapping("{id}")
    public Condomino findById(@PathVariable String id) {
        return condominosService.findById(id);
    }

    @GetMapping("nombre")
    public Condomino findByNombre(@RequestParam() String nombre) {
        return condominosService.findByNombre(nombre);
    }

    @GetMapping("telefono")
    public Condomino findByNumeroTelefono(@RequestParam() String telefono) {
        return condominosService.findByNumeroTelefono(telefono);
    }

    @PostMapping()
    public Condomino save(@RequestBody Condomino condomino) {
        return condominosService.save(condomino);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        condominosService.delete(id);
    }
}
