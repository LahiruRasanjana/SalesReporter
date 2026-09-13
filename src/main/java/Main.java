import io.CsvSalesReader;
import model.ProductSale;
import output.ConsoleOutput;
import output.FileOutput;
import output.OutputStrategy;
import report.SalesReportGenerator;
import service.SalesReportService;

import java.io.IOException;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        String csvFilePath = args[0];
        String outputMethod = args[1].toLowerCase();
        String outputFilePath = args.length > 2 ? args[2] : null;

        try {
            CsvSalesReader reader = new CsvSalesReader();
            List<ProductSale> sales = reader.read(csvFilePath);
            if (sales.isEmpty()) {
                System.err.println("Error: CSV file contains no data.");
                return;
            }
            SalesReportService service = new SalesReportService();
            SalesReportGenerator generator = new SalesReportGenerator(service);
            String report = generator.generate(sales);
            OutputStrategy output;

            if ("console".equals(outputMethod)) {
                output = new ConsoleOutput();
            } else if ("file".equals(outputMethod)) {
                output = new FileOutput(outputFilePath);
            } else {
                System.err.println("Error: Invalid output method. Use 'console' or 'file'.");
                return;
            }

            output.writeReport(report);
            if ("file".equals(outputMethod)) {
                System.out.println("Report successfully saved to " + outputFilePath);
            }
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid numeric value in CSV file.");
        }
    }
}