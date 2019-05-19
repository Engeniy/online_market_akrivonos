package ru.mail.krivonos.al.repository;

import ru.mail.krivonos.al.repository.model.User;

public interface UserRepository extends GenericRepository<Long, User> {

    User findUserByEmail(String email);
}
