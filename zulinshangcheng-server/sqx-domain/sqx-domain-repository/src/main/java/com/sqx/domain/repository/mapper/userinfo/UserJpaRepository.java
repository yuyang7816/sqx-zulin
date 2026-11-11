package com.sqx.domain.repository.mapper.userinfo;

import com.sqx.domain.repository.entity.app.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Long count(Specification<UserEntity> specification);

    /**
     * 获取授权会员用户
     *
     * @return
     */
//    List<UserEntity> findAllByRelationIdNotNullOrderByIdDesc();

    /**
     * 获取未提现用户
     *
     * @return
     */
//    List<UserEntity> findAllByRelationIdIsNullOrderByIdDesc();

    /**
     * 获取积分用户
     *
     * @return
     */
//    List<UserEntity> findAllByOrderJifenNotNullOrderByIdDesc();

    Page<UserEntity> findAll(Specification<UserEntity> specification, Pageable pageable);

    List<UserEntity> findAll(Specification<UserEntity> specification);

}
