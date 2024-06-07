package io.microServices.tickets.services;

import io.microServices.tickets.dto.CommentCreateDTO;
import io.microServices.tickets.dto.CommentReadDTO;
import io.microServices.tickets.entities.Comment;
import io.microServices.tickets.listeners.TicketEntityListener;
import io.microServices.tickets.repositories.CommentRepository;
import io.microServices.tickets.repositories.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CommentReadDTO createComment(CommentCreateDTO commentCreateDTO) {
        Comment comment = modelMapper.map(commentCreateDTO, Comment.class);
        var ticket = ticketRepository.findById(commentCreateDTO.getTicketId()).orElseThrow(() -> new RuntimeException("Ticket not found"));
        comment.setTicket(ticket);
        comment = commentRepository.save(comment);
        String response = TicketEntityListener.callRemoteEndpoint(commentCreateDTO.getText() );
        Comment chatResponse = new Comment();
        chatResponse.setText(response);
        chatResponse.setTicket(ticket);
        chatResponse.setUserId(ticket.getUserId());
        chatResponse = commentRepository.save(chatResponse);
        return modelMapper.map(chatResponse, CommentReadDTO.class);
    }

    public List<CommentReadDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(comment -> modelMapper.map(comment, CommentReadDTO.class))
                .collect(Collectors.toList());
    }

    public CommentReadDTO getCommentById(Integer id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        return modelMapper.map(comment, CommentReadDTO.class);
    }

    public CommentReadDTO updateComment(Integer id, CommentCreateDTO commentCreateDTO) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        modelMapper.map(commentCreateDTO, comment);
        comment = commentRepository.save(comment);
        return modelMapper.map(comment, CommentReadDTO.class);
    }

    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}
