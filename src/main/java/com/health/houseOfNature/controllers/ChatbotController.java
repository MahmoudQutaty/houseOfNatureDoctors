package com.health.houseOfNature.controllers;

import com.health.houseOfNature.models.Department;
import com.health.houseOfNature.models.Doctor;
import com.health.houseOfNature.repositories.DepartmentRepository;
import com.health.houseOfNature.repositories.DoctorRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String OPENAI_API_KEY = "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";

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping("/train")
    public String trainModel() {
        List<String> doctors = doctorRepository.findAll().stream()
                .map(doc -> "Doctor: " + doc.getName() + " (Department: " + doc.getDepartment().getName() + ")")
                .collect(Collectors.toList());

        List<String> departments = departmentRepository.findAll().stream()
                .map(Department::getName)
                .collect(Collectors.toList());

        return "Training data prepared:\nDoctors: " + String.join(", ", doctors) + "\nDepartments: " + String.join(", ", departments);
    }

    @PostMapping("/query")
    public String queryChatbot(@RequestBody String userMessage) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // **1. Retrieve data from the database**
            List<String> doctors = doctorRepository.findAll().stream()
                    .map(doc -> "Doctor: " + doc.getName() + " (Department: " + doc.getDepartment().getName() + ")")
                    .collect(Collectors.toList());

            List<String> departments = departmentRepository.findAll().stream()
                    .map(Department::getName)
                    .collect(Collectors.toList());

            String dbContext = "Available Doctors: " + String.join(", ", doctors) + ". "
                    + "Departments: " + String.join(", ", departments) + ".";

            // **2. Prepare the OpenAI API request**
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-4");

            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "system").put("content", "You are a Medical Center assistant chatbot. and here is information about the center (House of Nature is an AsiaOne award winner, DOH licensed Alternative Medicine & Rehabilitation Centre, with a Vision to enable people \"Live Healthy and Heal Naturally\". We provide preventive as well as curative healthcare and complementary, non-invasive or minimally invasive treatments. Our specialties include Ayurveda, Yoga, Chiropractic, Unani, Hijama, Naturopathy, Homeopathy & General Medicine. Our highly qualified & experienced team of licensed healthcare professionals, serve the clients with compassion and care. Located in Al Bateen, Abu Dhabi, We are also home to Abu Dhabi's first salt room. Come and meet our DOH licensed health professionals today and give yourself the gift of health.\n" +
                    "\n,). you also have to recommend the best doctor in the center to helping the person based on the problem he have, and recommend him a good doctor based on his skills and licenses, and make the response well structured and points and represent the doctor information in a good format."));
            messages.put(new JSONObject().put("role", "user").put("content", userMessage));
            messages.put(new JSONObject().put("role", "assistant").put("content", dbContext)); // Pass database context here

            requestBody.put("messages", messages);

            // **3. Set headers**
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
            headers.set("Content-Type", "application/json");

            // **4. Send the request to OpenAI**
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
            String response = restTemplate.postForObject(OPENAI_API_URL, requestEntity, String.class);

            // **5. Parse the OpenAI response**
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to process the query.";
        }
    }



}
