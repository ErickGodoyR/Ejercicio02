package com.api.hateoas.service;

import com.api.hateoas.entity.Cuenta;
import com.api.hateoas.exception.CuentaNotFoundException;
import com.api.hateoas.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<Cuenta> listAll(){
        return cuentaRepository.findAll();
    }

    public Cuenta get(Integer id) {
        return cuentaRepository.findById(id).get();
    }

    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    public void delete(Integer id) throws CuentaNotFoundException {
        if(!cuentaRepository.existsById(id)){
            throw new CuentaNotFoundException("Cuenta no encontrada con el ID: " + id);
        }

        cuentaRepository.deleteById(id);
    }

    //método para depositar dinero a la cuenta
    public Cuenta depositar (float monto, Integer id) {
        cuentaRepository.actualizarMonto(monto, id);
        return cuentaRepository.findById(id).get();
    }

    //método para retirar dinero de la cuenta
    public Cuenta retirar (float monto, Integer id) {
        cuentaRepository.actualizarMonto(-monto, id);
        return cuentaRepository.findById(id).get();
    }

}
