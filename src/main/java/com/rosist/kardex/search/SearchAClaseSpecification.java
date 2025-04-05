package com.rosist.kardex.search;

import com.rosist.kardex.model.Aclase;
import com.rosist.kardex.model.Agrupo;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchAClaseSpecification implements Specification<Aclase> {
    private Integer idGrupo;
    private String descri;

    private static final Logger log = LoggerFactory.getLogger(SearchAClaseSpecification.class);

    @Override
    public Predicate toPredicate(Root<Aclase> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        Join<Aclase, Agrupo> claseGrupoJoin = root.join("agrupo");
        if(idGrupo!=null){
            Expression<String> _idGrupo = criteriaBuilder.toString(claseGrupoJoin.get("idGrupo"));
            Predicate grupoEqualsPredicate = criteriaBuilder.equal(_idGrupo, idGrupo);
                predicates.add(grupoEqualsPredicate);
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
