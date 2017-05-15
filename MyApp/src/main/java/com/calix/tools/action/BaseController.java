package com.calix.tools.action;

import com.jfinal.core.Controller;

/**
 * Created by calix on 16-10-25
 */
public abstract class BaseController extends Controller {

    /**
     * 将对象转为json串发送到前台
     * @param obj
     */
    void sendJSONResponse(Object obj){
        if(obj instanceof String) {
            renderText((String)obj);
        } else {
            renderJson(obj);
        }
    }
}
