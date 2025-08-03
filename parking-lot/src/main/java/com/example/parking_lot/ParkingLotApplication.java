package com.example.parking_lot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.example.parking_lot.entity.ParkingSpot;
import com.example.parking_lot.entity.SpotSize;
import com.example.parking_lot.repository.ParkingSpotRepository;

@SpringBootApplication
public class ParkingLotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingLotApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataInitializer(ParkingSpotRepository spotRepository) {
		return args -> {
			if (spotRepository.count() == 0) {
				for (int i = 1; i <= 5; i++) {
					spotRepository.save(new ParkingSpot(null, SpotSize.SMALL, true, i));
					spotRepository.save(new ParkingSpot(null, SpotSize.MEDIUM, true, 100 + i));
					spotRepository.save(new ParkingSpot(null, SpotSize.LARGE, true, 200 + i));
				}
			}
		};
	}
}
