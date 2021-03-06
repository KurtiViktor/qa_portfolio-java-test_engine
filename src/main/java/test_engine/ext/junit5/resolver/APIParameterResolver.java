package test_engine.ext.junit5.resolver;

import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import test_engine.api.rest.retrofit.APIRequests;
import test_engine.api.rest.retrofit.RetrofitAdapter;
import test_engine.out.config.TestEngineCfg;

/**
 * Junit 5 ParameterResolver для работы с апи.
 */
public class APIParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == APIRequests.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        TestEngineCfg cfg = ConfigCache.getOrCreate(TestEngineCfg.class);
        return RetrofitAdapter.build(cfg.apiBaseUrl());
    }

}
