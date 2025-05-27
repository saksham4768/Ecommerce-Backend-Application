package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.MyAPIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repository.AddressRepository;
import com.ecommerce.project.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService{

    public final ModelMapper modelMapper;
    public final AddressRepository addressRepository;
    public final UserRepository userRepository;
    public AddressServiceImpl(ModelMapper modelMapper, AddressRepository addressRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User currentUser) {
        Address address = modelMapper.map(addressDTO, Address.class);

        // Set bidirectional relationship
        address.setUser(currentUser);
        currentUser.getAddresses().add(address);

        // Save user only if cascade is enabled, or save only address if not modifying user
        addressRepository.save(address); // OR userRepository.save(currentUser) if cascade is enabled

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();

        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", addressId, "addressId"));

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddress(User user) {
        List<Address> addresses = user.getAddresses();
        return addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", addressId, "addressId"));

        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());
        address.setAddressId(addressDTO.getAddressId());
        address.setState(addressDTO.getState());
        address.setStreet(addressDTO.getStreet());
        address.setPinCode(addressDTO.getPinCode());
        address.setBuildingName(addressDTO.getBuildingName());

        Address savedAddress = addressRepository.save(address);

        User user = address.getUser();
        user.getAddresses().removeIf(addressU -> addressU.getAddressId().equals(addressId));

        user.getAddresses().add(savedAddress);
        userRepository.save(user);
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public String deleteAddressById(Long addressId, User user) {
        addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", addressId, "addressId"));

        boolean isUserAddressPresent = user.getAddresses().stream().findFirst().get().getAddressId().equals(addressId);
        if(!isUserAddressPresent){
            throw new MyAPIException("In Logged in user there is no address present for this address Id:- " + addressId);
        }

        user.getAddresses().removeIf(add -> add.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.deleteById(addressId);
        return "Address deleted successfully with addressId:- " + addressId;
    }
}
