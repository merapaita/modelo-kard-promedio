package com.rosist.kardex.service.impl;

import java.util.List;

import com.rosist.kardex.search.SearchAClaseSpecification;
import com.rosist.kardex.search.SearchAGrupoSpecification;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
	public Page<Aclase> listarPageable(Integer idGrupo, String descri, Integer page, Integer size) {
		SearchAClaseSpecification specification = new SearchAClaseSpecification(idGrupo, descri);
		List<Aclase> aClasesTotal = repo.findAll(specification);
		Pageable pageRequest = createPageRequestUsing(page, size);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), aClasesTotal.size());		//allCustomers.size()
		List<Aclase> pageContent = aClasesTotal.subList(start, end);
		return new PageImpl<>(pageContent, pageRequest, aClasesTotal.size());
	}

	private Pageable createPageRequestUsing(int page, int size) {
		return PageRequest.of(page, size);
	}

	@Override
	public List<Aclase> listaPorGrupo(Integer idGrupo) throws Exception {
		SearchAClaseSpecification specification = new SearchAClaseSpecification(idGrupo, "");
		List<Aclase> artmaeclase = repo.findAll(specification);
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
