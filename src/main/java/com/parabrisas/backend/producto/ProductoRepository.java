package com.parabrisas.backend.producto;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository <Producto, Long> {
    /*Optional : List<Producto> findAll(spec:Specification<Producto>): List<Producto>*/
}
