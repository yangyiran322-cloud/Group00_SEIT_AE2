package uk.ac.gla.seit.ae2.repository;

import uk.ac.gla.seit.ae2.domain.TeachingRequirement;
import uk.ac.gla.seit.ae2.domain.RequirementStatus;

import java.util.List;

public interface RequirementRepository {
    String add(TeachingRequirement req);
    TeachingRequirement findById(String id);
    List<TeachingRequirement> findAll();
    List<TeachingRequirement> findByStatus(RequirementStatus status);
    boolean update(TeachingRequirement req);
    boolean saveAll();
    boolean loadAll();
}