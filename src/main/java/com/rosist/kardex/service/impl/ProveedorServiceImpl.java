package com.rosist.kardex.service.impl;

import java.sql.SQLException;
import java.time.LocalDateTime;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rosist.kardex.model.Proveedor;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.repo.IProveedorRepo;
import com.rosist.kardex.service.IProveedorService;

@Service
public class ProveedorServiceImpl extends CRUDImpl<Proveedor, Integer> implements IProveedorService {

	@Autowired
	private IProveedorRepo repo;
	
    private static Logger logger = LoggerFactory.getLogger(ProveedorServiceImpl.class);

	@Override
	protected IGenericRepo<Proveedor, Integer> getRepo() {
		return repo;
	}

	@Override
	public Page<Proveedor> listarPageable(Pageable page) {
		return repo.findAll(page);
	}

	@Override
	public Proveedor buscaPorRUC(String ruc) throws Exception {
		Proveedor proveedor = repo.buscaPorRUC(ruc);
		return proveedor;
	}

	@Transactional
	@Override
	public Proveedor registrarTransaccion(Proveedor proveedor) throws SQLException, Exception {
//        String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime   dUser = LocalDateTime.now();
	    logger.info("registrarTransaccion...grabada.-> " + proveedor);
        
		if (proveedor.getIdProveedor()==0) {
	        proveedor.setEstado("00");
		    proveedor.setUserup("cUser");
		    proveedor.setDuserup(dUser);
		}

		repo.save(proveedor);
		
		return proveedor;
	}

	@Transactional
	@Override
	public Proveedor modificaProveedor(Proveedor proveedor) throws Exception {
	    logger.info("modificando proveedor...grabada.-> ");
		String cUser = "cUser";
//      String cUser = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDateTime   dUser = LocalDateTime.now();
		proveedor.setUserup(cUser);
		proveedor.setDusercr(dUser);
		repo.modificaProveedor(proveedor.getNombre(), proveedor.getDireccion(), cUser, dUser, proveedor.getIdProveedor());
		return proveedor;
	}
	
}