package com.yupi.yuaicodemother.aop;

import cn.hutool.core.util.ObjUtil;
import com.yupi.yuaicodemother.annotation.AuthCheck;
import com.yupi.yuaicodemother.constant.UserConstant;
import com.yupi.yuaicodemother.exception.BusinessException;
import com.yupi.yuaicodemother.exception.ErrorCode;
import com.yupi.yuaicodemother.model.entity.User;
import com.yupi.yuaicodemother.model.enums.UserRoleEnum;
import com.yupi.yuaicodemother.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
@Aspect
public class AuthInterceptor  {
    @Resource
    private UserService userService;


    //环绕通知
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        //获取签注上的参数
        String mustRole = authCheck.mustRole();
        //若没有参数，则代表任何人都能调用，放行
        UserRoleEnum requiredRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        if (ObjUtil.isEmpty(requiredRoleEnum)) {
            return joinPoint.proceed();
        }
        //再取出用户的身份信息，进行一定判断
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        User user = userService.getLoginUser(request);
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(user.getUserRole());
        if(userRoleEnum==null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //如果签注参数为admin，且用户身份不是，则抛异常
        if(requiredRoleEnum.equals(UserRoleEnum.ADMIN)&&userRoleEnum.equals(UserRoleEnum.USER)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //其他情况则返回正常
        return joinPoint.proceed();

    }
}