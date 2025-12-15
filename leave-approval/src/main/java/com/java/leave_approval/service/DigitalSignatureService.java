package com.java.leave_approval.service;

import org.springframework.stereotype.Service;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class DigitalSignatureService {
    
    private static final String ALGORITHM = "SHA256withRSA";
    
    /**
     * Generate a digital signature for the given data using the private key
     */
    public byte[] signDocument(byte[] documentHash, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance(ALGORITHM);
        signature.initSign(privateKey);
        signature.update(documentHash);
        return signature.sign();
    }
    
    /**
     * Verify a digital signature using the public key
     */
    public boolean verifySignature(byte[] documentHash, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance(ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(documentHash);
        return signature.verify(signatureBytes);
    }
    
    /**
     * Generate SHA-256 hash of document content
     */
    public byte[] generateDocumentHash(byte[] documentContent) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(documentContent);
    }
    
    /**
     * Generate RSA key pair for a user (called once during user creation)
     */
    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
    
    /**
     * Store private key to file (in production, use secure key storage)
     */
    public void storePrivateKey(PrivateKey privateKey, String filePath) throws IOException {
        byte[] keyBytes = privateKey.getEncoded();
        Files.write(Path.of(filePath), keyBytes);
    }
    
    /**
     * Store public key to file
     */
    public void storePublicKey(PublicKey publicKey, String filePath) throws IOException {
        byte[] keyBytes = publicKey.getEncoded();
        Files.write(Path.of(filePath), keyBytes);
    }
    
    /**
     * Load private key from file
     */
    public PrivateKey loadPrivateKey(String filePath) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Path.of(filePath));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }
    
    /**
     * Load public key from file
     */
    public PublicKey loadPublicKey(String filePath) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Path.of(filePath));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }
}
