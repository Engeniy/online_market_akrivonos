package ru.mail.krivonos.al.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.krivonos.al.repository.RoleRepository;
import ru.mail.krivonos.al.repository.UserRepository;
import ru.mail.krivonos.al.repository.model.Role;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.PageCountingService;
import ru.mail.krivonos.al.service.PasswordService;
import ru.mail.krivonos.al.service.UserService;
import ru.mail.krivonos.al.service.converter.UserConverterAggregator;
import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.ProfileDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

import static ru.mail.krivonos.al.service.constant.LimitConstants.USERS_LIMIT;
import static ru.mail.krivonos.al.service.constant.OrderConstants.EMAIL;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverterAggregator userConverterAggregator;
    private final PasswordService passwordService;
    private final PageCountingService pageCountingService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            UserConverterAggregator userConverterAggregator,
            PasswordService passwordService,
            PageCountingService pageCountingService,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userConverterAggregator = userConverterAggregator;
        this.passwordService = passwordService;
        this.pageCountingService = pageCountingService;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDTO getUserByEmail(String email) {
        User userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail == null) {
            return null;
        }
        return userConverterAggregator.getUserAuthorizationConverter().toDTO(userByEmail);
    }

    @Override
    @Transactional
    public PageDTO<UserDTO> getUsers(Integer pageNumber) {
        PageDTO<UserDTO> pageDTO = new PageDTO<>();
        int countOfEntities = userRepository.getCountOfEntities();
        int countOfPages = pageCountingService.getCountOfPages(countOfEntities, USERS_LIMIT);
        pageDTO.setCountOfPages(countOfPages);
        int currentPageNumber = pageCountingService.getCurrentPageNumber(pageNumber, countOfPages);
        pageDTO.setCurrentPageNumber(currentPageNumber);
        int offset = pageCountingService.getOffset(currentPageNumber, USERS_LIMIT);
        List<User> users = userRepository.findAllWithAscendingOrder(USERS_LIMIT, offset, EMAIL);
        pageDTO.setList(getUserDTOs(users));
        return pageDTO;
    }

    @Override
    @Transactional
    public void updateRole(Long userID, Long roleID) {
        Role role = roleRepository.findById(roleID);
        User user = userRepository.findById(userID);
        user.setRole(role);
        userRepository.merge(user);
    }

    @Override
    @Transactional
    public void add(UserDTO userDTO) {
        User user = userConverterAggregator.getUserAuthorizationConverter().toEntity(userDTO);
        user.setPassword(passwordService.getPassword(user.getEmail()));
        user.getProfile().setUser(user);
        userRepository.persist(user);
    }

    @Override
    @Transactional
    public void deleteUsers(Long[] userIDs) {
        for (Long userID : userIDs) {
            User user = userRepository.findById(userID);
            userRepository.remove(user);
        }
    }

    @Override
    @Transactional
    public void changePassword(Long userID) {
        User userByID = userRepository.findById(userID);
        String password = passwordService.getPassword(userByID.getEmail());
        userByID.setPassword(password);
        userRepository.merge(userByID);
    }

    @Override
    @Transactional
    public UserDTO getUserByID(Long userID) {
        User byId = userRepository.findById(userID);
        return userConverterAggregator.getUserForProfileConverter().toDTO(byId);
    }

    @Override
    @Transactional
    public void updateProfile(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        ProfileDTO profile = userDTO.getProfile();
        user.getProfile().setAddress(profile.getAddress());
        user.getProfile().setTelephone(profile.getTelephone());
        userRepository.merge(user);
    }

    @Override
    @Transactional
    public void updatePassword(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId());
        String encodedPassword = passwordService.encodePassword(userDTO.getPassword());
        user.setPassword(encodedPassword);
        userRepository.merge(user);
    }

    private List<UserDTO> getUserDTOs(List<User> users) {
        return users.stream()
                .map(userConverterAggregator.getUserForShowingConverter()::toDTO)
                .collect(Collectors.toList());
    }
}
