package com.lowleveldesign.notificationsystem.service;

import com.lowleveldesign.notificationsystem.enums.ChannelType;
import com.lowleveldesign.notificationsystem.listener.NotificationEventPublisher;
import com.lowleveldesign.notificationsystem.model.Notification;
import com.lowleveldesign.notificationsystem.model.NotificationRequest;
import com.lowleveldesign.notificationsystem.model.NotificationTemplate;
import com.lowleveldesign.notificationsystem.model.User;
import com.lowleveldesign.notificationsystem.model.UserPreference;
import com.lowleveldesign.notificationsystem.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Public entry point (facade). send() is fast and non-blocking: it validates,
 * renders the template, applies user preferences, creates one Notification per
 * eligible channel, and hands each one to the dispatcher.
 */
public class NotificationService {

    private final UserService userService;
    private final TemplateService templateService;
    private final NotificationRepository repository;
    private final NotificationDispatcher dispatcher;
    private final NotificationEventPublisher publisher;
    private final AtomicLong sequence = new AtomicLong(1);

    public NotificationService(UserService userService, TemplateService templateService,
                               NotificationRepository repository, NotificationDispatcher dispatcher,
                               NotificationEventPublisher publisher) {
        this.userService = userService;
        this.templateService = templateService;
        this.repository = repository;
        this.dispatcher = dispatcher;
        this.publisher = publisher;
    }

    public List<Notification> send(NotificationRequest request) {
        User user = userService.getUser(request.getUserId());
        NotificationTemplate template = templateService.getTemplate(request.getTemplateId());

        Map<String, String> params = request.getParams();
        String subject = template.renderSubject(params);
        String body = template.renderBody(params);

        UserPreference preference = user.getPreference();
        Set<ChannelType> candidates = request.getChannels().isEmpty()
                ? EnumSet.allOf(ChannelType.class)
                : request.getChannels();

        // ALERT-type notifications can never be muted by the user.
        boolean muted = request.getType().isUserMutable() && preference.isTypeMuted(request.getType());

        List<Notification> created = new ArrayList<>();
        for (ChannelType channel : candidates) {
            if (!preference.isChannelEnabled(channel)) {
                continue;
            }

            Notification notification = new Notification(sequence.getAndIncrement(), user.getId(),
                    channel, request.getType(), request.getPriority(), subject, body);
            repository.save(notification);

            if (muted) {
                notification.markSkipped("user muted " + request.getType());
                publisher.publish(notification);
            } else {
                dispatcher.submit(notification);
            }
            created.add(notification);
        }
        return created;
    }

    public List<Notification> getHistory(String userId) {
        return repository.findByUser(userId);
    }
}
