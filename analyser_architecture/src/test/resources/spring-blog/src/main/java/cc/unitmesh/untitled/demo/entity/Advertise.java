package cc.unitmesh.untitled.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Advertise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String content;

    private String author;

    public Advertise(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Advertise() {

    }
}
