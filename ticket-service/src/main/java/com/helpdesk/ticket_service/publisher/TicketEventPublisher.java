package com.helpdesk.ticket_service.publisher;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.helpdesk.ticket_service.config.RabbitMQConfig;
import com.helpdesk.ticket_service.dto.TicketEventDTO;
import com.helpdesk.ticket_service.dto.TicketResponseDTO;

@Component
public class TicketEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public TicketEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEvent(TicketResponseDTO ticket, String eventType) {
        TicketEventDTO eventDTO = new TicketEventDTO(eventType, ticket);

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME, 
            RabbitMQConfig.ROUTING_KEY, 
            eventDTO
        );
    }
}