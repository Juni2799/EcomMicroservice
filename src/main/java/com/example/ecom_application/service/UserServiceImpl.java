package com.example.ecom_application.service;

import com.example.ecom_application.dtos.AddressDTO;
import com.example.ecom_application.dtos.UserRequest;
import com.example.ecom_application.dtos.UserResponse;
import com.example.ecom_application.entity.Address;
import com.example.ecom_application.entity.User;
import com.example.ecom_application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream()
                .map(this::userToUserResponseMapper)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponse> listUserById(Long userId) {
        return userRepository.findById(userId).map(this::userToUserResponseMapper);
    }

    @Override
    public void createNewUser(UserRequest userRequest) {
        User user = new User();
        userRequestToUserMapper(userRequest, user);
        userRepository.save(user);
    }

    @Override
    public boolean updateExistingUser(Long userId, UserRequest userRequest) {
        Optional<User> savedUser = userRepository.findById(userId);
        if(savedUser.isEmpty()){
            return false;
        }

        User user = savedUser.get();
        updateUserRequestToUser(userRequest, user);
        userRepository.save(user);

        return true;
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    private UserResponse userToUserResponseMapper(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setUserRole(user.getUserRole());

        if(user.getAddress() != null){
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            userResponse.setAddressDTO(addressDTO);
        }

        return userResponse;
    }

    private void userRequestToUserMapper(UserRequest userRequest, User user) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if(userRequest.getAddressDTO() != null){
            Address address = new Address();
            address.setStreet(userRequest.getAddressDTO().getStreet());
            address.setCity(userRequest.getAddressDTO().getCity());
            address.setState(userRequest.getAddressDTO().getState());
            address.setCountry(userRequest.getAddressDTO().getCountry());
            address.setZipcode(userRequest.getAddressDTO().getZipcode());
            user.setAddress(address);
        }
    }

    private void updateUserRequestToUser(UserRequest userRequest, User user) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if(userRequest.getAddressDTO() != null){
            Address address = user.getAddress();
            address.setStreet(userRequest.getAddressDTO().getStreet());
            address.setCity(userRequest.getAddressDTO().getCity());
            address.setState(userRequest.getAddressDTO().getState());
            address.setCountry(userRequest.getAddressDTO().getCountry());
            address.setZipcode(userRequest.getAddressDTO().getZipcode());
            user.setAddress(address);
        }
    }
}
