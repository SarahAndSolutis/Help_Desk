package com.helpdesk.notification_service.consumer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.helpdesk.notification_service.config.RabbitMQConfig;
import com.helpdesk.notification_service.dto.TicketEventDTO;
import com.helpdesk.notification_service.model.Notification;
import com.helpdesk.notification_service.repository.NotificationRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveTicketEvent(@Valid TicketEventDTO event) {
        log.info("Processando evento: {} para o Ticket ID {}", event.eventType(), event.ticket().id());

        String messageText = generateMessage(event);

        Notification notification = new Notification();
        notification.setTicketId(event.ticket().id());
        notification.setMessage(messageText);

        notificationRepository.save(notification);
    }

    private String generateMessage(TicketEventDTO event) {
        return switch (event.eventType()) {
            case "TicketCreated" -> "Novo chamado aberto: " + event.ticket().title();
            case "TicketAssigned" -> "Técnico atribuído ao chamado: " + event.ticket().title();
            case "TicketStatusChanged" -> "Status do chamado alterado para " + event.ticket().status();
            default -> "Atualização no chamado: " + event.ticket().title();
        };
    }
}