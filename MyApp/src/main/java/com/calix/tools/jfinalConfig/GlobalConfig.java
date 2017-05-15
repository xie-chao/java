package com.calix.tools.jfinalConfig;

import com.calix.tools.action.HttpTest;
import com.calix.tools.handler.CharsetHander;
import com.jfinal.config.*;
import com.jfinal.render.ViewType;

/**
 * Created by calix on 16-10-25
 *
 */
public class GlobalConfig extends JFinalConfig {

    public void configConstant(Constants constants) {
        constants.setDevMode(true);
        constants.setViewType(ViewType.JSP);
    }

    public void configRoute(Routes routes) {
        routes.add("/hello", HttpTest.class, "/");
    }

    public void configPlugin(Plugins plugins) {

    }

    public void configInterceptor(Interceptors interceptors) {

    }

    public void configHandler(Handlers handlers) {
        handlers.add(new CharsetHander());
    }
}
