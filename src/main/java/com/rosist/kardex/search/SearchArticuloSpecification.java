package com.rosist.kardex.search;

import com.rosist.kardex.model.Articulo;
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
public class SearchArticuloSpecification implements Specification<Articulo> {
    private String tipo;
    private String nomart;

    private static final Logger log = LoggerFactory.getLogger(SearchArticuloSpecification.class);

    @Override
    public Predicate toPredicate(Root<Articulo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(StringUtils.hasText(tipo)){
            Expression<String> _tipo = criteriaBuilder.toString(root.get("tipo"));
            Predicate nameLikePredicate = criteriaBuilder.equal(_tipo, tipo);
            predicates.add(nameLikePredicate);
        }
        if(StringUtils.hasText(nomart)){
            Expression<String> _nomart = criteriaBuilder.lower(root.get("nomart"));
            Predicate nomartLikePredicate = criteriaBuilder.like(_nomart, "%".concat(nomart.toUpperCase()).concat("%"));
            predicates.add(nomartLikePredicate);
        }
//        query.orderBy(criteriaBuilder.asc(root.get("price")));
        return criteriaBuilder.and( predicates.toArray( new Predicate[predicates.size()] ) );
    }
}
