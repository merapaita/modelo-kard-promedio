//package com.rosist.kardex.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import com.rosist.kardex.model.Rol;
//import com.rosist.kardex.repo.IGenericRepo;
//import com.rosist.kardex.repo.IRolRepo;
//import com.rosist.kardex.service.IRolService;
//
//@Service
//public class RolServiceImpl extends CRUDImpl<Rol, Integer> implements IRolService {
//
//	@Autowired
//	private IRolRepo repo;
//	
//	@Override
//	protected IGenericRepo<Rol, Integer> getRepo() {
//		return repo;
//	}
//	
//	@Override
//	public Page<Rol> listarPageable(Pageable page) {
//		return repo.findAll(page);
//	}
//
//}