import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JetDataCSVGenerator {
    public static void main(String[] args) {
        // Specify the full path to ensure file is created in the right location
        String csvFile = System.getProperty("user.dir") + File.separator + "jets_data.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            // Write header
            writer.append("model,type,seatCapacity,hourlyRate\n");

            // Write jet data - ensure no extra spaces
            writer.append("Gulfstream G650,Ultra Long Range,18,12000.0\n");
            writer.append("Cessna Citation X,Super Mid-Size,8,5500.0\n");
            writer.append("Embraer Phenom 300,Light Jet,6,3200.0\n");
            writer.append("Bombardier Global 7500,Ultra Long Range,14,15000.0\n");
            writer.append("Dassault Falcon 8X,Large Jet,16,9800.0\n");
            writer.append("Bombardier Challenger 350,Super Mid-Size,10,6200.0\n");
            writer.append("Gulfstream G450,Large Jet,12,10500.0\n");
            writer.append("Embraer Legacy 500,Mid-Size,9,4800.0\n");
            writer.append("Cessna Citation CJ4,Light Jet,7,3700.0\n");
            writer.append("Dassault Falcon 2000,Large Jet,14,8900.0\n");

            System.out.println("CSV file created successfully at: " + csvFile);
        } catch (IOException e) {
            System.out.println("Error creating CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}