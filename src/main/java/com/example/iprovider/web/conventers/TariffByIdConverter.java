package com.example.iprovider.web.conventers;

import com.example.iprovider.data.TariffRepository;
import com.example.iprovider.entities.Tariff;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TariffByIdConverter implements Converter<String, Tariff> {

    private final TariffRepository tariffRepository;

    public TariffByIdConverter(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    public Tariff convert(String source) {
        return tariffRepository.readByID(Long.parseLong(source)).orElse(null);
    }
}
