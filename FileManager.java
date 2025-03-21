import java.io.*;

public class FileManager {
    public static boolean loadJetsFromCSV(JetInventory jetInventory) {
        String csvFile = "jets_data.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip header
            String headerLine = br.readLine();

            String line;
            int jetCount = 0;
            while ((line = br.readLine()) != null) {
                String[] jetData = line.split(",");
                if (jetData.length >= 4) {
                    try {
                        Jet jet = new Jet(
                                jetData[0].trim(),                // model
                                jetData[1].trim(),                // type
                                Integer.parseInt(jetData[2].trim()),   // seat capacity
                                true,                           // availability
                                Double.parseDouble(jetData[3].trim())  // hourly rate
                        );
                        jetInventory.addJet(jet);
                        jetCount++;
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing jet data: " + line);
                    }
                }
            }

            System.out.println("CSV file successfully loaded. Total jets: " + jetCount);
            return true;
        } catch (IOException e) {
            System.out.println("Error reading jet data from CSV: " + e.getMessage());
            return false;
        }
    }
}
