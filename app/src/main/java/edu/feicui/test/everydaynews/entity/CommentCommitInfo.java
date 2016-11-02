package edu.feicui.test.everydaynews.entity;

/**
 * Created by Administrator on 16-10-12.
 */
public class CommentCommitInfo {
    public int status;
    public String message;
    public CommentCommitData data;

    @Override
    public String toString() {
        return "CommentCommitInfo{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
