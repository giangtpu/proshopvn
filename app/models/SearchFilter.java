package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giangdaika on 24/04/2016.
 */
public class SearchFilter {
    private List<SearchCondition> conditionList=new ArrayList<SearchCondition>();
    private String sortFieldName;
    private boolean isDesc=true;
    private int page=1;
    private int pageSize=50;

    public List<SearchCondition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<SearchCondition> conditionList) {
        this.conditionList = conditionList;
    }

    public String getSortFieldName() {
        return sortFieldName;
    }

    public void setSortFieldName(String sortFieldName) {
        this.sortFieldName = sortFieldName;
    }

    public boolean isDesc() {
        return isDesc;
    }

    public void setIsDesc(boolean isDesc) {
        this.isDesc = isDesc;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
