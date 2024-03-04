package Controllers.Art;

import Models.art;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import Services.Art.ArtServices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ExcelHandler {
    ArtServices artService = new ArtServices();
    // Méthode pour lire un fichier Excel
    public void readExcelFile(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

        // Iterate over rows
        for (Row row : sheet) {
            // Iterate over cells
            for (Cell cell : row) {
                System.out.print(cell.toString() + "\t");
            }
            System.out.println();
        }

        workbook.close();
        fileInputStream.close();
    }

    // Méthode pour écrire dans un fichier Excel
    public void writeExcelFile(String filePath) throws IOException {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("art");

            // Création de l'en-tête
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Id_art", "Title", "Materials", "Height", "Width", "Type" , "City" , "Description" , "Price"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                // Appliquer le style au titre
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }
            ArtServices artServices = new ArtServices();
            // Remplissage des données
            List<art> artList = artServices.display();
            for (int i = 0; i < artList.size(); i++) {
                art art = artList.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(art.getId_art());
                row.createCell(1).setCellValue(art.getTitle());
                row.createCell(2).setCellValue(art.getMaterials());
                row.createCell(3).setCellValue(art.getHeight());
                row.createCell(4).setCellValue(art.getWidth());
                row.createCell(5).setCellValue(art.getType());
                row.createCell(6).setCellValue(art.getCity());
                row.createCell(7).setCellValue(art.getDescription());
                row.createCell(8).setCellValue(art.getPrice());
            }

            // Redimensionner automatiquement les colonnes pour s'adapter au contenu
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Sauvegarde du fichier Excel
            FileOutputStream fileOut = new FileOutputStream("Art.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}