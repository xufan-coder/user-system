package com.zerody.user.enums;

/**
 * @author  DaBai
 * @date  2021/1/6 17:06
 */
public enum TemplateTypeEnum {


    STAFF_IMPORT(0,"员工导入","crm_staff.xlsx"),
    BACK_STAFF_IMPORT(1,"后台管理员工导入","bos_staff.xlsx");

    private Integer code;

    private String text;

    private String url;

    TemplateTypeEnum(Integer code, String text, String url) {
        this.code = code;
        this.text = text;
        this.url  = url;
    }


    public Integer getCode() {
        return code;
    }


    public void setCode(Integer code) {
        this.code = code;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static TemplateTypeEnum getByCode(Integer code){
        for(TemplateTypeEnum e : TemplateTypeEnum.values()){
            if(e.code .equals(code) ){
                return e;
            }
        }
        return null;
    }
}