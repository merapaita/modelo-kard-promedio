package com.rosist.kardex.search;

import com.rosist.kardex.model.Agrupo;
import com.rosist.kardex.service.impl.AgrupoServiceImpl;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchAGrupoSpecification implements Specification<Agrupo> {
    private String tipo;
    private String descri;

    private static final Logger log = LoggerFactory.getLogger(SearchAGrupoSpecification.class);

    @Override
    public Predicate toPredicate(Root<Agrupo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(StringUtils.hasText(tipo)){
            Expression<String> _tipo = criteriaBuilder.toString(root.get("tipo"));
            Predicate nameLikePredicate = criteriaBuilder.equal(_tipo, tipo);
            predicates.add(nameLikePredicate);
        }
        if(StringUtils.hasText(descri)){
            Expression<String> _descri = criteriaBuilder.lower(root.get("descri"));
            Predicate nameLikePredicate = criteriaBuilder.like(_descri, "%".concat(descri.toUpperCase()).concat("%"));
            predicates.add(nameLikePredicate);
        }
//        query.orderBy(criteriaBuilder.asc(root.get("price")));
        return criteriaBuilder.and( predicates.toArray( new Predicate[predicates.size()] ) );
    }
}
