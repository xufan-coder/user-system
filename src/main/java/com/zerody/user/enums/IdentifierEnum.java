package com.zerody.user.enums;

public enum IdentifierEnum {

    // 状态(1有效、-1删除(解除绑定)、0失效(审批已通过、已撤销、拒绝))
    EFFECTIVE(1,"有效"),
    INVALID(0,"失效(审批已通过、已撤销、拒绝)"),
    DELETE(-1,"删除(解除绑定)");

    private Integer value;

    private String text;

    IdentifierEnum(Integer value, String text){
        this.value = value;
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
