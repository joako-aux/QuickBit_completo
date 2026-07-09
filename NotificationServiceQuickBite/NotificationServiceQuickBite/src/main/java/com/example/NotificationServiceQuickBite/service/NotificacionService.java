package com.example.NotificationServiceQuickBite.service;

import com.example.NotificationServiceQuickBite.Modelo.Notificacion;
import com.example.NotificationServiceQuickBite.dto.NotificacionDto;
import com.example.NotificationServiceQuickBite.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificacionService {

    // Instancia del logger para la clase NotificacionService
    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    @Autowired
    private NotificacionRepository repository;

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmail(NotificacionDto dto) {
        log.info("Preparando envío de email para el usuario ID: {} (Destinatario: {})",
                dto.getUsuarioId(), dto.getDestinatarioEmail());

        Notificacion notificacion = Notificacion.builder()
                .usuarioId(dto.getUsuarioId())
                .destinatarioEmail(dto.getDestinatarioEmail())
                .asunto(dto.getAsunto())
                .mensaje(dto.getMensaje())
                .fechaEnvio(LocalDateTime.now())
                .build();

        try {
            log.debug("Configurando mensaje de correo. Asunto: '{}'", dto.getAsunto());

            // Configurar y mandar el mail real
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(dto.getDestinatarioEmail());
            message.setSubject(dto.getAsunto());
            message.setText(dto.getMensaje());

            log.debug("Intentando conectar con el servidor SMTP para enviar el correo...");
            mailSender.send(message);

            notificacion.setEstado("ENVIADO");
            log.info("Email enviado exitosamente a: {}", dto.getDestinatarioEmail());

        } catch (Exception e) {
            notificacion.setEstado("FALLIDO");
            // Usamos log.error pasando la excepción 'e' al final para que guarde el stack trace completo si es necesario
            log.error("Error crítico al enviar email a {}. Motivo: {}", dto.getDestinatarioEmail(), e.getMessage(), e);
        }

        // Persistir en base de datos local
        try {
            repository.save(notificacion);
            log.debug("Registro de notificación guardado en la base de datos con estado: {}", notificacion.getEstado());
        } catch (Exception dbEx) {
            log.error("No se pudo persistir el estado de la notificación en la base de datos", dbEx);
        }
    }
}