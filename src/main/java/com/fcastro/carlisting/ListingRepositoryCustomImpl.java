package com.fcastro.carlisting;

import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ListingRepositoryCustomImpl implements ListingRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Listing> findAllByAllParams(MultiValueMap<String, String> params){

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Listing> query = cb.createQuery(Listing.class);
        Root<Listing> listing = query.from(Listing.class);

        List<Predicate> predicates = buildCriteria(params, listing, cb);

        query.select(listing)
                .where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query).getResultList();
    }

    private List<Predicate> buildCriteria(MultiValueMap<String, String> params, Root<Listing> listing, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        if (params == null || params.isEmpty()) {
            return predicates;
        }

        List<String> makes = params.get("make");
        if (makes != null) {
            CriteriaBuilder.In<String> inClauseMake = cb.in(cb.upper(listing.<String>get("make")));
            makes.forEach(make -> inClauseMake.value(make.toUpperCase()));
            predicates.add(inClauseMake);
        }

        List<String> models = params.get("model");
        if (models != null) {
            CriteriaBuilder.In<String> inClauseModel = cb.in(cb.upper(listing.<String>get("model")));
            models.forEach(model -> inClauseModel.value(model.toUpperCase()));
            predicates.add(inClauseModel);
        }

        List<String> colors = params.get("color");
        if (colors != null) {
            CriteriaBuilder.In<String> inClauseColor = cb.in(cb.upper(listing.<String>get("color")));
            colors.forEach(color -> inClauseColor.value(color.toUpperCase()));
            predicates.add(inClauseColor);
        }

        List<String> years = params.get("year");
        if (years != null) {
            List<Predicate> yearPredicates = new ArrayList<>();
            years.forEach((year) -> yearPredicates.add(cb.equal(listing.<Integer>get("year"), year)));
            predicates.add(cb.and(cb.or(yearPredicates.toArray(new Predicate[yearPredicates.size()]))));
        }
        return predicates;
    }
}
