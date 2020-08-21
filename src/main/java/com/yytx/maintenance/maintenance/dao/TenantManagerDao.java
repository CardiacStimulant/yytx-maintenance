package com.yytx.maintenance.maintenance.dao;

import com.github.pagehelper.Page;
import com.yytx.maintenance.maintenance.entity.TelephoneNumber;
import com.yytx.maintenance.maintenance.entity.Tenant;
import com.yytx.maintenance.pojo.SearchParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TenantManagerDao {
    Page<Tenant> queryPage(@Param("condition") SearchParams searchParams);

    Page<TelephoneNumber> queryTelephoneNumberPage(@Param("condition") SearchParams searchParams);

    TelephoneNumber selectTelephoneNumberById(@Param("id") Long id);

    int addTenantTelephoneNumber(TelephoneNumber telephoneNumber);

    int updateTenantTelephoneNumber(TelephoneNumber telephoneNumber);

    int checkTelephoneNumberExists(TelephoneNumber telephoneNumber);

    int deleteTenantTelephoneNumber(TelephoneNumber telephoneNumber);
}
