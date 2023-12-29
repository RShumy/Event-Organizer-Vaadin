package com.eventorganizr.organizr.service;

import com.eventorganizr.organizr.entity.Authority;
import com.eventorganizr.organizr.entity.User;
import com.eventorganizr.organizr.exception.RecordNotFoundException;
import com.eventorganizr.organizr.repository.UserRepository;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = encoder;
    }

    public User findUser(Long id) {
       return userRepository.findById(id)
               .orElseThrow(RecordNotFoundException::new);
    }

    public User findUserByUserName(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(RecordNotFoundException::new);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        //Removed Password field from updateUser method to prevent perpetual re-encoding when save user is called
        //Password is saved only at registration if the UserName is not found in the Data Base
        System.out.println("Save User called from User Service");
        try { userRepository.save(user); }
        catch (RuntimeException e){
            throw new RuntimeException("Cannot create user with existing User Name or Email!");
        }
    }

    //For insertion and learning purposes only, to be deleted afterwards
    //---------------------------------------------------------------
    public ServiceHolder<UserService,User> createUser(String UserName,String FirstName,String LastName,String Email,String Password,boolean IsActive) {
        User user = new User( UserName, FirstName, LastName, Email, Password, IsActive);
        saveUser(user);
        updatePassword(user);
        return ServiceHolder.of(this).with(user);
    }

    @Data
    public static class ServiceHolder<S extends UserService, V extends User>{
        private S service;
        private V value;
        private ServiceHolder(S service){
            this.service = service;
        }

        public static <S extends UserService, V extends User> ServiceHolder<S, V> of(S service){
            return new ServiceHolder<>(Objects.requireNonNull(service));
        }

        public <B extends UserService,U extends User> ServiceHolder<B, U> with(U value){
            if (this.service != null){
                this.value = (V) value;
                return (ServiceHolder<B, U>) this;
            }
            else throw new RuntimeException("Cannot return ServiceHolder!");
        }

        public void withAuthorities(Authority... authorities){
            for (Authority authority : authorities) {
                value.addAuthority(authority);
                service.saveUser(value);
            }
        };

    }
    //----------------------------------------------------------

    public void updatePassword(User user){
        User userToUpdate = findUser(user.getUserId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        saveUser(user);
    }

    //Removed Password field from updateUser method to prevent perpetual re-encoding when save user is called
    //Password is saved only at registration if the UserName is not found in the Data Base
    public void updateUser(Long id, User user){
        User userToUpdate = findUser(id);
        userToUpdate.setUserName(user.getUserName());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setEmail(user.getEmail());

        userRepository.save(userToUpdate);
    }

    public void delete(Long id){
        boolean existsById = userRepository.existsById(id);
        if (!existsById) throw new RecordNotFoundException();
        userRepository.deleteById(id);
    }

}
