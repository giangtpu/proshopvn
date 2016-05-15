package models.forms;

/**
 * Created by minhnt31 on 01/03/2016.
 */
public class SearchConditionForm {
    private String fieldName;
    private String fieldType; // Date, Int, String
    private String fieldValue;
    private String optionHourValue;
    private String optionMinuteValue;
    private String compQueryOp; // eq, ne, gt, lt, gte , lte, like, nlike

    public SearchConditionForm() {
        this.fieldName = "";
        this.fieldType = "";
        this.fieldValue = "";
        this.optionHourValue = "";
        this.optionMinuteValue = "";
        this.compQueryOp = "";
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getCompQueryOp() {
        return compQueryOp;
    }

    public void setCompQueryOp(String compQueryOp) {
        this.compQueryOp = compQueryOp;
    }

    public String getOptionHourValue() {
        return optionHourValue;
    }

    public void setOptionHourValue(String optionHourValue) {
        this.optionHourValue = optionHourValue;
    }

    public String getOptionMinuteValue() {
        return optionMinuteValue;
    }

    public void setOptionMinuteValue(String optionMinuteValue) {
        this.optionMinuteValue = optionMinuteValue;
    }
}
