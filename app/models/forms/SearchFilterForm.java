package models.forms;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minhnt31 on 19/02/2016.
 */
public class SearchFilterForm {
    private List<SearchConditionForm> conditionList = new ArrayList<SearchConditionForm>();
    private String sortFieldName;
    private boolean isDesc;
    private List<String> filter;
    private int page;
    private int pageSize;
    private boolean isResetPage;
    private long totalResult;
    private long totalPage;
    private boolean isResetCondition=false;

    public String validate() {
      // trim search content
        if(conditionList!=null && !conditionList.isEmpty()){
            for(SearchConditionForm condition: conditionList){
                String value = condition.getFieldValue();
                value = StringUtils.trimWhitespace(value);
                condition.setFieldValue(value);
            }

        }
        return null;
    }

    public SearchFilterForm() {
        List<SearchConditionForm> conditionListTemp = new ArrayList<SearchConditionForm>();
        SearchConditionForm searchConditionFormTemp = new SearchConditionForm();
        conditionListTemp.add(searchConditionFormTemp);
        this.conditionList = conditionListTemp;
        this.sortFieldName = "";
        this.isDesc = true;
        this.page = 1;
        this.pageSize = 50;
        this.isResetPage = false;
        this.totalResult = 0;
        this.totalPage = 0;
    }

    public List<SearchConditionForm> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<SearchConditionForm> conditionList) {
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

    public boolean isResetPage() {
        return isResetPage;
    }

    public void setIsResetPage(boolean isResetPage) {
        this.isResetPage = isResetPage;
    }

    public List<String> getFilter() {
        return filter;
    }

    public void setFilter(List<String> filter) {
        this.filter = filter;
    }

    public long getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(long totalResult) {
        this.totalResult = totalResult;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isResetCondition() {
        return isResetCondition;
    }

    public void setIsResetCondition(boolean isResetCondition) {
        this.isResetCondition = isResetCondition;
    }
}
