package utils;

/**
 * A utility class for JSON responses in API endpoints
 */
public class JsonResponse {
    private boolean success;
    private String message;
    private Object data;
    
    public JsonResponse() {
        this.success = true;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
}