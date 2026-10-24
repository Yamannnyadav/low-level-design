package com.lowleveldesign.notificationsystem.model;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Reusable message with {placeholders}, e.g. "Hi {name}, order {orderId} shipped". */
public class NotificationTemplate {
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{(\\w+)\\}");

    private final String id;
    private final String subjectTemplate;
    private final String bodyTemplate;

    public NotificationTemplate(String id, String subjectTemplate, String bodyTemplate) {
        this.id = id;
        this.subjectTemplate = subjectTemplate;
        this.bodyTemplate = bodyTemplate;
    }

    public String getId() { return id; }

    public String renderSubject(Map<String, String> params) { return render(subjectTemplate, params); }

    public String renderBody(Map<String, String> params) { return render(bodyTemplate, params); }

    private String render(String template, Map<String, String> params) {
        Matcher matcher = PLACEHOLDER.matcher(template);
        StringBuilder out = new StringBuilder();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = params.get(key);
            if (value == null) {
                throw new IllegalArgumentException("Missing template parameter '" + key + "' for template " + id);
            }
            matcher.appendReplacement(out, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(out);
        return out.toString();
    }
}
