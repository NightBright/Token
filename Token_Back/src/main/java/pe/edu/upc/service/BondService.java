package pe.edu.upc.service;

import org.springframework.stereotype.Service;

import pe.edu.upc.model.entity.Bond;

@Service
public interface BondService extends CrudService<Bond>{
    public void results(int id,double tcea,double tcea_escuado,double trea,double cPrice,double vna);
}