package com.lexisware.portafolio.reports.controllers;

import com.lexisware.portafolio.reports.services.ReportServicePdf;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

// Controlador para la descarga de reportes administrativos en formato PDF
@RestController
@RequestMapping("/api/reports")
public class ReportPdfController {

    private final ReportServicePdf reportServicePdf;

    // Inicializa el controlador con el servicio de generación de PDFs
    public ReportPdfController(ReportServicePdf reportServicePdf) {
        this.reportServicePdf = reportServicePdf;
    }

    // Genera y descarga un reporte PDF con la lista de todos los programadores
    @GetMapping("/programmers/pdf")
    @SuppressWarnings("null")
    public ResponseEntity<InputStreamResource> downloadProgrammersReport() {
        ByteArrayInputStream bis = reportServicePdf.generateProgrammersPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=ReportePDF_Programadores.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    // Genera y descarga un reporte PDF con el historial de todas las asesorías
    @GetMapping("/advisories/pdf")
    public ResponseEntity<InputStreamResource> downloadAdvisoriesReport() {
        ByteArrayInputStream bis = reportServicePdf.generateAdvisoriesPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=ReportePDF_Asesorias.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    // Genera y descarga un reporte PDF con los proyectos de un usuario específico
    @GetMapping("/projects/{uid}/pdf")
    public ResponseEntity<InputStreamResource> downloadUserProjectsReport(
            @PathVariable("uid") String uid) {
        ByteArrayInputStream bis = reportServicePdf.generateUserProjectsPdf(uid);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=ReportePDF_Proyectos.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
