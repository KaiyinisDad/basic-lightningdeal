package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.UserPasswordDO;

public interface UserPasswordDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Mon Jun 27 16:55:36 CST 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Mon Jun 27 16:55:36 CST 2022
     */
    int insert(UserPasswordDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Mon Jun 27 16:55:36 CST 2022
     */
    int insertSelective(UserPasswordDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Mon Jun 27 16:55:36 CST 2022
     */
    UserPasswordDO selectByPrimaryKey(Integer id);
    UserPasswordDO selectByUserId(Integer UserId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Mon Jun 27 16:55:36 CST 2022
     */
    int updateByPrimaryKeySelective(UserPasswordDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Mon Jun 27 16:55:36 CST 2022
     */
    int updateByPrimaryKey(UserPasswordDO record);
}