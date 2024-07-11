package com.io.hhplus.concert.infrastructure.reservation.repository.impl;

import com.io.hhplus.concert.common.enums.TicketStatus;
import com.io.hhplus.concert.domain.reservation.model.Ticket;
import com.io.hhplus.concert.domain.reservation.repository.TicketRepository;
import com.io.hhplus.concert.infrastructure.reservation.repository.jpaRepository.TicketJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {

    private final TicketJpaRepository ticketJpaRepository;

    private boolean isNotDeleted(Date deletedAt) {
        return deletedAt == null;
    }

    private boolean isEqualTicketStatus(TicketStatus ticketStatus, TicketStatus targetTicketStatus) {
        return ticketStatus != null && ticketStatus.equals(targetTicketStatus);
    }

    private Ticket mapEntityToDto(com.io.hhplus.concert.infrastructure.reservation.entity.Ticket entity) {
        return Ticket.create(
                entity.getId(),
                entity.getReservationId(),
                entity.getConcertId(),
                entity.getPerformanceId(),
                entity.getSeatId(),
                entity.getConcertName(),
                entity.getArtistName(),
                entity.getIsReceiveOnline().compareTo(1) == 0,
                entity.getIsReceiveOnSite().compareTo(1) == 0,
                entity.getIsReceiveByPost().compareTo(1) == 0,
                entity.getPrice(),
                entity.getPerformedAt(),
                entity.getSeatNo(),
                entity.getTicketStatus(),
                entity.getReservedAt(),
                entity.getPublishedAt(),
                entity.getReceivedAt(),
                entity.getCancelAcceptedAt(),
                entity.getCancelApprovedAt()
        );
    }

    private com.io.hhplus.concert.infrastructure.reservation.entity.Ticket mapDtoToEntity(Ticket dto) {
        return com.io.hhplus.concert.infrastructure.reservation.entity.Ticket.builder()
                .id(dto.ticketId())
                .reservationId(dto.reservationId())
                .concertId(dto.concertId())
                .performanceId(dto.performanceId())
                .seatId(dto.seatId())
                .concertName(dto.concertName())
                .artistName(dto.artistName())
                .isReceiveByPost(dto.isReceiveOnline() ? 1 : 0)
                .isReceiveOnSite(dto.isReceiveOnline() ? 1 : 0)
                .isReceiveByPost(dto.isReceiveByPost() ? 1 : 0)
                .price(dto.price())
                .performedAt(dto.performedAt())
                .seatNo(dto.seatNo())
                .ticketStatus(dto.ticketStatus())
                .reservedAt(dto.reservedAt())
                .publishedAt(dto.publishedAt())
                .receivedAt(dto.receivedAt())
                .cancelAcceptedAt(dto.cancelAcceptedAt())
                .cancelApprovedAt(dto.cancelApprovedAt())
                .build();
    }

    @Override
    public Ticket save(Ticket ticket) {
        return mapEntityToDto(ticketJpaRepository.save(mapDtoToEntity(ticket)));
    }

    @Override
    public BigDecimal sumTicketPriceByReservationIdAndTicketStatus(Long reservationId, TicketStatus ticketStatus) {
        return ticketJpaRepository.findAllByReservationId(reservationId)
                .stream()
                .filter(entity -> isNotDeleted(entity.getDeletedAt()) && isEqualTicketStatus(entity.getTicketStatus(), ticketStatus))
                .map(com.io.hhplus.concert.infrastructure.reservation.entity.Ticket::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Ticket> findAllByReservationIdAndTicketStatus(Long reservationId, TicketStatus ticketStatus) {
        return ticketJpaRepository.findAllByReservationId(reservationId)
                .stream()
                .filter(entity -> isNotDeleted(entity.getDeletedAt()) && isEqualTicketStatus(entity.getTicketStatus(), ticketStatus))
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> saveAll(List<Ticket> tickets) {
        return ticketJpaRepository.saveAll(tickets.stream().map(this::mapDtoToEntity).collect(Collectors.toList()))
                .stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

}
