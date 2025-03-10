package model;

/**
 * Send this back to the user, not model.Notification
 */
public class NotificationWrapper {
    private String title;
    private String body;
    private boolean isRead;

    public NotificationWrapper() {}

    public NotificationWrapper(model.Notification notification) {
        setBody(notification.getBody());
        setRead(notification.getIsRead());
        setTitle(notification.getTitle());
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }
    
    public boolean isRead() {
        return isRead;
    }
}
