package org.jeecgframework.web.activiti.util;
public class WXTemplateData {

    private String value; // 小标题

    private String color; // 模板内容字体的颜色，不填默认黑色

    public WXTemplateData(String value, String color) {
        super();
        this.value = value;
        this.color = color;
    }

    public WXTemplateData() {
        super();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}