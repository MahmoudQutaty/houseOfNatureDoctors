package com.health.houseOfNature.utils;

import java.util.List;

public class FuzzyMatchingUtil {

    // Function to calculate Levenshtein distance
    public static int levenshteinDistance(String s1, String s2) {
        int lenS1 = s1.length();
        int lenS2 = s2.length();
        int[][] dp = new int[lenS1 + 1][lenS2 + 1];

        for (int i = 0; i <= lenS1; i++) {
            for (int j = 0; j <= lenS2; j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else if (s1.charAt(i - 1) == s2.charAt(j - 1)) dp[i][j] = dp[i - 1][j - 1];
                else dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
            }
        }
        return dp[lenS1][lenS2];
    }

    // Improved method to find the closest skill by using a Levenshtein distance threshold
    public static String findClosestSkillWithThreshold(String userInput, List<String> validSkills, int threshold) {
        String closestMatch = null;
        int minDistance = Integer.MAX_VALUE;

        for (String skill : validSkills) {
            int distance = levenshteinDistance(userInput.toLowerCase(), skill.toLowerCase());
            if (distance <= threshold && distance < minDistance) {
                minDistance = distance;
                closestMatch = skill;
            }
        }
        return closestMatch;
    }

    // Function to find the closest skill with the best fuzzy match
    public static String findImprovedClosestSkill(String userInput, List<String> validSkills) {
        // Threshold for Levenshtein distance
        int threshold = 3;  // You can adjust this value as needed (lower means stricter matching)

        // First, try to find the closest match using the thresholded Levenshtein distance
        String closestMatch = findClosestSkillWithThreshold(userInput, validSkills, threshold);

        // If no match found within the threshold, try to match substrings or components of the input
        if (closestMatch == null) {
            for (String skill : validSkills) {
                // Check if the user input is a substring of the skill (case-insensitive)
                if (skill.toLowerCase().contains(userInput.toLowerCase())) {
                    closestMatch = skill;
                    break;
                }
            }
        }

        // If no match found, fall back to finding the closest match without the threshold
        if (closestMatch == null) {
            closestMatch = findClosestSkillWithThreshold(userInput, validSkills, Integer.MAX_VALUE);
        }

        return closestMatch;
    }
}
