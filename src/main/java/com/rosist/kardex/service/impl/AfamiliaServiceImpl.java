package com.rosist.kardex.service.impl;

import java.util.List;

import com.rosist.kardex.model.Aclase;
import com.rosist.kardex.search.SearchAClaseSpecification;
import com.rosist.kardex.search.SearchAFamiliaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
	public Page<Afamilia> listarPageable(Integer idClase, String descri, Integer page, Integer size) {
		SearchAFamiliaSpecification specification = new SearchAFamiliaSpecification(idClase, descri);
		List<Afamilia> aFamiliasTotal = repo.findAll(specification);
		Pageable pageRequest = createPageRequestUsing(page, size);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), aFamiliasTotal.size());		//allCustomers.size()
		List<Afamilia> pageContent = aFamiliasTotal.subList(start, end);
		return new PageImpl<>(pageContent, pageRequest, aFamiliasTotal.size());
	}

	private Pageable createPageRequestUsing(int page, int size) {
		return PageRequest.of(page, size);
	}

	@Override
	public List<Afamilia> listaPorClase(Integer idClase) throws Exception {
		SearchAFamiliaSpecification specification = new SearchAFamiliaSpecification(idClase, "");
		List<Afamilia> aFamilia      = repo.findAll(specification);
		return aFamilia;
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