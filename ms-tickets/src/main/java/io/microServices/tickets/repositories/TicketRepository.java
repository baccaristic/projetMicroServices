package io.microServices.tickets.repositories;

import io.microServices.tickets.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Collection<Ticket> findByUserId(String userId);
}
