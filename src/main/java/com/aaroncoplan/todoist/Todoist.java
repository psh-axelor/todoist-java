package com.aaroncoplan.todoist;

import com.aaroncoplan.todoist.helpers.ProjectRequest;
import com.aaroncoplan.todoist.model.Project;
import com.aaroncoplan.todoist.model.Task;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.util.List;

public class Todoist {

    private final int HTTP_OK = 200;
    private final int HTTP_OK_NO_CONTENT = 204;

    private final String URL_BASE = "https://beta.todoist.com/API/v8";

    public Todoist(String token) {
        Unirest.setTimeouts(20_000, 20_000);
        Unirest.setDefaultHeader("Authorization", String.format("Bearer %s", token));
    }

    public List<Project> getAllProjects() {
        try {
            HttpResponse<String> response = Unirest.get(URL_BASE + "/projects")
                    .asString();
            if(response.getStatus() != HTTP_OK) {
                throw new Exception("HTTP STATUS " + response.getStatus());
            }
            return JsonAdapters.extractProjectList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Project getProject(long id) {
        try {
            HttpResponse<String> response = Unirest.get(URL_BASE + "/projects/" + id)
                    .asString();
            if(response.getStatus() != HTTP_OK) {
                throw new Exception("HTTP STATUS " + response.getStatus());
            }
            return JsonAdapters.extractProject(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Project createNewProject(String name) {
        try {
            HttpResponse<String> response = Unirest.post(URL_BASE + "/projects")
                    .header("Content-Type", "application/json")
                    .body(JsonAdapters.writeProjectRequest(new ProjectRequest(name)))
                    .asString();
            if(response.getStatus() != HTTP_OK) {
                throw new Exception("HTTP STATUS " + response.getStatus());
            }
            return JsonAdapters.extractProject(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateProject(long id, String name) {
        try {
            HttpResponse<String> response = Unirest.post(URL_BASE + "/projects/" + id)
                    .header("Content-Type", "application/json")
                    .body(JsonAdapters.writeProjectRequest(new ProjectRequest(name)))
                    .asString();
            if(response.getStatus() != HTTP_OK_NO_CONTENT) {
                throw new Exception("HTTP STATUS " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProject(long id) {
        try {
            HttpResponse<String> response = Unirest.delete(URL_BASE + "/projects/" + id)
                    .asString();
            if(response.getStatus() != HTTP_OK_NO_CONTENT) {
                throw new Exception("HTTP STATUS " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Task> getActiveTasks() {
        try {
            HttpResponse<String> response = Unirest.get(URL_BASE + "/tasks")
                    .asString();
            if(response.getStatus() != HTTP_OK) {
                throw new Exception("HTTP STATUS " + response.getStatus());
            }

            return JsonAdapters.extractTaskList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*public List<Task> getActiveTasks(Long projectId, Long labelId, String filter, String lang) {

    }*/
}
