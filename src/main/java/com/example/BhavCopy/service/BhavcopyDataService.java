package com.example.BhavCopy.service;

import com.example.BhavCopy.entity.Jobq;
import com.example.BhavCopy.entity.BhavcopyData;
import com.example.BhavCopy.repository.BhavcopyDataRepository;
import com.example.BhavCopy.repository.JobqRepository;
import com.example.BhavCopy.response.QueryRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BhavcopyDataService {

    @Autowired
    private BhavcopyDataRepository bhavcopyDataRepository;

    @Autowired
    private JobqRepository jobqRepository;

    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(BhavcopyDataService.class);

    // Existing methods for querying data
    public List<BhavcopyData> getSymbolInfo(String symbol) {
        String symbolInput = symbol.trim().toUpperCase();
        System.out.println("Querying for symbol: " + symbolInput);
        return bhavcopyDataRepository.findBySymbolUsingProcedure(symbolInput);
    }

    public Long countSymbolsBySeries(String series) {
        String seriesInput = series.trim().toUpperCase();
        return bhavcopyDataRepository.countBySeries(seriesInput);
    }

    public List<String> getSymbolsWithGainGreaterThan(Double gainPercentage) {
        return bhavcopyDataRepository.findSymbolsWithGainGreaterThan(gainPercentage);
    }

    public List<String> getSymbolsWithHighLowGreaterThan(Double threshold) {
        return bhavcopyDataRepository.findSymbolsWithHighLowGreaterThan(threshold);
    }

    public Double calculateStandardDeviation(String series) {
        return bhavcopyDataRepository.calculateStandardDeviation(series);
    }

    public List<String> findTopNGainSymbols(int n) {
        return bhavcopyDataRepository.findTopNGainSymbols(n);
    }

    public List<String> findBottomNLaggards(int n) {
        return bhavcopyDataRepository.findBottomNLaggards(n);
    }

    public List<String> findTopNTradedSymbols(int n) {
        return bhavcopyDataRepository.findTopNTradedSymbols(n);
    }

    public List<String> findBottomNTradedSymbols(int n) {
        return bhavcopyDataRepository.findBottomNTradedSymbols(n);
    }

    @SuppressWarnings("unchecked")
    public List<String> findHighLowTradedSymbols(String series) {
        List<Object[]> result = bhavcopyDataRepository.findHighLowTradedSymbols(series);
        Map<String, String> response = new HashMap<>();

        if (!result.isEmpty()) {
            Object[] symbols = result.get(0);
            String highSymbol = (String) symbols[0];
            String lowSymbol = (String) symbols[1];
            response.put("highSymbol", highSymbol);
            response.put("lowSymbol", lowSymbol);
        }
        return (List<String>) response;
    }

    // Job Queue Handling

    private String generateReqid() {
        return "REQ-" + UUID.randomUUID().toString();
    }

    public String startJob(Map<String, Object> params) {
        String reqid = generateReqid(); // Generate a request ID for the job
        String paramsJson = convertToJson(params);
        JsonNode param = null;
        try {
            param = objectMapper.readTree(paramsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Jobq jobq = new Jobq();
        jobq.setReqid(reqid);
        jobq.setParams(param);
        jobq.setStatus("pending");
        jobq.setAddedAt(java.time.LocalDateTime.now());
        jobq.setStartedAt(jobq.getAddedAt());

        jobqRepository.save(jobq);
        return reqid;
    }

    private String convertToJson(Map<String, Object> params) {
        try {
            return objectMapper.writeValueAsString(params); // Convert the map to a JSON string
        } catch (Exception e) {
            logger.error("Error while converting params to JSON: {}", e.getMessage());
            throw new RuntimeException("Failed to convert params to JSON", e); // Handle exception
        }
    }

    public Jobq checkJobStatus(String reqid) {
        Jobq jobq = jobqRepository.findByReqid(reqid);
        Map<String, String> response = new HashMap<>();

        if (jobq != null) {
            response.put("status", jobq.getStatus());
            if ("done".equals(jobq.getStatus())) {
                response.put("result", jobq.getResponse().toString());
            }
        } else {
            response.put("status", "not_found");
            response.put("message", "Job with reqid " + reqid + " not found");
        }
        return jobq;
    }

    public void updateJobStatus(String reqid, String status, JsonNode result) {
        Jobq jobq = jobqRepository.findByReqid(reqid);
        if (jobq != null) {
            jobq.setStatus(status);
            jobq.setResponse(result);
            jobq.setStartedAt(java.time.LocalDateTime.now());
    
            int sleepTime = ThreadLocalRandom.current().nextInt(10, 20);
            try {
                Thread.sleep(sleepTime * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
    
            jobq.setEndedAt(LocalDateTime.now());
    
            // Calculate the duration between startedAt and endedAt
            Duration duration = Duration.between(jobq.getStartedAt(), jobq.getEndedAt());
            jobq.setDuration(duration.toSeconds());
    
            jobqRepository.save(jobq);
        }
    }
    

    public String handleQuery(QueryRequest request) {
        String operation = request.getOperation();
        Map<String, Object> params = request.getParameters();
        String id = startJob(params);
        Object result = null;

        // Process the operation and store the output in result
        switch (operation) {
            case "checkJobStatus":
                String reqid = (String) params.get("reqid");
                result = checkJobStatus(reqid);
                break;
            case "getSymbolInfo":
                String symbol = (String) params.get("symbol");
                result = getSymbolInfo(symbol);
                break;
            case "countSymbolsBySeries":
                String series = (String) params.get("series");
                result = countSymbolsBySeries(series);
                break;
            case "getSymbolsWithGainGreaterThan":
                Double gainPercentage = (Double) params.get("gainPercentage");
                result = getSymbolsWithGainGreaterThan(gainPercentage);
                break;
            case "getSymbolsWithHighLowGreaterThan":
                Double threshold = (Double) params.get("threshold");
                result = getSymbolsWithHighLowGreaterThan(threshold);
                break;
            case "calculateStandardDeviation":
                series = (String) params.get("series");
                result = calculateStandardDeviation(series);
                break;
            case "findTopNGainSymbols":
                Integer n = (Integer) params.get("n");
                result = findTopNGainSymbols(n);
                break;
            case "findBottomNLaggards":
                n = (Integer) params.get("n");
                result = findBottomNLaggards(n);
                break;
            case "findTopNTradedSymbols":
                n = (Integer) params.get("n");
                result = findTopNTradedSymbols(n);
                break;
            case "findBottomNTradedSymbols":
                n = (Integer) params.get("n");
                result = findBottomNTradedSymbols(n);
                break;
            case "findHighLowTradedSymbols":
                series = (String) params.get("series");
                result = findHighLowTradedSymbols(series);
                break;
            default:
                throw new IllegalArgumentException("Invalid operation type");
        }

        // Convert result to JsonNode
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode resultJsonNode;
        try {
            resultJsonNode = objectMapper.valueToTree(result);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert result to JsonNode", e);
        }

        updateJobStatus(id, "Done", resultJsonNode); // Update job status with JsonNode result
        return id;
    }

}
