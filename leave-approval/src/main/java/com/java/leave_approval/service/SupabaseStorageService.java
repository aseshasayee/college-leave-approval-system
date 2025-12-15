package com.java.leave_approval.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
public class SupabaseStorageService {
    
    @Value("${supabase.storage.url}")
    private String storageUrl;
    
    @Value("${supabase.storage.bucket}")
    private String bucket;
    
    @Value("${supabase.storage.anon-key}")
    private String anonKey;
    
    private final HttpClient httpClient = HttpClient.newHttpClient();
    
    /**
     * Upload file to Supabase Storage
     * Returns the public URL of the uploaded file
     */
    public String uploadFile(MultipartFile file) throws IOException, InterruptedException {
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String uploadPath = storageUrl + "/object/" + bucket + "/" + filename;
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uploadPath))
            .header("Authorization", "Bearer " + anonKey)
            .header("Content-Type", file.getContentType())
            .POST(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            // Return public URL
            return storageUrl + "/object/public/" + bucket + "/" + filename;
        } else {
            throw new RuntimeException("Failed to upload file to Supabase: " + response.body());
        }
    }
    
    /**
     * Download file content from Supabase Storage for signature generation
     */
    public byte[] downloadFile(String fileUrl) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(fileUrl))
            .header("Authorization", "Bearer " + anonKey)
            .GET()
            .build();
        
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("Failed to download file from Supabase");
        }
    }
    
    /**
     * Upload text message as file to Supabase
     */
    public String uploadTextMessage(String message, Long studentId) throws IOException, InterruptedException {
        String filename = UUID.randomUUID().toString() + "_student_" + studentId + ".txt";
        String uploadPath = storageUrl + "/object/" + bucket + "/" + filename;
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uploadPath))
            .header("Authorization", "Bearer " + anonKey)
            .header("Content-Type", "text/plain")
            .POST(HttpRequest.BodyPublishers.ofString(message))
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return storageUrl + "/object/public/" + bucket + "/" + filename;
        } else {
            throw new RuntimeException("Failed to upload message to Supabase: " + response.body());
        }
    }
}
