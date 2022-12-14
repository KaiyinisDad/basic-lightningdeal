package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//service层还需要具体实现，所以需要这个包
public class UserServiceImplement implements UserService {


    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;
    @Override
    public UserModel getUserById(Integer id){
        //调用userdomapper获取对应用户dataobject
        UserDO userDO=userDOMapper.selectByPrimaryKey(id);
        //mybatis为我们自动生成的方法仅仅是selectbyprimarykey，但是我们要求通过用户id查询，所以要改造Userpassworddomapper。首先改造对应的xml文件,再改造对应的java文件
        //userPasswordDOMapper.selectByPrimaryKey()
        if(userDO==null){
            //对应用户不存在
            return null;
        }
        //通过用户id获取对应加密密码信息
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO,userPasswordDO);

    }
    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        //一定要做严格的校验
        if(userModel ==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);

        }
       //if( StringUtils.isEmpty(userModel.getName())||userModel.getGender() == null ||userModel.getAge()==null||StringUtils.isEmpty(userModel.getTelephone())){
         //   throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        //}
        ValidationResult result=validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

       //实现Model->dataobject方法
        UserDO userDO=convertFromModel(userModel);

        try {
            userDOMapper.insertSelective(userDO);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"your telphone number has been already existed!");
        }
        userModel.setId(userDO.getId());//为了解决参数不合法
       UserPasswordDO userPasswordDO=convertPassWordFromModel(userModel);
       userPasswordDOMapper.insertSelective(userPasswordDO);
       return;


    }
    @Override
    public UserModel validateLogin(String telphone,String encryptPassword) throws BusinessException {
        //通过用户手机获取用户信息
        UserDO userDO=userDOMapper.selectByTelphone(telphone);
        if(userDO==null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel=convertFromDataObject(userDO,userPasswordDO);


        //比对用户信息内加密密码是否和传输进来的密码相匹配
        if(!StringUtils.equals(encryptPassword,userModel.getEncryptPassword())) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;


    }
    private UserPasswordDO convertPassWordFromModel(UserModel userModel){
        if(userModel ==null){
            return null;
        }
        UserPasswordDO userPasswordDO=new UserPasswordDO();
        userPasswordDO.setEncryptPassword(userModel.getEncryptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }
    private UserDO convertFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserDO userDO= new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;

    }
    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO == null){
            return null;
        }

        UserModel userModel=new UserModel();
        BeanUtils.copyProperties(userDO,userModel);

        //不可能没有密码，纯粹为了保护
        if(userPasswordDO != null){
            userModel.setEncryptPassword(userPasswordDO.getEncryptPassword());

        }
        return userModel;

    }
}
