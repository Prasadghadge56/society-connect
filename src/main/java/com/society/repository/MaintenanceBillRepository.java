package com.society.repository;

import com.society.entity.MaintenanceBill;
import com.society.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaintenanceBillRepository extends JpaRepository<MaintenanceBill, Long>{

    // find bills by user
    List<MaintenanceBill> findByUser(User user);

    // find bills by user id
    List<MaintenanceBill> findByUserId(Long userId);

    // find bill by user and month
    List<MaintenanceBill> findByUserAndMonth(User user , String month);

    //find bills by status
    List<MaintenanceBill> findByStatus(String status);


    // Find bills by month
    List<MaintenanceBill> findByMonth(String month);

    // Count unpaid bills
    long countByStatus(String status);

    // Check if bill exists for specific user and month
    boolean existsByUserIdAndMonth(Long userId, String month);

    // Find bill by user ID and month (returns Optional)
    Optional<MaintenanceBill> findByUserIdAndMonth(Long userId, String month);

    // Get all bills for a specific user in descending order (latest first)
    List<MaintenanceBill> findByUserIdOrderByIdDesc(Long userId);

    // Get bills by user ID and status
    List<MaintenanceBill> findByUserIdAndStatus(Long userId, String status);

    // Custom query to find bills with user details
    @Query("SELECT b FROM MaintenanceBill b JOIN FETCH b.user WHERE b.user.id = :userId")
    List<MaintenanceBill> findBillsWithUserDetails(@Param("userId") Long userId);

    // Find latest bill for a user
    @Query("SELECT b FROM MaintenanceBill b WHERE b.user.id = :userId ORDER BY b.id DESC")
    Optional<MaintenanceBill> findLatestBillByUserId(@Param("userId") Long userId);


}

