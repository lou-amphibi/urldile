package ru.louamphibi.urldile.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

@Aspect
@Component
public class UrldileAspect {

    private Logger logger = Logger.getLogger(getClass().getName());

    @Pointcut("execution(* ru.louamphibi.urldile.service.*.*(..))")
    private void forService() {}

    @Pointcut("execution(* ru.louamphibi.urldile.error.UrldileExceptionHandler.*(..))")
    private void forExceptionHandler() {}

    @Pointcut("execution(* ru.louamphibi.urldile.repository.*.*(..))")
    private void forRepository() {}

    @Pointcut("execution(* ru.louamphibi.urldile.rest.*.*(..))")
    private void forRestController() {}



    @Before("forService() || forExceptionHandler() || forRepository() || forRestController()")
    private void before(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();

        String className = joinPoint.getTarget().getClass().getSimpleName();

        logger.info(className + " Aspect @Before: calling method: " + method);

        Object[] args = joinPoint.getArgs();

        for (Object o: args) {
            logger.info(" ---> argument: " + o);
        }
    }

    @AfterReturning(pointcut = "forService() || forExceptionHandler() || forRepository() || forRestController()",
            returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().toShortString();

        String className = joinPoint.getTarget().getClass().getSimpleName();

        logger.info(className + " Aspect @AfterReturning: from method: " + method);

        logger.info(" ---> result: " + result);
    }

    @AfterThrowing(pointcut = "forService() || forExceptionHandler() || forRepository() || forRestController()",
            throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        String method = joinPoint.getSignature().toShortString();

        String className = joinPoint.getTarget().getClass().getSimpleName();

        logger.warning(className + " Aspect  @AfterThrowing: from method: " + method);

        logger.warning(" ---> exception: " + ex + " message - " + ex.getMessage());
    }
}
