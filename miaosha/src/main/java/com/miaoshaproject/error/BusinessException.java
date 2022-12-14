package com.miaoshaproject.error;

//包装器业务异常类实现
public class BusinessException extends Exception implements CommonError{

    private CommonError commonError;
    //直接接受Embusinesserror的传参用于构造业务异常
    public BusinessException(CommonError commonError){
        //不要漏了这一句，因为对Exception自身有一些初始化的机制
        super();
        this.commonError=commonError;
    }
    //接受自定义errmsg的方式构造业务异常
    public BusinessException(CommonError commonError,String errMsg){
        super();
        this.commonError=commonError;
        this.commonError.setErrMsg(errMsg);
    }
    @Override
    public int getErrCode() {
        return this.commonError.getErrCode() ;
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.setErrMsg(errMsg);
        return this;
    }
}
