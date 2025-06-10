package clb.backend.DTO;

import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private Long leadId;
    private List<Long> memberIds;
    private List<Long> taskIds;

    // Constructors
    public ProjectDTO() {}

    public ProjectDTO(Long id, String title, String description, Long leadId, 
                     List<Long> memberIds, List<Long> taskIds) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.leadId = leadId;
        this.memberIds = memberIds != null ? memberIds : new ArrayList<>();
        this.taskIds = taskIds != null ? taskIds : new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getLeadId() { return leadId; }
    public void setLeadId(Long leadId) { this.leadId = leadId; }

    public List<Long> getMemberIds() { return memberIds; }
    public void setMemberIds(List<Long> memberIds) { this.memberIds = memberIds; }

    public List<Long> getTaskIds() { return taskIds; }
    public void setTaskIds(List<Long> taskIds) { this.taskIds = taskIds; }
}