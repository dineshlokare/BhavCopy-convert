package com.example.BhavCopy.repository;

import com.example.BhavCopy.entity.Jobq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobqRepository extends JpaRepository<Jobq, Long> {
    Jobq findByReqid(String reqid);
}
