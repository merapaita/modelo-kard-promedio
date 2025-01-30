package com.rosist.kardex.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rosist.kardex.exception.ResourceNotFoundException;
import com.rosist.kardex.model.Afamilia;
import com.rosist.kardex.repo.IAfamiliaRepo;
import com.rosist.kardex.repo.IGenericRepo;
import com.rosist.kardex.service.IAfamiliaService;

import jakarta.transaction.Transactional;

@Service
public class AfamiliaServiceImpl extends CRUDImpl<Afamilia, Integer> implements IAfamiliaService {

	@Autowired
	private IAfamiliaRepo repo;
	
	@Override
	protected IGenericRepo<Afamilia, Integer> getRepo() {
		return repo;
	}

	@Override
	public Page<Afamilia> listarPageable(Pageable page) {
		return repo.findAll(page);
	}
	
	@Override
	public List<Afamilia> listaPorClase(Integer idClase) throws Exception {
		List<Afamilia> artmaefamilia = repo.listaPorClase(idClase);
		return artmaefamilia;
	}
	
	@Override
	public Afamilia buscaPorFamilia(String familia, Integer idClase) throws Exception {
		Afamilia afamilia = repo.buscaPorFamilia(familia, idClase);
		return afamilia;
	}
	
	@Override
	public String getNewFamilia(Integer idClase) {
		String newClase = repo.getNewFamilia(idClase);
		return newClase;
	}
	
	@Transactional
	@Override
	public Afamilia registrarAfamilia(Afamilia afamilia) throws Exception {
		if (afamilia.getAclase() == null) {
			throw new ResourceNotFoundException("No se ha definido la familia a registrar.");
		} else if(afamilia.getAclase().getIdClase()==null) {
			throw new ResourceNotFoundException("No se ha definido id de clase a registrar.");
		}
		String familia = afamilia.getFamilia();
		int idClase = afamilia.getAclase().getIdClase();
		if (buscaPorFamilia(familia, idClase)!=null) {
			throw new ResourceNotFoundException("Familia ya se encuentra registrada.");
		}
		repo.save(afamilia);
		return afamilia;
	}

}
