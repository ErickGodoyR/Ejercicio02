package com.api.hateoas.controller;

import com.api.hateoas.entity.Cuenta;
import com.api.hateoas.entity.Monto;
import com.api.hateoas.service.CuentaService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;


    @GetMapping
    public ResponseEntity<List<Cuenta>> listarCuentas() {
        List<Cuenta> cuentas = cuentaService.listAll();

        if(cuentas.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        for(Cuenta cuenta:cuentas){
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuenta.getId())).withSelfRel());
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        }

        CollectionModel<Cuenta> modelo = CollectionModel.of(cuentas);
        modelo.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withSelfRel());

        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> listarCuenta(@PathVariable Integer id) {
        try{
            Cuenta cuenta = cuentaService.get(id);
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuenta.getId())).withSelfRel());
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));

            return new ResponseEntity<>(cuenta, HttpStatus.OK);
        }catch(Exception exception){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Cuenta> guardarCuenta(@RequestBody Cuenta cuenta) {
        Cuenta cuentaBBDD = cuentaService.save(cuenta);

        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBBDD.getId())).withSelfRel());
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));

        return ResponseEntity.created(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBBDD.getId())).toUri()).body(cuentaBBDD);

    }

    @PutMapping
    public ResponseEntity<Cuenta> actualizarCuenta(@RequestBody Cuenta cuenta) {
        Cuenta cuentaBBDD = cuentaService.save(cuenta);

        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBBDD.getId())).withSelfRel());
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));

        return new ResponseEntity<>(cuentaBBDD, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable Integer id) {
        try{
            cuentaService.delete(id);
            return  ResponseEntity.noContent().build();
        }catch (Exception exception){
            return ResponseEntity.notFound().build();
        }
    }


    //Depositar dinero
    @PatchMapping("/{id}/deposito")
    public ResponseEntity<Cuenta> depositarDinero (@PathVariable Integer id, @RequestBody Monto monto) {
        Cuenta cuentaBBDD = cuentaService.depositar(monto.getMonto(), id);

        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBBDD.getId())).withSelfRel());
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBBDD.getId(), null)).withRel("depósitos"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));

        return new ResponseEntity<>(cuentaBBDD, HttpStatus.OK);
    }

    //Depositar dinero
    @PatchMapping("/{id}/retiro")
    public ResponseEntity<Cuenta> retirarDinero (@PathVariable Integer id, @RequestBody Monto monto) {
        Cuenta cuentaBBDD = cuentaService.retirar(monto.getMonto(), id);

        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBBDD.getId())).withSelfRel());
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBBDD.getId(), null)).withRel("depósitos"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).retirarDinero(cuentaBBDD.getId(), null)).withRel("retiros"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));

        return new ResponseEntity<>(cuentaBBDD, HttpStatus.OK);
    }




}
