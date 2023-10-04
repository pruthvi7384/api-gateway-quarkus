package com.api.gateway.repositories;

import com.api.gateway.domain.ProjectSetting;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProjectSettingRepository implements PanacheRepositoryBase<ProjectSetting,Long> {
}
