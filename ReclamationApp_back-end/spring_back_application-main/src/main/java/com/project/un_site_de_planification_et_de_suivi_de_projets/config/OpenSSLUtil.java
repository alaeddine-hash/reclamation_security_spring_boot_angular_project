package com.project.un_site_de_planification_et_de_suivi_de_projets.config;
import java.io.*;
import java.nio.file.Files;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

@Component
public class OpenSSLUtil {
    public static String encryptWithSymmetricKey(String plaintext, String symmetricKeyPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "openssl", "enc", "-aes-256-cbc", "-a", "-salt", "-in", "-", "-out", "-",
                "-pass", "file:" + symmetricKeyPath);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (var os = process.getOutputStream();
             var is = process.getInputStream()) {
            os.write(plaintext.getBytes());
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder encryptedData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                encryptedData.append(line);
            }
            String[] parts = encryptedData.toString().split("\\.");
            if (parts.length > 1) {
                return parts[2]; // Take the second part after the "."
            } else {
                return ""; // Handle the case where there is no "."
            }
        } finally {
            process.destroy();
        }
    }



   /* public static String decryptWithSymmetricKey(String encryptedData, String symmetricKeyPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "openssl", "enc", "-d", "-aes-256-cbc", "-a", "-salt", "-in", "-", "-pass", "file:" + symmetricKeyPath);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (var os = process.getOutputStream();
             var is = process.getInputStream()) {
            // Send the encryptedData to the process's standard input
            os.write(encryptedData.getBytes());
            os.flush(); // Flush the stream to ensure data is sent to the process
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder decryptedData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                decryptedData.append(line);
            }
            return decryptedData.toString();
        } finally {
            process.destroy();
        }
    }*/


  /*  public static String decryptWithSymmetricKey(String encryptedData, String symmetricKeyPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "openssl", "enc", "-d", "-aes-256-cbc", "-a", "-salt", "-in", "-", "-pass", "file:" + symmetricKeyPath);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (var os = process.getOutputStream();
             var is = process.getInputStream()) {
            os.write(encryptedData.getBytes());
            os.flush(); // Add this line to ensure data is sent to the process

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder decryptedData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                decryptedData.append(line);
            }
            return decryptedData.toString();
        } finally {
            process.destroy();
        }
    }
*/



   /* public static String decryptWithSymmetricKey(String encryptedData, String symmetricKeyPath) throws IOException {
        // Create temporary input and output files
        File inputTempFile = File.createTempFile("input", ".tmp");
        File outputTempFile = File.createTempFile("output", ".tmp");

        try {
            // Write the encrypted data to the temporary input file
            Files.write(inputTempFile.toPath(), encryptedData.getBytes());

            // Build the openssl command
            List<String> opensslCommand = new ArrayList<>();
            opensslCommand.add("openssl");
            opensslCommand.add("enc");
            opensslCommand.add("-d");
            opensslCommand.add("-aes-256-cbc");
            opensslCommand.add("-a");
            opensslCommand.add("-salt");
            opensslCommand.add("-in");

            // Replace backslashes with forward slashes in the input file path
            opensslCommand.add(inputTempFile.getAbsolutePath().replace("\\", "/"));

            opensslCommand.add("-out");

            // Replace backslashes with forward slashes in the output file path
            opensslCommand.add(outputTempFile.getAbsolutePath().replace("\\", "/"));

            opensslCommand.add("-pass");
            opensslCommand.add("file:" + symmetricKeyPath);

            // Start the openssl process
            ProcessBuilder processBuilder = new ProcessBuilder(opensslCommand);
            processBuilder.redirectErrorStream(true);
            System.out.println(processBuilder.command());
            Process process = processBuilder.start();
            // Capture and print the output of the openssl command
            StringBuilder outputBuilder = new StringBuilder();
            try (Scanner scanner = new Scanner(process.getInputStream())) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    outputBuilder.append(line).append(System.lineSeparator());
                }
            }
            process.waitFor();

            // Read the decrypted data from the output file
            byte[] decryptedDataBytes = Files.readAllBytes(outputTempFile.toPath());
            String decryptedData = new String(decryptedDataBytes);

            // Print the output of the openssl command
            System.out.println("OpenSSL Output:");
            System.out.println(outputBuilder.toString());

            return decryptedData;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Decryption process interrupted", e);
        } finally {
            // Clean up temporary files
            inputTempFile.delete();
            outputTempFile.delete();
        }
    }*/

  /*  public static String decryptWithSymmetricKey(String encryptedData, String symmetricKeyPath) throws IOException {
        File inputTempFile = null;
        File outputTempFile = null;
        try {
            // Create a temporary input file to store the encrypted data
            inputTempFile = File.createTempFile("input", ".txt"); // Use .txt extension
            Files.write(inputTempFile.toPath(), encryptedData.getBytes());

            // Create a temporary output file to store the decrypted data
            outputTempFile = File.createTempFile("output", ".txt"); // Use .txt extension

            // Build the openssl command
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "openssl", "enc", "-d", "-aes-256-cbc", "-a", "-salt", "-in", inputTempFile.getAbsolutePath().replace("\\", "/"),
                    "-out", outputTempFile.getAbsolutePath().replace("\\", "/"), "-pass", "file:" + symmetricKeyPath);
            processBuilder.redirectErrorStream(true);

            System.out.println(processBuilder.command());
            System.out.println(processBuilder.redirectErrorStream());
            // Start the process
            Process process = processBuilder.start();

            try (var os = process.getOutputStream();
                 var is = process.getInputStream();
                 var errorStream = process.getErrorStream()) {
                os.write(encryptedData.getBytes());
                os.close();

                // Read the decrypted data from the output file
                byte[] decryptedDataBytes = Files.readAllBytes(outputTempFile.toPath());
                String decryptedData = new String(decryptedDataBytes);

                // Print any error messages from openssl
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                String errorMessage;
                while ((errorMessage = errorReader.readLine()) != null) {
                    System.err.println("OpenSSL Error: " + errorMessage);
                }

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    return decryptedData;
                } else {
                    throw new IOException("openssl command failed with exit code: " + exitCode);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Decryption process interrupted", e);
        } finally {
            // Clean up temporary files
            if (inputTempFile != null) {
                inputTempFile.delete();
            }
            if (outputTempFile != null) {
                outputTempFile.delete();
            }
        }
    }*/



   /* public static String decryptWithSymmetricKey(String encryptedData, String symmetricKeyPath) throws IOException {
        File inputTempFile = null;
        File outputTempFile = null;
        try {
            // Obtenez le répertoire de travail actuel
            String currentDirectory = System.getProperty("user.dir");

            // Créez un fichier temporaire pour stocker les données chiffrées
            inputTempFile = File.createTempFile("input", ".txt", new File(currentDirectory));
            Files.write(inputTempFile.toPath(), encryptedData.getBytes());

            // Créez un fichier temporaire pour stocker les données déchiffrées
            outputTempFile = File.createTempFile("output", ".txt", new File(currentDirectory));

            // Construisez la commande openssl
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "openssl", "enc", "-d", "-aes-256-cbc", "-a", "-salt", "-in", inputTempFile.getName(),
                    "-out", outputTempFile.getName(), "-pass", "file:" + symmetricKeyPath);
            processBuilder.directory(new File(currentDirectory)); // Définissez le répertoire de travail actuel
            processBuilder.redirectErrorStream(true);

            System.out.println(processBuilder.command());
            System.out.println(processBuilder.redirectErrorStream());
            // Démarrez le processus
            Process process = processBuilder.start();

            // Écrivez les données chiffrées dans l'entrée du processus
            try (var os = process.getOutputStream();
                 var is = process.getInputStream();
                 var errorStream = process.getErrorStream()) {
                os.write(encryptedData.getBytes());
                os.close();

                // Lisez les données déchiffrées à partir du fichier de sortie temporaire
                byte[] decryptedDataBytes = Files.readAllBytes(outputTempFile.toPath());
                String decryptedData = new String(decryptedDataBytes);

                // Affichez les messages d'erreur éventuels d'openssl
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                String errorMessage;
                while ((errorMessage = errorReader.readLine()) != null) {
                    System.err.println("OpenSSL Error: " + errorMessage);
                }

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    return decryptedData;
                } else {
                    throw new IOException("openssl command failed with exit code: " + exitCode);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Decryption process interrupted", e);
        } finally {
            // Nettoyez les fichiers temporaires
            if (inputTempFile != null) {
                inputTempFile.delete();
            }
            if (outputTempFile != null) {
                outputTempFile.delete();
            }
        }
    }*/


/*
    public static String decryptWithSymmetricKey(String encryptedData, String symmetricKeyPath) throws IOException {
        File inputTempFile = null;
        File outputTempFile = null;
        try {
            // Get the current directory
            File currentDir = new File(System.getProperty("user.dir"));

            // Create a temporary input file with a unique name in the current directory
            inputTempFile = File.createTempFile("input", ".txt", currentDir);
            Files.write(inputTempFile.toPath(), encryptedData.getBytes());

            // Create a temporary output file with a unique name in the current directory
            outputTempFile = File.createTempFile("output", ".txt", currentDir);

            // Get the names of the input and output files
            String inputFileName = inputTempFile.getName();
            String outputFileName = outputTempFile.getName();

            // Build the OpenSSL command using only file names
            String opensslCommand = String.format("openssl enc -d -aes-256-cbc -a -salt -in %s -out %s -pass file:%s",
                    inputFileName, outputFileName, symmetricKeyPath);

            // Log the OpenSSL command
            System.out.println("Executing command: " + opensslCommand);

            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", opensslCommand);
            processBuilder.directory(currentDir); // Set the working directory to the current directory
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Write the encrypted data to the process input stream
            try (var os = process.getOutputStream()) {
                os.write(encryptedData.getBytes());
                os.close();
            }

            // Read the output and error streams concurrently
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Command Output: " + line);
                }
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                // Read the decrypted data from the output file
                byte[] decryptedDataBytes = Files.readAllBytes(outputTempFile.toPath());
                String decryptedData = new String(decryptedDataBytes);

                return decryptedData;
            } else {
                throw new IOException("openssl command failed with exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Decryption process interrupted", e);
        } finally {
            // Clean up temporary files
            if (inputTempFile != null) {
                inputTempFile.delete();
            }
            if (outputTempFile != null) {
                outputTempFile.delete();
            }
        }
    }
*/

    public static String decryptWithSymmetricKey(String encryptedData, String symmetricKeyPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "openssl", "enc", "-d", "-aes-256-cbc", "-a", "-salt", "-in", "-", "-out", "-",
                "-pass", "file:" + symmetricKeyPath);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (var os = process.getOutputStream();
             var is = process.getInputStream()) {
            os.write(encryptedData.getBytes());
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder decryptedData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                decryptedData.append(line);
            }
            return decryptedData.toString();
        } finally {
            process.destroy();
        }
    }


    public static String decrypt(String encryptedData, String symmetricKey) throws Exception {
        // Create a SecretKey object from the symmetric key
        SecretKey secretKey = new SecretKeySpec(symmetricKey.getBytes(), "AES");

        // Create a Cipher object for decryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Initialize the cipher for decryption
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));

        // Decrypt the data
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));

        // Return the decrypted data as a string
        return new String(decryptedData);
    }


}
