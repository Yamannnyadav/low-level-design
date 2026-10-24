package com.lowleveldesign.notificationsystem.service;

import com.lowleveldesign.notificationsystem.exceptions.ResourceNotFoundException;
import com.lowleveldesign.notificationsystem.model.NotificationTemplate;

import java.util.concurrent.ConcurrentHashMap;

public class TemplateService {
    private final ConcurrentHashMap<String, NotificationTemplate> templates = new ConcurrentHashMap<>(); //templateId vs template

    public void registerTemplate(String id, String subjectTemplate, String bodyTemplate) {
        templates.put(id, new NotificationTemplate(id, subjectTemplate, bodyTemplate));
    }

    public NotificationTemplate getTemplate(String id) {
        NotificationTemplate template = templates.get(id);
        if (template == null) {
            throw new ResourceNotFoundException("Template not found: " + id);
        }
        return template;
    }
}
