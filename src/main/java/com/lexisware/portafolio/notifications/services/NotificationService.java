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

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final AdvisoryRepository advisoryRepository;
    private final EmailService emailService;

    public NotificationService(AdvisoryRepository advisoryRepository, EmailService emailService) {
        this.advisoryRepository = advisoryRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 9 * * ?") // 9 AM diario
    public void sendDailyReminders() {
        log.info("[CRON] Ejecutando recordatorios diarios de asesorías...");

        // Asesorías mañana
        String tomorrow = LocalDate.now().plusDays(1).toString();
        List<AdvisoryEntity> advisories = advisoryRepository.findByDate(tomorrow);

        for (AdvisoryEntity advisory : advisories) {
            if (advisory.getStatus() == AdvisoryEntity.Status.approved) {
                // Recordatorio programador
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

                // Recordatorio solicitante
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

    @Scheduled(cron = "0 */15 * * * ?") // Cada 15 minutos
    public void checkUpcomingAdvisories() {
        log.info("[CRON] Verificando asesorías próximas para recordatorio (30 min antes)...");

        // Fecha actual
        String today = LocalDate.now().toString();
        java.time.LocalTime now = java.time.LocalTime.now();

        // Busca asesorías de hoy aprobadas y sin recordatorio enviado
        List<AdvisoryEntity> advisories = advisoryRepository.findByDateAndStatusAndReminderSentFalse(
                today, AdvisoryEntity.Status.approved);

        int count = 0;
        for (AdvisoryEntity advisory : advisories) {
            try {
                // Parsea hora asesoría (Formato esperado HH:mm)
                java.time.LocalTime advisoryTime = java.time.LocalTime.parse(advisory.getTime());

                // Calcula diferencia en minutos
                long minutesUntilStart = java.time.temporal.ChronoUnit.MINUTES.between(now, advisoryTime);

                // Si falta entre 0 y 30 minutos (y no ha pasado más de 15 min del inicio, por
                // si acaso)
                if (minutesUntilStart >= 0 && minutesUntilStart <= 30) {
                    // Enviar notificaciones
                    sendReminderEmail(advisory);

                    // Marcar como enviado
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

    private void sendReminderEmail(AdvisoryEntity advisory) {
        // Recordatorio Programador
        try {
            emailService.sendHtmlEmail(
                    advisory.getProgrammerEmail(),
                    "Recordatorio: Asesoría en 30 minutos",
                    "Hola " + advisory.getProgrammerName() + ", tu asesoría con "
                            + advisory.getRequesterName() + " comienza en breve (" + advisory.getTime() + ").");
        } catch (Exception e) {
            log.error("Error enviando email programador: {}", e.getMessage());
        }

        // Recordatorio Solicitante
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

    // Envía email HTML
    public void sendEmail(String to, String subject, String body) {
        emailService.sendHtmlEmail(to, subject, body);
    }
}
