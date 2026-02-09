package com.lexisware.portafolio.reports.services;

import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.users.repositories.UserRepository;
import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import com.lexisware.portafolio.advisory.repositories.AdvisoryRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lexisware.portafolio.project.repositories.ProjectRepository;
import com.lexisware.portafolio.project.entities.ProjectEntity;

// Servicio especializado en la generación dinámica de documentos PDF corporativos
@Service
public class ReportServicePdf {

    // Paleta de colores profesional para el diseño minimalista de los PDFs
    private static final Color PRIMARY_COLOR = new Color(71, 85, 105);
    private static final Color SECONDARY_COLOR = new Color(30, 41, 59);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color WARNING_COLOR = new Color(245, 158, 11);
    private static final Color DANGER_COLOR = new Color(220, 38, 38);
    private static final Color HEADER_BG = new Color(15, 23, 42);
    private static final Color SUBTITLE_COLOR = new Color(148, 163, 184);
    private static final Color BORDER_COLOR = new Color(203, 213, 225);

    private final UserRepository userRepository;
    private final AdvisoryRepository advisoryRepository;
    private final ProjectRepository projectRepository;

    // Inyecta los repositorios necesarios para extraer los datos de los reportes
    public ReportServicePdf(UserRepository userRepository, AdvisoryRepository advisoryRepository,
            ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.advisoryRepository = advisoryRepository;
        this.projectRepository = projectRepository;
    }

    // Genera un documento PDF con el listado de proyectos de un usuario específico
    public ByteArrayInputStream generateUserProjectsPdf(String userUid) {
        Document document = new Document(PageSize.A4, 36, 36, 60, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Configura el encabezado común para el reporte de proyectos
            addReportHeader(document, "REPORTE DE PROYECTOS");

            UserEntity user = userRepository.findById(userUid).orElse(null);
            String userName = (user != null) ? user.getDisplayName() : "Usuario Desconocido";

            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.DARK_GRAY);
            Paragraph userInfo = new Paragraph("Programador: " + userName, infoFont);
            userInfo.setAlignment(Element.ALIGN_LEFT);
            userInfo.setSpacingAfter(10);
            document.add(userInfo);

            // Estructura la tabla de contenidos para mostrar los detalles del proyecto
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 3, 5, 3, 2 });
            table.setSpacingBefore(10);

            addStyledTableHeader(table, "Título");
            addStyledTableHeader(table, "Descripción");
            addStyledTableHeader(table, "Tecnologías");
            addStyledTableHeader(table, "Estado");

            List<ProjectEntity> projects = projectRepository
                    .findByOwner_Uid(userUid, org.springframework.data.domain.Pageable.unpaged()).getContent();

            if (projects.isEmpty()) {
                PdfPCell emptyCell = new PdfPCell(new Phrase("No hay proyectos registrados",
                        FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC, Color.GRAY)));
                emptyCell.setColspan(4);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell.setPadding(15);
                table.addCell(emptyCell);
            } else {
                for (ProjectEntity project : projects) {
                    addStyledCell(table, project.getTitle());
                    addStyledCell(table,
                            project.getDescription() != null ? project.getDescription() : "Sin descripción");
                    String stack = (project.getTechStack() != null) ? String.join(", ", project.getTechStack()) : "N/A";
                    addStyledCell(table, stack);

                    PdfPCell statusCell = new PdfPCell(new Phrase("Activo",
                            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE)));
                    statusCell.setBackgroundColor(SUCCESS_COLOR);
                    statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    statusCell.setPadding(5);
                    table.addCell(statusCell);
                }
            }

            document.add(table);
            addFooter(document, projects.size());
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Crea un reporte PDF que lista a todos los programadores activos en el sistema
    public ByteArrayInputStream generateProgrammersPdf() {
        Document document = new Document(PageSize.A4, 36, 36, 60, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            addReportHeader(document, "REPORTE DE PROGRAMADORES");

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 3, 4, 3, 2 });
            table.setSpacingBefore(15);

            addStyledTableHeader(table, "Nombre Completo");
            addStyledTableHeader(table, "Email Corporativo");
            addStyledTableHeader(table, "Especialidad");
            addStyledTableHeader(table, "Estado");

            List<UserEntity> programmers = userRepository.findByRole(UserEntity.Role.PROGRAMMER);

            for (UserEntity user : programmers) {
                addStyledCell(table, user.getDisplayName() != null ? user.getDisplayName() : "N/A");
                addStyledCell(table, user.getEmail());
                addStyledCell(table, user.getSpecialty() != null ? user.getSpecialty() : "General");

                boolean isAvailable = user.getAvailable() != null && user.getAvailable();
                PdfPCell activeCell = new PdfPCell(new Phrase(isAvailable ? "Disponible" : "No Disponible",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE)));
                activeCell.setBackgroundColor(isAvailable ? SUCCESS_COLOR : Color.GRAY);
                activeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                activeCell.setPadding(5);
                table.addCell(activeCell);
            }

            document.add(table);
            addFooter(document, programmers.size());
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Genera un reporte PDF consolidado con el estado de todas las asesorías
    // programadas
    public ByteArrayInputStream generateAdvisoriesPdf() {
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 60, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            addReportHeader(document, "REPORTE DE ASESORÍAS");

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 3, 3, 2, 2, 3, 2 });
            table.setSpacingBefore(15);

            addStyledTableHeader(table, "Solicitante");
            addStyledTableHeader(table, "Programador Asignado");
            addStyledTableHeader(table, "Fecha");
            addStyledTableHeader(table, "Hora");
            addStyledTableHeader(table, "Motivo");
            addStyledTableHeader(table, "Estado");

            List<AdvisoryEntity> advisories = advisoryRepository.findAll();

            for (AdvisoryEntity adv : advisories) {
                addStyledCell(table, adv.getRequesterName() != null ? adv.getRequesterName() : "Anónimo");
                addStyledCell(table,
                        adv.getProgrammer() != null ? adv.getProgrammer().getDisplayName() : "Sin asignar");
                addStyledCell(table, adv.getDate() != null ? adv.getDate().toString() : "N/A");
                addStyledCell(table, adv.getTime() != null ? adv.getTime() : "N/A");
                addStyledCell(table,
                        adv.getNote() != null
                                ? (adv.getNote().length() > 40 ? adv.getNote().substring(0, 37) + "..." : adv.getNote())
                                : "Sin motivo");

                PdfPCell statusCell = new PdfPCell();
                String statusText = "";
                Color statusColor = Color.GRAY;

                if (AdvisoryEntity.Status.approved == adv.getStatus()) {
                    statusText = "✓ Aprobada";
                    statusColor = SUCCESS_COLOR;
                } else if (AdvisoryEntity.Status.rejected == adv.getStatus()) {
                    statusText = "✗ Rechazada";
                    statusColor = DANGER_COLOR;
                } else {
                    statusText = "⧗ Pendiente";
                    statusColor = WARNING_COLOR;
                }

                statusCell.setPhrase(new Phrase(statusText,
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE)));
                statusCell.setBackgroundColor(statusColor);
                statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                statusCell.setPadding(5);
                table.addCell(statusCell);
            }

            document.add(table);
            addFooter(document, advisories.size());
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Agrega los elementos visuales del encabezado (banners, títulos y fechas) al
    // documento
    private void addReportHeader(Document document, String title) throws DocumentException {
        PdfPTable bannerTop = new PdfPTable(1);
        bannerTop.setWidthPercentage(100);
        PdfPCell bannerCell1 = new PdfPCell();
        bannerCell1.setBackgroundColor(SECONDARY_COLOR);
        bannerCell1.setFixedHeight(2);
        bannerCell1.setBorder(Rectangle.NO_BORDER);
        bannerTop.addCell(bannerCell1);
        bannerTop.setSpacingAfter(0);
        document.add(bannerTop);

        PdfPTable bannerMid = new PdfPTable(1);
        bannerMid.setWidthPercentage(100);
        PdfPCell bannerCell2 = new PdfPCell();
        bannerCell2.setBackgroundColor(PRIMARY_COLOR);
        bannerCell2.setFixedHeight(3);
        bannerCell2.setBorder(Rectangle.NO_BORDER);
        bannerMid.addCell(bannerCell2);
        bannerMid.setSpacingAfter(15);
        document.add(bannerMid);

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, PRIMARY_COLOR);
        Paragraph titleParagraph = new Paragraph(title, titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingAfter(8);
        document.add(titleParagraph);

        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, SUBTITLE_COLOR);
        Paragraph subtitle = new Paragraph("LEXISWARE - Sistema de Gestión de Portafolios", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(5);
        document.add(subtitle);

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.ITALIC, new Color(100, 116, 139));
        Paragraph dateParagraph = new Paragraph("📄 Generado el: " + currentDate, dateFont);
        dateParagraph.setAlignment(Element.ALIGN_CENTER);
        dateParagraph.setSpacingAfter(15);
        document.add(dateParagraph);

        PdfPTable line1 = new PdfPTable(1);
        line1.setWidthPercentage(100);
        PdfPCell lineCell1 = new PdfPCell();
        lineCell1.setBackgroundColor(PRIMARY_COLOR);
        lineCell1.setFixedHeight(2);
        lineCell1.setBorder(Rectangle.NO_BORDER);
        line1.addCell(lineCell1);
        line1.setSpacingAfter(2);
        document.add(line1);

        PdfPTable line2 = new PdfPTable(1);
        line2.setWidthPercentage(90);
        PdfPCell lineCell2 = new PdfPCell();
        lineCell2.setBackgroundColor(SECONDARY_COLOR);
        lineCell2.setFixedHeight(1);
        lineCell2.setBorder(Rectangle.NO_BORDER);
        line2.addCell(lineCell2);
        line2.setSpacingAfter(12);
        document.add(line2);
    }

    // Configura el estilo visual de las celdas de encabezado de las tablas
    private void addStyledTableHeader(PdfPTable table, String headerTitle) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(HEADER_BG);
        header.setPhrase(new Phrase(headerTitle,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE)));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setPadding(8);
        header.setBorderWidth(0);
        table.addCell(header);
    }

    // Configura el estilo visual y bordes de las celdas de datos en las tablas
    private void addStyledCell(PdfPTable table, String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content,
                FontFactory.getFont(FontFactory.HELVETICA, 9, Color.DARK_GRAY)));
        cell.setPadding(6);
        cell.setBorderColor(BORDER_COLOR);
        cell.setBorderWidth(0.5f);
        table.addCell(cell);
    }

    // Agrega el resumen de totales al final del documento PDF
    private void addFooter(Document document, int totalRecords) throws DocumentException {
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, SECONDARY_COLOR);
        Paragraph footer = new Paragraph("\nTotal de registros: " + totalRecords, footerFont);
        footer.setAlignment(Element.ALIGN_RIGHT);
        footer.setSpacingBefore(15);
        document.add(footer);
    }
}
