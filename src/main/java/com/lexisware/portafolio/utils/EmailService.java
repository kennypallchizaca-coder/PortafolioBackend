package com.lexisware.portafolio.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

// Servicio encargado de la composición y envío asíncrono de notificaciones por correo electrónico
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    // Inicializa el servicio con el emisor de correos configurado en el sistema
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${mail.from}")
    private String fromEmail;

    @Value("${mail.from.name}")
    private String fromName;

    // Ejecuta el envío de correos en formato HTML de manera asíncrona para no
    // bloquear el hilo principal
    @Async
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email enviado a: {}", to);
        } catch (Exception e) {
            log.error("Error enviando email a: {}", to, e);
        }
    }

    // Remite un mensaje de bienvenida personalizado tras la creación de una nueva
    // cuenta
    public void sendWelcomeEmail(String to, String userName) {
        String subject = "¡Bienvenido a LEXISWARE Portafolio!";
        String htmlContent = """
                <html>
                    <body style='font-family: Arial, sans-serif;'>
                        <h2 style='color: #3B82F6;'>¡Bienvenido a LEXISWARE Portafolio!</h2>
                        <p>Hola <strong>%s</strong>,</p>
                        <p>Tu cuenta ha sido creada exitosamente.</p>
                        <p>Ahora puedes acceder a todas las funcionalidades de nuestra plataforma.</p>
                        <br/>
                        <p>Saludos,<br/>El equipo de LEXISWARE</p>
                    </body>
                </html>
                """.formatted(userName);

        sendHtmlEmail(to, subject, htmlContent);
    }

    // Notifica al programador sobre la recepción de una nueva solicitud de asesoría
    // técnica
    public void sendAdvisoryNotificationToProgrammer(
            String programmerEmail,
            String programmerName,
            String requesterName,
            String date,
            String time,
            String note) {
        String subject = "Nueva solicitud de asesoría";
        String htmlContent = """
                <html>
                    <body style='font-family: Arial, sans-serif;'>
                        <h2 style='color: #3B82F6;'>Nueva Solicitud de Asesoría</h2>
                        <p>Hola <strong>%s</strong>,</p>
                        <p>Has recibido una nueva solicitud de asesoría:</p>
                        <ul>
                            <li><strong>Solicitante:</strong> %s</li>
                            <li><strong>Fecha:</strong> %s</li>
                            <li><strong>Hora:</strong> %s</li>
                        </ul>
                        <p><strong>Mensaje:</strong></p>
                        <p style='background-color: #f3f4f6; padding: 15px; border-radius: 5px;'>
                            %s
                        </p>
                        <p>Ingresa a la plataforma para aceptar o rechazar la solicitud.</p>
                        <br/>
                        <p>Saludos,<br/>El equipo de LEXISWARE</p>
                    </body>
                </html>
                """.formatted(programmerName, requesterName, date, time, note);

        sendHtmlEmail(programmerEmail, subject, htmlContent);
    }

    // Envía una confirmación al solicitante indicando que su petición ha sido
    // enviada correctamente
    public void sendAdvisoryConfirmationToRequester(
            String requesterEmail,
            String requesterName,
            String programmerName,
            String date,
            String time) {
        String subject = "Solicitud de Asesoría Enviada";
        String htmlContent = """
                <html>
                    <body style='font-family: Arial, sans-serif;'>
                        <h2 style='color: #10B981;'>Solicitud de Asesoría Enviada</h2>
                        <p>Hola <strong>%s</strong>,</p>
                        <p>Tu solicitud de asesoría ha sido enviada correctamente:</p>
                        <ul>
                            <li><strong>Programador:</strong> %s</li>
                            <li><strong>Fecha:</strong> %s</li>
                            <li><strong>Hora:</strong> %s</li>
                        </ul>
                        <p>Recibirás una notificación cuando el programador responda.</p>
                        <br/>
                        <p>Saludos,<br/>El equipo de LEXISWARE</p>
                    </body>
                </html>
                """.formatted(requesterName, programmerName, date, time);

        sendHtmlEmail(requesterEmail, subject, htmlContent);
    }

    // Informa al usuario sobre la aprobación o el rechazo de su solicitud de
    // asesoría
    public void sendAdvisoryStatusUpdate(
            String requesterEmail,
            String requesterName,
            String status,
            String programmerName) {
        String statusColor = status.equals("approved") ? "#10B981" : "#EF4444";
        String statusText = status.equals("approved") ? "APROBADA" : "RECHAZADA";

        String subject = "Asesoría " + statusText;
        String htmlContent = """
                <html>
                    <body style='font-family: Arial, sans-serif;'>
                        <h2 style='color: %s;'>Asesoría %s</h2>
                        <p>Hola <strong>%s</strong>,</p>
                        <p>Tu solicitud de asesoría con <strong>%s</strong> ha sido <strong>%s</strong>.</p>
                        <p>Revisa la plataforma para más detalles.</p>
                        <br/>
                        <p>Saludos,<br/>El equipo de LEXISWARE</p>
                    </body>
                </html>
                """.formatted(statusColor, statusText, requesterName, programmerName, statusText);

        sendHtmlEmail(requesterEmail, subject, htmlContent);
    }
}
