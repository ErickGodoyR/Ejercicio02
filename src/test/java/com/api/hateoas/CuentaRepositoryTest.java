package com.api.hateoas;

import com.api.hateoas.entity.Cuenta;
import com.api.hateoas.repository.CuentaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@Rollback(value = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CuentaRepositoryTest {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Test
    void testAgregarCuenta(){
        Cuenta cuenta = new Cuenta(1, "100123");
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);

        //Ver por que no funciona assertThat de prueba unitaria
        //Assertions.assertThat(cuentaGuardada).isNotNull();
        //Assertions.assertThat(cuentaGuardada.getId()).isGreaterThan();
    }


}
