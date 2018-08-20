package myyuk.exam.web.ajax;

/**
 * Todo Request
 * - The data to request from the client side.
 */
public class TodoRequest {
    private String content;
    private boolean complete;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
