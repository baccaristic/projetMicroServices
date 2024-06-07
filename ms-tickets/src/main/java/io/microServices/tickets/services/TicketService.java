package io.microServices.tickets.services;

import io.microServices.tickets.dto.TicketCreateDTO;
import io.microServices.tickets.dto.TicketReadDTO;
import io.microServices.tickets.entities.Comment;
import io.microServices.tickets.entities.Ticket;
import io.microServices.tickets.listeners.TicketEntityListener;
import io.microServices.tickets.repositories.CommentRepository;
import io.microServices.tickets.repositories.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public TicketReadDTO createTicket(TicketCreateDTO ticketCreateDTO) {
        Ticket ticket = modelMapper.map(ticketCreateDTO, Ticket.class);
        ticket = ticketRepository.save(ticket);

        String response = TicketEntityListener.callRemoteEndpoint(ticketCreateDTO.getTest());
        Comment comment = new Comment();
        comment.setText(response);
        comment.setTicket(ticket);
        comment.setUserId(ticket.getUserId());

        commentRepository.save(comment);

        return modelMapper.map(ticket, TicketReadDTO.class);
    }

    public List<TicketReadDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticket -> modelMapper.map(ticket, TicketReadDTO.class))
                .collect(Collectors.toList());
    }

    public TicketReadDTO getTicketById(Integer id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
        return modelMapper.map(ticket, TicketReadDTO.class);
    }

    public TicketReadDTO updateTicket(Integer id, TicketCreateDTO ticketCreateDTO) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
        modelMapper.map(ticketCreateDTO, ticket);
        ticket = ticketRepository.save(ticket);
        return modelMapper.map(ticket, TicketReadDTO.class);
    }

    public void deleteTicket(Integer id) {
        ticketRepository.deleteById(id);
    }

    public List<TicketReadDTO> getTicketByUserId(String userId) {
        return ticketRepository.findByUserId(userId).stream()
                .map(ticket -> modelMapper.map(ticket, TicketReadDTO.class))
                .collect(Collectors.toList());
    }

    public TicketReadDTO updateTicketStatus(Integer id, String status) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setStatus(status);
        ticket = ticketRepository.save(ticket);
        return modelMapper.map(ticket, TicketReadDTO.class);
    }
}
