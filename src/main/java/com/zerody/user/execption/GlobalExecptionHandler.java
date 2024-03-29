package com.zerody.user.execption;

import java.util.List;

import com.zerody.common.api.bean.DataResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.zerody.common.api.bean.R;

import lombok.extern.slf4j.Slf4j;

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
    public DataResult<Object> methodArgumentNotValidExceptionInfo(MethodArgumentNotValidException e){
        StringBuilder errorStr = new StringBuilder();
        List<ObjectError> errs = e.getBindingResult().getAllErrors();
        for (ObjectError err : errs){
            errorStr.append(err.getDefaultMessage()).append(",");
        }

        return R.error(errorStr.substring(0,errorStr.length()-1));
    }
}
