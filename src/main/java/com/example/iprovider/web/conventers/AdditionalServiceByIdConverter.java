package com.example.iprovider.web.conventers;

import com.example.iprovider.data.AdditionalServiceRepository;
import com.example.iprovider.entities.AdditionalService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AdditionalServiceByIdConverter implements Converter<String, AdditionalService> {
    private final AdditionalServiceRepository additionalServiceRepository;

    public AdditionalServiceByIdConverter(AdditionalServiceRepository additionalServiceRepository) {
        this.additionalServiceRepository = additionalServiceRepository;
    }

    @Override
    public AdditionalService convert(String id) {
        return additionalServiceRepository.read(Long.parseLong(id)).orElse(null);
    }
}
