package com.ejemplo.ventas.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    private static final String CONTROLLER_METHODS =
            "execution(* com.ejemplo.ventas.controller..*(..))";

    @Before(CONTROLLER_METHODS)
    public void registrarEntrada(JoinPoint joinPoint) {
        LOGGER.info("Método: {}.{} - argumentos: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @AfterReturning(pointcut = CONTROLLER_METHODS, returning = "resultado")
    public void registrarRespuesta(JoinPoint joinPoint, Object resultado) {
        LOGGER.info("Respuesta correcta en {}.{}: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                resultado);
    }

    @AfterThrowing(pointcut = CONTROLLER_METHODS, throwing = "exception")
    public void registrarError(JoinPoint joinPoint, Throwable exception) {
        LOGGER.error("Error en {}.{}: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }

    @Around(CONTROLLER_METHODS)
    public Object medirTiempo(ProceedingJoinPoint joinPoint) throws Throwable {
        long inicio = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long duracionMs = (System.nanoTime() - inicio) / 1_000_000;
            LOGGER.info("Tiempo de ejecución de {}: {} ms",
                    joinPoint.getSignature().getName(), duracionMs);
        }
    }
}