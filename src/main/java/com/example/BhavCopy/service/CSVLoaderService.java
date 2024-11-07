package com.example.BhavCopy.service;

import com.example.BhavCopy.entity.BhavcopyData;
import com.example.BhavCopy.repository.BhavcopyDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVLoaderService {
    
    private static final Logger logger = LoggerFactory.getLogger(CSVLoaderService.class);
    
    @Autowired
    private BhavcopyDataRepository bhavcopyDataRepository;

    public void loadCSV(MultipartFile file) {
        List<BhavcopyData> dataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            reader.readLine(); // Skip header row

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 13) {
                    logger.warn("Skipping line due to insufficient values: {}", line);
                    continue; // Ensure that all required values are present
                }

                BhavcopyData data = new BhavcopyData(
                        values[0].trim(), // symbol
                        values[1].trim(), // series
                        parseDouble(values[2].trim()), // open
                        parseDouble(values[3].trim()), // high
                        parseDouble(values[4].trim()), // low
                        parseDouble(values[5].trim()), // close
                        parseDouble(values[6].trim()), // last
                        parseDouble(values[7].trim()), // prevClose
                        parseLong(values[8].trim()), // totTrdQty
                        parseDouble(values[9].trim()), // totTrdVal
                        values[10].trim(), // timestamp
                        parseInt(values[11].trim()), // totalTrades
                        values[12].trim() // isin
                );
                
                dataList.add(data);
            }

            // Save all data at once for better performance
            if (!dataList.isEmpty()) {
                bhavcopyDataRepository.saveAll(dataList);
                logger.info("CSV data loaded successfully.");
            } else {
                logger.warn("No valid data found to load.");
            }
        } catch (Exception e) {
            logger.error("Error while loading CSV data: {}", e.getMessage());
        }
    }

    private Double parseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            logger.warn("Failed to parse double value: {}", value);
            return null;
        }
    }

    private Integer parseInt(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Failed to parse integer value: {}", value);
            return null;
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            logger.warn("Failed to parse long value: {}", value);
            return null;
        }
    }
}
