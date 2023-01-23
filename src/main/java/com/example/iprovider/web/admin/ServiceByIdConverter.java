package com.example.iprovider.web.admin;

import com.example.iprovider.data.ServiceRepository;
import com.example.iprovider.entities.Service;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ServiceByIdConverter implements Converter<String, Service> {

    private final ServiceRepository serviceRepository;

    public ServiceByIdConverter(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Service convert(String id) {
        return serviceRepository.findById(Long.parseLong(id)).orElse(null);
    }
}
