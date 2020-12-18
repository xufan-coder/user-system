package com.zerody.user.execption;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zerody.common.bean.DataResult;
import com.zerody.common.util.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName GlobalExecptionHandler
 * @DateTime 2020/12/18_10:35
 * @Deacription TODO
 */
@RestControllerAdvice
@RestController
@Slf4j
public class GlobalExecptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DataResult MethodArgumentNotValidExceptionInfo(MethodArgumentNotValidException e){
        StringBuilder errorStr = new StringBuilder();
        List<ObjectError> errs = e.getBindingResult().getAllErrors();
        for (ObjectError err : errs){
            errorStr.append(err.getDefaultMessage()).append(",");
        }

//        log.error("{}",,JSON.toJSONString(e.getBindingResult().getPropertyEditorRegistry()));
        return new DataResult(ResultCodeEnum.RESULT_ERROR, false, errorStr.substring(0,errorStr.length()), null);
    }
}
