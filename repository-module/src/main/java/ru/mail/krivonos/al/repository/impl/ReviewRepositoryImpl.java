package ru.mail.krivonos.al.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.ReviewRepository;
import ru.mail.krivonos.al.repository.model.Review;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("reviewRepository")
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Review> findNotHiddenReviews(int limit, int offset) {
        String queryString = String.format("from %s where hidden = 0 order by date_of_creation desc",
                entityClass.getName());
        Query query = entityManager.createQuery(queryString)
                .setMaxResults(limit)
                .setFirstResult(offset);
        return query.getResultList();
    }

    @Override
    public int getCountOfNotHiddenEntities() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Review> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(criteriaBuilder.isFalse(root.get("isHidden")));
        return Math.toIntExact(entityManager.createQuery(criteriaQuery).getSingleResult());
    }
}
