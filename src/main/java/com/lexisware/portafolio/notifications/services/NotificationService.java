package com.lexisware.portafolio.notifications.services;

import com.lexisware.portafolio.advisory.repositories.AdvisoryRepository;
import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import com.lexisware.portafolio.utils.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// Servicio de gestión de notificaciones y tareas programadas para recordatorios
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final AdvisoryRepository advisoryRepository;
    private final EmailService emailService;

    // Inicializa el servicio con los repositorios y el motor de envío de correos
    public NotificationService(AdvisoryRepository advisoryRepository, EmailService emailService) {
        this.advisoryRepository = advisoryRepository;
        this.emailService = emailService;
    }

    // Tarea diaria a las 9 AM para avisar sobre las asesorías del día siguiente
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendDailyReminders() {
        log.info("[CRON] Ejecutando recordatorios diarios de asesorías...");

        String tomorrow = LocalDate.now().plusDays(1).toString();
        List<AdvisoryEntity> advisories = advisoryRepository.findByDate(tomorrow);

        for (AdvisoryEntity advisory : advisories) {
            if (advisory.getStatus() == AdvisoryEntity.Status.approved) {
                // Notificación al programador
                try {
                    emailService.sendHtmlEmail(
                            advisory.getProgrammerEmail(),
                            "Recordatorio: Asesoría Mañana",
                            "Hola " + advisory.getProgrammerName() + ", tienes una asesoría programada para mañana con "
                                    + advisory.getRequesterName() + " a las " + advisory.getTime());
                    log.info("Recordatorio enviado a programador: {}", advisory.getProgrammerEmail());
                } catch (Exception e) {
                    log.error("Error enviando recordatorio a programador: {}", e.getMessage());
                }

                // Notificación al solicitante de la asesoría
                try {
                    emailService.sendHtmlEmail(
                            advisory.getRequesterEmail(),
                            "Recordatorio: Asesoría Mañana",
                            "Hola " + advisory.getRequesterName() + ", tienes una asesoría programada para mañana con "
                                    + advisory.getProgrammerName() + " a las " + advisory.getTime());
                    log.info("Recordatorio enviado a solicitante: {}", advisory.getRequesterEmail());
                } catch (Exception e) {
                    log.error("Error enviando recordatorio a solicitante: {}", e.getMessage());
                }
            }
        }
        log.info("[CRON] Recordatorios enviados con éxito. Total procesados: {}", advisories.size());
    }

    // Tarea ejecutada cada 15 minutos para recordar sesiones próximas (30 min
    // antes)
    @Scheduled(cron = "0 */15 * * * ?")
    public void checkUpcomingAdvisories() {
        log.info("[CRON] Verificando asesorías próximas para recordatorio (30 min antes)...");

        String today = LocalDate.now().toString();
        java.time.LocalTime now = java.time.LocalTime.now();

        List<AdvisoryEntity> advisories = advisoryRepository.findByDateAndStatusAndReminderSentFalse(
                today, AdvisoryEntity.Status.approved);

        int count = 0;
        for (AdvisoryEntity advisory : advisories) {
            try {
                java.time.LocalTime advisoryTime = java.time.LocalTime.parse(advisory.getTime());
                long minutesUntilStart = java.time.temporal.ChronoUnit.MINUTES.between(now, advisoryTime);

                // Verifica si la sesión inicia en la ventana de los próximos 30 minutos
                if (minutesUntilStart >= 0 && minutesUntilStart <= 30) {
                    sendReminderEmail(advisory);
                    advisory.setReminderSent(true);
                    advisoryRepository.save(advisory);
                    count++;
                }
            } catch (Exception e) {
                log.error("Error procesando recordatorio para asesoría ID {}: {}", advisory.getId(), e.getMessage());
            }
        }

        if (count > 0) {
            log.info("[CRON] Se enviaron {} recordatorios previos.", count);
        }
    }

    // Envía los correos de proximidad tanto al programador como al solicitante
    private void sendReminderEmail(AdvisoryEntity advisory) {
        try {
            emailService.sendHtmlEmail(
                    advisory.getProgrammerEmail(),
                    "Recordatorio: Asesoría en 30 minutos",
                    "Hola " + advisory.getProgrammerName() + ", tu asesoría con "
                            + advisory.getRequesterName() + " comienza en breve (" + advisory.getTime() + ").");
        } catch (Exception e) {
            log.error("Error enviando email programador: {}", e.getMessage());
        }

        try {
            emailService.sendHtmlEmail(
                    advisory.getRequesterEmail(),
                    "Recordatorio: Asesoría en 30 minutos",
                    "Hola " + advisory.getRequesterName() + ", tu asesoría con "
                            + advisory.getProgrammerName() + " comienza en breve (" + advisory.getTime() + ").");
        } catch (Exception e) {
            log.error("Error enviando email solicitante: {}", e.getMessage());
        }
    }

    // Facilita el envío puntual de correos electrónicos en formato HTML
    public void sendEmail(String to, String subject, String body) {
        emailService.sendHtmlEmail(to, subject, body);
    }
}
