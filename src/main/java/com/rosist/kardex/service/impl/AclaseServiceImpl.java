package com.rosist.kardex.service.impl;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rosist.kardex.exception.ResourceNotFoundException;
import com.rosist.kardex.model.Aclase;
import com.rosist.kardex.model.Agrupo;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.repo.IAclaseRepo;
import com.rosist.kardex.service.IAclaseService;

@Service
public class AclaseServiceImpl extends CRUDImpl<Aclase, Integer> implements IAclaseService {

	@Autowired
	private IAclaseRepo repo;
	
	@Override
	protected IGenericRepo<Aclase, Integer> getRepo() {
		return repo;
	}

	@Override
	public Page<Aclase> listarPageable(Pageable page) {
		return repo.findAll(page);
	}
	
	@Override
	public List<Aclase> listaPorGrupo(Integer idGrupo) throws Exception {
		List<Aclase> artmaeclase = repo.listaPorGrupo(idGrupo);
		return artmaeclase;
	}

	@Override
	public Aclase buscaPorClase(String clase, Integer id_grupo) throws Exception {
		Aclase aclase = repo.buscaPorClase(clase, id_grupo);
		return aclase;
	}
	
	@Override
	public String getNewClase(Integer idGrupo) {
		String newClase = repo.getNewClase(idGrupo);
		return newClase;
	}
	
	@Transactional
	@Override
	public Aclase registrarAclase(Aclase aclase) throws Exception {
		if (aclase == null) {
			throw new ResourceNotFoundException("No se ha definido la clase a registrar.");
		} else if(aclase.getAgrupo().getIdGrupo()==null) {
			throw new ResourceNotFoundException("No se ha definido id de grupo a registrar.");
		}
		String clase = aclase.getClase();
		int idGrupo = aclase.getAgrupo().getIdGrupo();
		
		if (buscaPorClase(clase, idGrupo)!=null) {
			throw new ResourceNotFoundException("Clase ya se encuentra registrada...");
		}
		repo.save(aclase);
		return aclase;
	}
	

}
