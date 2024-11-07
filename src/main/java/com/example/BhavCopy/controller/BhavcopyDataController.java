package com.example.BhavCopy.controller;

import com.example.BhavCopy.service.BhavcopyDataService;
import com.example.BhavCopy.entity.Jobq;
import com.example.BhavCopy.response.QueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bhavcopy")
public class BhavcopyDataController {

    @Autowired
    private BhavcopyDataService bhavcopyDataService;

   
    // Endpoint to check job status by reqid
    @GetMapping("/checkJobStatus/{reqid}")
    public Map<String, Object> checkJobStatus(@PathVariable String reqid) {
        Jobq result= bhavcopyDataService.checkJobStatus(reqid);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("data", result);
        return responseMap;
    }

    // Endpoint to handle dynamic queries
    @PostMapping("/query")
    public Map<String, Object> handleQuery(@RequestBody QueryRequest request) {
        Object response = bhavcopyDataService.handleQuery(request);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("requestId", response);
        return responseMap;
    }
}
