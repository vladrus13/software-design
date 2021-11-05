package aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
public class AspectLogRunner {

    @Around("@annotation(aspects.AspectProfile) && execution(* *(..))")
    public Object logExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder sb = new StringBuilder(joinPoint.toString() + "(");
        for (Object o : joinPoint.getArgs()) {
            String newO = o.toString();
            sb.append(newO.length() > 10 ? newO.substring(9) : newO).append(", ");
        }
        String s = sb.append("end)").toString();
        Euler.into(joinPoint.getArgs()[0].toString(), s);
        long start = System.nanoTime();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        long exit = System.nanoTime();
        Euler.onto(joinPoint.getArgs()[0].toString(), exit - start);
        Timer.add(joinPoint.toString(), exit - start);
        return result;
    }
}
