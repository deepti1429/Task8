package com.sample.loginform.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sample.loginform.Entity.Data;
import com.sample.loginform.services.DataService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DataController {
    @Autowired
    private DataService dataService;

    @PostMapping("/addRecord")
    @ResponseBody
    public Data PostDetails(@RequestBody Data data) {
    return	dataService.saveDetails(data);
    }
   
    @GetMapping("/record/{id}")
    public ResponseEntity<Data> getRecordById(@PathVariable long id) {
        Optional<Data> optionalRecord = dataService.getRecordByid(id);
        return optionalRecord.map(record -> new ResponseEntity<>(record, HttpStatus.OK))
                             .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/record/{id}")
    public ResponseEntity<Void> deleteRecordById(@PathVariable long id) {
        dataService.deleteRecordById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/record/{id}")
    public ResponseEntity<Data> updateRecord(@PathVariable long id, @RequestBody Data recordDetails) {
        Data updatedRecord = dataService.updateRecord(id, recordDetails);
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }
    
    @GetMapping("/data")
    public String getAllData(Model model) {
        List<Data> dataList = dataService.getAllData();
        model.addAttribute("dataList", dataList);
        return "data";
    }

    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        List<Data> dataList = dataService.getAllData();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"data.pdf\"");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        for (Data data : dataList) {
            table.addCell(data.getId().toString());
            table.addCell(data.getName());
            table.addCell(data.getValue());
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<Data> dataList = dataService.getAllData();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"data.xls\"");

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("DataEntity");

        int rowNum = 0;
        for (Data data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getId());
            row.createCell(1).setCellValue(data.getName());
            row.createCell(2).setCellValue(data.getValue());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Data> dataList = dataService.getAllData();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"data.csv\"");

        PrintWriter writer = response.getWriter();
        writer.println("ID,Name,Value");
        for (Data data : dataList) {
            writer.println(data.getId() + "," + data.getName() + "," + data.getValue());
        }
    }
}




