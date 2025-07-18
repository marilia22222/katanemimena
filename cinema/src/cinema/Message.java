package cinema;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private MessageType type;
    private Object data;
    private boolean success;
    private String errorMessage;
    
    public enum MessageType {
        // User operations
        REGISTER_USER,
        DELETE_USER,
        LOGIN_USER,
        LOGOUT_USER,
        
        // Event operations
        ADD_EVENT,
        DEACTIVATE_EVENT,
        GET_EVENTS,
        GET_EVENT_BY_ID,
        SEARCH_EVENTS,
        
        // Show operations
        ADD_SHOW,
        GET_SHOWS,
        GET_SHOW_BY_ID,
        
        // Order operations
        CREATE_ORDER,
        CANCEL_ORDER,
        PROCESS_PAYMENT,
        GET_USER_ORDERS,
        
        // Special operations
        DISCOUNT_NOTIFICATION,
        
        // Response
        RESPONSE
    }
    
    public Message() {
        this.success = true;
    }
    
    public Message(MessageType type) {
        this.type = type;
        this.success = true;
    }
    
    public Message(MessageType type, Object data) {
        this.type = type;
        this.data = data;
        this.success = true;
    }
    
    // Getters and Setters
    public MessageType getType() {
        return type;
    }
    
    public void setType(MessageType type) {
        this.type = type;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.success = false;
    }
    
    @Override
    public String toString() {
        return "Message{" + "type=" + type + ", success=" + success + 
               (errorMessage != null ? ", error=" + errorMessage : "") + '}';
    }
}
