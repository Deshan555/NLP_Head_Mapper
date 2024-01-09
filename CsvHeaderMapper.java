package com.eimsky.godzilla.v01;


import org.apache.commons.text.similarity.JaccardSimilarity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvHeaderMapper {

    public static void main(String[] args) {
        try {
            // Step 1: Read CSV file and get header names
            List<String> csvHeaders = readCsvHeaders("data.csv");
            /*
            [
  "Location_ID",
  "Location_Name",
  "Geo_Latitude",
  "Geo_Longitude",
  "Energy_Consumption",
  "Fuel_Consumption",
  "Power_Generator_Type",
  "Sun_Availability"
]
             */

            // Step 2: Predefined set of header values
            List<String> predefinedHeaders = Arrays.asList(
                    "Location ID",
                    "Location Name",
                    "Latitude",
                    "Longitude",
                    "Energy Consumption",
                    "Fuel Consumption",
                    "Power Generator Type",
                    "Sun Availability"
            );

            // Step 3: Map CSV headers based on similarity to predefined set using NLP
            Map<String, String> headerMapping = mapHeaders(csvHeaders, predefinedHeaders);

            // Step 4: Print or store the mapped headers
            System.out.println("Header Mapping:");
            for (Map.Entry<String, String> entry : headerMapping.entrySet()) {
                System.out.println(entry.getKey() + " => " + entry.getValue());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readCsvHeaders(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine();
        reader.close();

        // Assuming headers are comma-separated
        return Arrays.asList(line.split(","));
    }

    private static Map<String, String> mapHeaders(List<String> csvHeaders, List<String> predefinedHeaders) {
        Map<String, String> headerMapping = new HashMap<>();

        // Use similarity calculation (e.g., Jaccard Similarity from OpenNLP)
        JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
        
        for (String csvHeader : csvHeaders) {
            double maxSimilarity = 0;
            String mappedHeader = "";
            for (String predefinedHeader : predefinedHeaders) {
                double similarity = jaccardSimilarity.apply(csvHeader, predefinedHeader);
                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                    mappedHeader = predefinedHeader;
                }
            }
            headerMapping.put(csvHeader, mappedHeader);
        }
        return headerMapping;
    }
}
