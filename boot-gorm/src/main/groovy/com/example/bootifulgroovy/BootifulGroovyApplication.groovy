package com.example.bootifulgroovy

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller
import grails.gorm.services.Service
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView


@SpringBootApplication
class BootifulGroovyApplication {

    static void main(String[] args) {
        SpringApplication.run BootifulGroovyApplication, args
    }

    @Bean
    def initializer(ReservationRepository repository) {
        { args ->

            ["Jeff", "Nicki", "Josh"].forEach { repository.save(new Reservation(reservationName: it)) }

            repository.findAll().forEach { println it }

        } as ApplicationRunner
    }
}

@RestController
class ReservationRestController {

    @Autowired
    ReservationRepository reservationRepository

    @GetMapping('/reservations')
    def reservations() {
        reservationRepository.findAll()
    }

    @GetMapping('/reservations/{name}')
    def reservationsByName(@PathVariable String name) {
        reservationRepository.findByReservationName name
    }
}

@Controller
class ReservationMvcController {

    @Autowired
    ReservationRepository reservationRepository

    @GetMapping("/reservations.groovy")
    def groovyReservations() {
        new ModelAndView("reservations", [reservationList: reservationRepository.findAll()])
    }
}

@Service(Reservation)
interface ReservationRepository  {
    Collection<Reservation> findByReservationName(String reservationName)
    Collection<Reservation> findAll()
}

@Entity
@ToString
@EqualsAndHashCode
class Reservation {
    String reservationName
}