package myyuk.exam.data;

/**
 * Todo Entry
 *
 * id : unique sequence
 * content : todo content
 * createTime : the first time when created the todo-task. (epoch time)
 * updateTime : the latest time when modified the todo-task. (epoch time)
 *
 */
public class TodoEntry {
    private long id;
    private String content;
    private long createTime;
    private long updateTime;
    private boolean complete;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", complete=" + complete +
                '}';
    }
}
