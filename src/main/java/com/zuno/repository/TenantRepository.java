package com.zuno.repository;

import com.zuno.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    List<Tenant> findByListingIdAndStatus(UUID listingId, Tenant.TenantStatus status);
    List<Tenant> findByRoomIdAndStatus(UUID roomId, Tenant.TenantStatus status);
}
