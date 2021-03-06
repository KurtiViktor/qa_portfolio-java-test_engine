package test_engine.ext.junit5.resolver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import test_engine.db.jpa.service.impl.JPAServiceImpl;

/**
 * Junit 5 ParameterResolver для работы с бд.
 */
public class DBParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == JPAServiceImpl.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return new JPAServiceImpl();
    }

}