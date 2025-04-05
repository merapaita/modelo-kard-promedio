package com.rosist.kardex.search;

import com.rosist.kardex.model.Afamilia;
import com.rosist.kardex.model.Aclase;
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
public class SearchAFamiliaSpecification implements Specification<Afamilia> {
    private Integer idClase;
    private String descri;
    private static final Logger log = LoggerFactory.getLogger(SearchAFamiliaSpecification.class);
    @Override
    public Predicate toPredicate(Root<Afamilia> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Join<Aclase, Aclase> familiaClaseJoin = root.join("aclase");
        if(idClase!=null){
            Expression<String> _idClase = criteriaBuilder.toString(familiaClaseJoin.get("idClase"));
            Predicate claseEqualsPredicate = criteriaBuilder.equal(_idClase, idClase);
                predicates.add(claseEqualsPredicate);
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