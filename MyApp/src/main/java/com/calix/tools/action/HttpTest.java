package com.calix.tools.action;

/**
 * Created by calix on 16-10-25
 */
public class HttpTest extends BaseController {

    public void index() {
        render("index.jsp");
    }

    public void answer() {
        renderNull();
    }
}
