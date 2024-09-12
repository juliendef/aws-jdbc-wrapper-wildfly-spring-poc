package org.example.wildfly.repositories;

import org.example.wildfly.repositories.entities.Camera;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CameraRepository extends JpaRepository<Camera, Integer> {}
