package com.latmn.kangaroo.framework.core.result;


public class FieldResult {
    private String name;
    private String tips;
    private Object rejectedValue;
    private FieldResult(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    @Override
    public String toString() {
        return "KangarooFieldDomain{" +
                "name='" + name + '\'' +
                ", tips='" + tips + '\'' +
                ", rejectedValue=" + rejectedValue +
                '}';
    }

    public FieldResult(String name, String tips, Object rejectedValue) {
        this.name = name;
        this.tips = tips;
        this.rejectedValue = rejectedValue;
    }
}
