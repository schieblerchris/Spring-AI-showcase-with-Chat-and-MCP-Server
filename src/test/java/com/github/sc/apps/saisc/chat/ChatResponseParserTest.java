package com.github.sc.apps.saisc.chat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatResponseParserTest {

    private static final String WHAT_TIME_IS_IT_RESPONSE = "<think>\n" +
            "Okay, the user is asking for the current time. Let me check the available tools. There's a function called get_current_time that returns the current time. I don't need any parameters for that. So I should call get_current_time without any arguments. Let me make sure I'm using the right tool. Yep, the description says it returns the current time. Alright, I'll generate the tool call.\n" +
            "</think>\n" +
            "\n" +
            "<think>\n" +
            "Okay, the user asked for the current time. I called the get_current_time function, and it returned \"12:31:28\". So I need to present that to the user.\n" +
            "\n" +
            "Let me check if there's anything else they might need. The response is straightforward, so I'll just state the current time as 12:31:28. No need for extra info here.\n" +
            "\n" +
            "Wait, the function response is a string, so I should format it properly. Maybe add a time zone? But the function doesn't specify that. Since the user didn't mention time zones, I'll just go with the time given.\n" +
            "\n" +
            "Alright, the answer is the current time is 12:31:28.\n" +
            "</think>\n" +
            "\n" +
            "The current time is **12:31:28**.";

    private static final String WHO_IS_EXPERT_IN_CYCLING = "<think>\n" +
            "Okay, the user is asking, \"Who is expert in cycling?\" So I need to figure out how to use the provided tools to answer this.\n" +
            "\n" +
            "First, I should check if there's a hobby called cycling. The functions related to hobbies are list_all_hobbies, list_all_hobbies_by_category_name, etc. Maybe I should list all hobbies to see if cycling is one of them.\n" +
            "\n" +
            "Wait, the function list_all_hobbies might give me the list of hobbies. Let me call that first. Once I have the hobby names, I can check if \"cycling\" is present. But the user says \"expert in cycling\", so maybe the hobby ID for cycling is known.\n" +
            "\n" +
            "Alternatively, maybe cycling is under a specific category. For example, the list_all_hobby_categories function can list all categories. Let's see.\n" +
            "\n" +
            "Wait, the user is asking for an expert in cycling, so I need to find the hobby ID for cycling first. Let's try to list all hobbies to see what's there.\n" +
            "\n" +
            "So, I'll call list_all_hobbies. That should return all hobbies with their IDs and names. Then I can look for the hobby named \"cycling\" or maybe it's a different name like \"cycling sports\" or something else.\n" +
            "\n" +
            "Alternatively, maybe \"cycling\" is a category. Wait, the functions have list_all_hobby_categories and list_all_hobbies_by_category_id. Hmm.\n" +
            "\n" +
            "Let me start by calling list_all_hobbies to get all hobby names.\n" +
            "\n" +
            "Wait, the function list_all_hobbies returns all hobbies. Let's do that.\n" +
            "\n" +
            "So first tool call: list_all_hobbies.\n" +
            "\n" +
            "Once I have the list, I can find the hobby ID for cycling. Suppose the hobby name is \"cycling\", then I can get its ID. Then, I need to find persons who are skilled in that hobby.\n" +
            "\n" +
            "Wait, the function list_all_persons_by_hobby_id would take a hobby ID and list all persons who have that hobby. Then, for each person, determine who is more skilled.\n" +
            "\n" +
            "But the user wants \"expert\", so maybe the person with the highest skill level.\n" +
            "\n" +
            "Wait, the functions available: determine_which_person_is_more_skilled_in_hobby takes two person IDs and a hobby ID to see who is more skilled. But to find the expert, I need to list all persons who have that hobby, then compare them pairwise? Or maybe the system has a way to get the most skilled.\n" +
            "\n" +
            "Wait, the problem says: \"Resolve the person at the last possible moment from id to the real deal.\" So probably the process is: find the hobby ID for cycling, then get all persons in that hobby, then determine who is the most skilled.\n" +
            "\n" +
            "But the tools don't have a direct function to get the most skilled person. The determine_which_person_is_more_skilled_in_hobby function takes two people and returns who is more skilled. So maybe we need to have a list of persons for that hobby, then compare them one by one.\n" +
            "\n" +
            "But first, need to get the hobby ID.\n" +
            "\n" +
            "Let me start by calling list_all_hobbies.\n" +
            "\n" +
            "So first tool call: list_all_hobbies.\n" +
            "</think>\n" +
            "\n" +
            "<think>\n" +
            "Okay, the user is asking who is expert in cycling. Let me see.\n" +
            "\n" +
            "First, I need to find the hobby ID for cycling. From the list_all_hobbies response, I can see that there's a hobby with name \"Cycling\" and hobbyId 809. Let me confirm that. Looking at the list: yes, hobbyId 809 is \"Cycling\" under hobbyCategoryId 701.\n" +
            "\n" +
            "Now, the next step is to find all persons who are associated with this hobby. The function list_all_persons_by_hobby_id takes a hobbyId and returns the list of persons. So I should call that function with hobbyId 809.\n" +
            "\n" +
            "Wait, the user wants the expert person. But the functions available don't directly give expertise levels. The determine_which_person_is_more_skilled_in_hobby function compares two people's skills in a hobby. But to use that, I need at least two persons. However, the user is asking for the expert, so maybe the list of persons for that hobby will have the most skilled one.\n" +
            "\n" +
            "Alternatively, maybe the list_all_persons_by_hobby_id will give me the list of people who do cycling, and then I can check which one is the most skilled. But the tools don't have a function to get the skill level directly. Hmm.\n" +
            "\n" +
            "Wait, the function determine_which_person_is_more_skilled_in_hobby requires two person IDs and a hobby ID. But if there are multiple people, maybe the system expects me to pick two and determine, but the user wants the best fit. However, the problem statement says \"resolve the person at the last possible moment from id to the real deal.\" So maybe first get the list of persons who have cycling as a hobby, then find the most skilled.\n" +
            "\n" +
            "But the tools don't have a function to get skill levels. Wait, perhaps the list_all_persons_by_hobby_id will give the list of persons who are involved in that hobby, and then the system can assume that the one with the highest skill is the expert. But the problem says \"best fitting person by availability and hobby skill level\".\n" +
            "\n" +
            "Wait, the user's question is \"Who is expert in cycling?\" So maybe the answer is the person with the highest skill in cycling. But since the tools don't have a direct way to get skill levels, perhaps the approach is:\n" +
            "\n" +
            "1. Get the hobby ID for cycling (809).\n" +
            "2. Get all persons who have this hobby (list_all_persons_by_hobby_id with hobbyId 809).\n" +
            "3. Then, for the list of persons, determine which one is the most skilled. But the function determine_which_person_is_more_skilled_in_hobby needs two people. So if there's only one person, that's the answer. If there are multiple, we need to compare them pairwise.\n" +
            "\n" +
            "But the problem says \"resolve the person at the last possible moment from id to the real deal\". So maybe after getting the list of persons, we can check their availability and skill.\n" +
            "\n" +
            "Wait, the tools also have is_person_on_vacation, but the user hasn't specified a date, so maybe we don't need that yet.\n" +
            "\n" +
            "Let me outline the steps:\n" +
            "\n" +
            "- Call list_all_persons_by_hobby_id with hobbyId 809 to get the list of persons who do cycling.\n" +
            "- Then, from that list, determine who is the most skilled. But how? The function determine_which_person_is_more_skilled_in_hobby takes two person IDs and a hobby ID. So if there are two people, call that function. If there's one person, that's the answer.\n" +
            "\n" +
            "But the user might have multiple people. Let's assume there are multiple. For example, if the list has two persons, say person1 and person2, then call determine_which_person_is_more_skilled_in_hobby with those two IDs and hobbyId 809.\n" +
            "\n" +
            "But the problem says \"resolve the person at the last possible moment from id to the real deal\". So first, get the list of persons for the hobby, then pick the most skilled one.\n" +
            "\n" +
            "So first step: call list_all_persons_by_hobby_id with hobbyId 809.\n" +
            "\n" +
            "Wait, the user's initial query was to find who is expert in cycling. So the first tool call was list_all_hobbies to find cycling's ID, which is 809.\n" +
            "\n" +
            "Now, next step is list_all_persons_by_hobby_id with hobbyId 809.\n" +
            "\n" +
            "Let me check the function: list_all_persons_by_hobby_id requires hobbyId as a parameter.\n" +
            "\n" +
            "So the tool call should be:\n" +
            "\n" +
            "{\"name\": \"list_all_persons_by_hobby_id\", \"arguments\": {\"hobbyId\": 809}}\n" +
            "\n" +
            "Then, once we have the list of persons, we can determine who is the expert.\n" +
            "\n" +
            "But the problem says \"resolve the person at the last possible moment from id to the real deal\". So the assistant should first get the list of persons for the hobby, then compare their skills if needed.\n" +
            "\n" +
            "So the next tool call is list_all_persons_by_hobby_id with hobbyId 809.\n" +
            "</think>\n" +
            "\n" +
            "<think>\n" +
            "Okay, so the user is asking who is an expert in cycling. Let me go through the steps I took.\n" +
            "\n" +
            "First, I used the list_all_hobbies function to see all hobbies. The response showed that cycling has hobbyId 809. That's good to know.\n" +
            "\n" +
            "Next, I called list_all_persons_by_hobby_id with hobbyId 809 to find people who have cycling as a hobby. The response gave two people: Christopher Meyer (personId 200) and Hildegart Birnbaum (personId 201). \n" +
            "\n" +
            "Now, I need to figure out who is more skilled in cycling between these two. The tool determine_which_person_is_more_skilled_in_hobby requires both person IDs and the hobby ID. So I should call that function with firstPersonId 200, secondPersonId 201, and hobbyId 809.\n" +
            "\n" +
            "Wait, but the user asked for the expert, so maybe I need to check their skills. Let me make sure. The function will return who is more skilled. If they're equally skilled, it might return both. But the user wants the best fit, so probably the one with higher skill.\n" +
            "\n" +
            "So the next step is to use determine_which_person_is_more_skilled_in_hobby with those IDs and hobby ID. Let me structure the tool call.\n" +
            "</think>\n" +
            "\n" +
            "<think>\n" +
            "Okay, let's see. The user asked, \"Who is expert in cycling?\" So I started by listing all hobbies to find cycling. The list shows that cycling has hobbyId 809.\n" +
            "\n" +
            "Then I used list_all_persons_by_hobby_id with hobbyId 809, which gave two people: Christopher Meyer (id 200) and Hildegart Birnbaum (id 201). \n" +
            "\n" +
            "Next, I needed to figure out who's more skilled in cycling. So I called determine_which_person_is_more_skilled_in_hobby with both person IDs and the hobby ID. The response was [200], meaning Christopher Meyer is the more skilled one.\n" +
            "\n" +
            "I should check if there's any other info needed, like vacations or other details, but the user just asked for the expert, and the function result points to person 200. Since the response is straightforward and the tool says 200 is the expert, I can conclude Christopher Meyer is the expert in cycling.\n" +
            "\n" +
            "Wait, the tool response was [200], so that's the ID. The name is Christopher Meyer. I should present that as the answer. No need for further checks unless the user asks about availability or something else, but the question was just who's the expert. So the answer is Christopher Meyer.\n" +
            "</think>\n" +
            "\n" +
            "The expert in cycling is **Christopher Meyer** (person ID 200).";

    private ChatResponseParser cut = new ChatResponseParser();

    @Test
    void parse() {
        cut.parse(WHAT_TIME_IS_IT_RESPONSE);
    }
}