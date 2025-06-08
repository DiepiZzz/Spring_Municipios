package com.example.demo.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping; 

import com.example.demo.Service.MunicipioService;
import com.example.demo.dto.PdfRequest;
import com.example.demo.model.Municipio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; 

@Controller
@RequestMapping("/graficas")
public class GraficasController {

    @Autowired
    private MunicipioService municipioService;

    @GetMapping
    public String showGraficas(Model model) {
        List<Municipio> municipios = municipioService.getAllMunicipios();

        List<String> nombresMunicipios = municipios.stream()
                                                .map(Municipio::getNombre)
                                                .collect(Collectors.toList());

        List<Integer> numHabitantes = municipios.stream()
                                                 .map(m -> m.getNumHabitantes() != null ? m.getNumHabitantes() : 0)
                                                 .collect(Collectors.toList());

        List<Integer> numCasas = municipios.stream()
                                            .map(m -> m.getNumCasas() != null ? m.getNumCasas() : 0)
                                            .collect(Collectors.toList());

        List<Integer> numParques = municipios.stream()
                                              .map(m -> m.getNumParques() != null ? m.getNumParques() : 0)
                                              .collect(Collectors.toList());

        List<Integer> numColegios = municipios.stream()
                                               .map(m -> m.getNumColegios() != null ? m.getNumColegios() : 0)
                                               .collect(Collectors.toList());

        
        List<Object> allData = new ArrayList<>();
        for (Municipio m : municipios) {
            allData.add(new Object() {
                public String getNombre() { return m.getNombre(); }
                public Integer getNumHabitantes() { return m.getNumHabitantes() != null ? m.getNumHabitantes() : 0; }
                public Integer getNumCasas() { return m.getNumCasas() != null ? m.getNumCasas() : 0; }
                public Integer getNumParques() { return m.getNumParques() != null ? m.getNumParques() : 0; }
                public Integer getNumColegios() { return m.getNumColegios() != null ? m.getNumColegios() : 0; }
            });
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            String nombresJson = mapper.writeValueAsString(nombresMunicipios);
            String habitantesJson = mapper.writeValueAsString(numHabitantes);
            String casasJson = mapper.writeValueAsString(numCasas);
            String parquesJson = mapper.writeValueAsString(numParques);
            String colegiosJson = mapper.writeValueAsString(numColegios);
            String allDataJson = mapper.writeValueAsString(allData);

            model.addAttribute("nombresMunicipiosJson", nombresJson);
            model.addAttribute("numHabitantesJson", habitantesJson);
            model.addAttribute("numCasasJson", casasJson);
            model.addAttribute("numParquesJson", parquesJson);
            model.addAttribute("numColegiosJson", colegiosJson);
            model.addAttribute("allDataJson", allDataJson);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error al procesar los datos para la gráfica.");
        }

        return "graficas";
    }

    @PostMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadPdf(@RequestBody PdfRequest pdfRequest) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PDDocument document = new PDDocument(); 

        try {
            
            PDRectangle pageSize = PDRectangle.A4;
            float margin = 50; 
            float contentWidth = pageSize.getWidth() - 2 * margin;
            float contentHeight = pageSize.getHeight() - 2 * margin;

            
            PDPage titlePage = new PDPage(pageSize);
            document.addPage(titlePage);
            PDPageContentStream contentStream = new PDPageContentStream(document, titlePage);

            contentStream.beginText();
            
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
            contentStream.newLineAtOffset(margin, pageSize.getHeight() - margin);
            contentStream.showText("Informe de Datos de Municipios");
            contentStream.endText();

            contentStream.beginText();
            
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.newLineAtOffset(margin, pageSize.getHeight() - margin - 30);
            
            String message = pdfRequest.getMessage();
            int lineLength = 70; 
            float yPosition = pageSize.getHeight() - margin - 50;

            for (String line : message.split("(?<=\\G.{" + lineLength + "})")) { 
                contentStream.showText(line.trim());
                yPosition -= 15; 
                contentStream.newLineAtOffset(0, -15); 
            }
            contentStream.endText();
            contentStream.close();


            
            for (PdfRequest.ChartData chartData : pdfRequest.getCharts()) {
                PDPage chartPage = new PDPage(pageSize); 
                document.addPage(chartPage);
                PDPageContentStream chartContentStream = new PDPageContentStream(document, chartPage);

                try {
                    
                    String base64Image = chartData.getDataUrl().split(",")[1];
                    byte[] imageBytes = Base64.getDecoder().decode(base64Image);

                    
                    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
                    
                    
                    PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);

                    
                    chartContentStream.beginText();
                    
                    chartContentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
                    chartContentStream.newLineAtOffset(margin, pageSize.getHeight() - margin);
                    chartContentStream.showText(chartData.getTitle());
                    chartContentStream.endText();

                    
                    float imageWidth = pdImage.getWidth();
                    float imageHeight = pdImage.getHeight();

                    
                    float scale = 1;
                    if (imageWidth > contentWidth) {
                        scale = contentWidth / imageWidth;
                    }
                    if (imageHeight * scale > contentHeight - 50) { 
                        scale = (contentHeight - 50) / imageHeight;
                    }

                    float finalImageWidth = imageWidth * scale;
                    float finalImageHeight = imageHeight * scale;

                    float x = (pageSize.getWidth() - finalImageWidth) / 2; 
                    float y = (pageSize.getHeight() - finalImageHeight) / 2 - 20; 

                    chartContentStream.drawImage(pdImage, x, y, finalImageWidth, finalImageHeight);

                } catch (Exception e) {
                    System.err.println("Error al procesar la imagen de la gráfica: " + chartData.getTitle() + " - " + e.getMessage());
                    chartContentStream.beginText();
                    
                    chartContentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
                    chartContentStream.setNonStrokingColor(255, 0, 0); 
                    chartContentStream.newLineAtOffset(margin, pageSize.getHeight() - margin - 50);
                    chartContentStream.showText("Error al cargar la gráfica: " + chartData.getTitle());
                    chartContentStream.endText();
                } finally {
                    chartContentStream.close();
                }
            }

            document.save(baos); 

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error general al generar el PDF con PDFBox: " + e.getMessage(), e);
        } finally {
            if (document != null) {
                document.close(); 
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "informe_municipios.pdf");
        headers.setContentLength(baos.size());

        return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
    }
}