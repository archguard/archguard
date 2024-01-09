package cc.unitmesh.untitled.demo.dto;

import cc.unitmesh.untitled.demo.entity.User;
import lombok.Data;

@Data
public class CreateBlogRequest {
    private String title;
    private String content;
    private User user;
}