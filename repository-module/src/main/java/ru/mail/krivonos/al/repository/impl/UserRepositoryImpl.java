package ru.mail.krivonos.al.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.UserRepository;
import ru.mail.krivonos.al.repository.model.User;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository("userRepository")
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    @Override
    public User findUserByEmail(String email) {
        String queryString = String.format("from %s %s", entityClass.getName(),
                "where email = :email");
        Query query = entityManager.createQuery(queryString).setParameter("email", email);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
