package org.example.wildfly.repositories.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "mvms_camera")
public class Camera {

    @Id
    private int camera_id;
    private String name;

    @Override
    public String toString() {
        return "Camera{" +
                "camera_id=" + camera_id +
                ", name='" + name + '\'' +
                '}';
    }
}
