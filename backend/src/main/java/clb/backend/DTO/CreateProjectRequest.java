package clb.backend.DTO;

import java.util.ArrayList;
import java.util.List;

public class CreateProjectRequest {
    private String title;
    private String description;
    private List<Long> memberIds;

    // Constructors
    public CreateProjectRequest() {}

    public CreateProjectRequest(String title, String description, List<Long> memberIds) {
        this.title = title;
        this.description = description;
        this.memberIds = memberIds != null ? memberIds : new ArrayList<>();
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Long> getMemberIds() { return memberIds; }
    public void setMemberIds(List<Long> memberIds) { this.memberIds = memberIds; }
}