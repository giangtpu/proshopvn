package models;

/**
 * Created by giangdaika on 24/04/2016.
 */
public class SearchCondition {
//CheckSimPackage

    public SearchCondition() {
        this.mulCondition = 1;
    }

    public enum CompQueryOp {
        eq(0),
        ne(1),
        gt(2),
        lt(3),
        gte(4),
        lte(5),
        like(6),
        nlike(7);
        private final int code;

        private CompQueryOp(int code){
            this.code = code;
        }
        public int getCode(){
            return this.code;
        }
        public String getName(){
            return this.getName();
        }

        public boolean equal(int code)
        {
            return (this.code == code);
        }
    }
    private int mulCondition =1;
    private String fieldName;
    private String fieldType; // Date, Int, String
    private Object fieldValue;
    private CompQueryOp compQueryOp; // eq, ne, gt, lt, gte , lte, like, nlike

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

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public CompQueryOp getCompQueryOp() {
        return compQueryOp;
    }

    public void setCompQueryOp(CompQueryOp compQueryOp) {
        this.compQueryOp = compQueryOp;
    }

    public int getMulCondition() {
        return mulCondition;
    }

    public void setMulCondition(int mulCondition) {
        this.mulCondition = mulCondition;
    }
}
